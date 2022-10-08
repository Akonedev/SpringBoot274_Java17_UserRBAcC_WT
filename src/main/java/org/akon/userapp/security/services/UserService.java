package org.akon.userapp.security.services;

import org.akon.userapp.load.dtos.UserDTO;
import org.akon.userapp.load.dtos.requests.CreateOrUpdateUserDTO;
import org.akon.userapp.load.dtos.requests.RegisterUserAccountDTO;
import org.akon.userapp.security.user.*;
import org.akon.userapp.security.exceptions.*;
import org.akon.userapp.security.repositories.AddressRepository;
import org.akon.userapp.security.repositories.ContactRepository;
import org.akon.userapp.security.repositories.RoleRepository;
import org.akon.userapp.security.repositories.UserRepository;
import org.akon.userapp.security.services.validation.EmailValidator;
import org.akon.userapp.security.services.validation.PasswordValidator;
import org.akon.userapp.security.services.validation.PhoneValidator;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${userApp.security.salt}")
    private String salt;

    private PasswordValidator passwordValidator;
    private EmailValidator emailValidator;
    private PhoneValidator phoneValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
    public UserService() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        passwordValidator = new PasswordValidator();
        emailValidator = new EmailValidator();
        phoneValidator = new PhoneValidator();
    }

    public List<UserDTO> getUserPresentationList() {
        ArrayList<UserDTO> listDto = new ArrayList<>();
        Iterable<AppUser> list = getUserList();
        list.forEach(e -> listDto.add(new UserDTO(e)));
        return listDto;
    }

    public AppUser getUserById(Long id) {
        if (id == null) {
            throw new InvalidUserIdentifierException("AppUser Id cannot be null");
        }
        Optional<AppUser> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new UserNotFoundException(String.format("AppUser not found for Id = %s", id));
    }

    public AppUser getUserByUsername(String username) {
        if (username == null) {
            throw new InvalidUsernameException("username cannot be null");
        }
        return userRepository.findByUsername(username);
    }

    public AppUser getUserByEmail(String email) {
        if (email == null) {
            throw new InvalidEmailException("email cannot be null");
        }
        return userRepository.findByEmail(email);
    }


    @Transactional
//    @Bean
    public AppUser registerUserAccount(RegisterUserAccountDTO registerUserAccountDTO) {
        if (registerUserAccountDTO == null) {
            throw new InvalidUserDataException("AppUser account data cannot be null");
        }

        checkIfUsernameNotUsed(registerUserAccountDTO.getUsername());
        passwordValidator.checkPassword(registerUserAccountDTO.getPassword());
        emailValidator.checkEmail(registerUserAccountDTO.getEmail());

        checkIfEmailNotUsed(registerUserAccountDTO.getEmail());

        // create the new appUser account: not all the appUser information required
        AppUser appUser = new AppUser();
        appUser.setUsername(registerUserAccountDTO.getUsername());
//        appUser.setPassword(EncryptionService.encrypt(registerUserAccountDTO.getPassword(), salt));
//        appUser.setPasswordencodePassword((registerUserAccountDTO.getPassword());
        appUser.setPassword(encodePassword(registerUserAccountDTO.getPassword()));

        appUser.setEmail(registerUserAccountDTO.getEmail());
//        appUser.setUsername(registerUserAccountDTO.getUsername());
//        appUser.setEmail(registerUserAccountDTO.getEmail());
        appUser.setEnabled(true);
        appUser.setSecured(false);

        // set gender
        Gender gender = Gender.getValidGender(registerUserAccountDTO.getGender());
        appUser.setGender(gender);

        addUserRole(appUser, Role.USER);
        appUser.setCreationDt(LocalDateTime.now());

        AppUser appUserCreated = userRepository.save(appUser);

        // set contact
        Contact contact = new Contact();
        contact.setEmail(registerUserAccountDTO.getEmail());

        addContactOnUser(appUserCreated, contact);

        // set empty address
        addAddressOnUser(appUserCreated, new Address());

        appUserCreated = userRepository.save(appUserCreated);

        log.info(String.format("AppUser %s has been created.", appUserCreated.getId()));
        return appUserCreated;
    }

    // check if the username has not been registered
    public void checkIfUsernameNotUsed(String username) {
        AppUser appUserByUsername = getUserByUsername(username);
            if (appUserByUsername != null) {
                String msg = String.format("The username %s it's already in use from another user with ID = %s",
                        appUserByUsername.getUsername(), appUserByUsername.getId());
                log.error(msg);
            throw new InvalidUserDataException(msg);
        }
    }

    // check if the email has not been registered
    public void checkIfEmailNotUsed(String email) {
        AppUser appUserByEmail = getUserByEmail(email);
        if (appUserByEmail != null) {
            String msg = String.format("The email %s it's already in use from another user with ID = %s",
                    appUserByEmail.getContact().getEmail(), appUserByEmail.getId());
            log.error(msg);
            throw new InvalidUserDataException(String.format("This email %s it's already in use.",
                    appUserByEmail.getContact().getEmail()));
        }
    }

    @Transactional
    public AppUser createUser(CreateOrUpdateUserDTO createUserDTO) {
        if (createUserDTO == null) {
            throw new InvalidUserDataException("AppUser account data cannot be null");
        }

        checkIfUsernameNotUsed(createUserDTO.getUsername());
        checkIfEmailNotUsed(createUserDTO.getEmail());
        passwordValidator.checkPassword(createUserDTO.getPassword());
        emailValidator.checkEmail(createUserDTO.getEmail());
        phoneValidator.checkPhone(createUserDTO.getPhone());

        // create the appUser
        AppUser appUser = new AppUser();
        appUser.setUsername(createUserDTO.getUsername());
        appUser.setPassword(EncryptionService.encrypt(createUserDTO.getPassword(), salt));

        appUser.setFirstName(createUserDTO.getFistname());
        appUser.setLastName(createUserDTO.getLastname());

        // set gender
        Gender gender = Gender.getValidGender(createUserDTO.getGender());
        appUser.setGender(gender);

        // date of birth
        appUser.setBirthDate(createUserDTO.getBirthDate());

        appUser.setEnabled(true);
        appUser.setSecured(createUserDTO.isSecured());

        appUser.setNote(createUserDTO.getNote());
        appUser.setCreationDt(LocalDateTime.now());

        // set default appUser the role
        addUserRole(appUser, Role.USER);

        AppUser appUserCreated = userRepository.save(appUser);

        // set contact
        Contact contact = new Contact();
        contact.setEmail(createUserDTO.getEmail());
        contact.setPhone(createUserDTO.getPhone());
        contact.setSkype(createUserDTO.getSkype());
        contact.setFacebook(createUserDTO.getFacebook());
        contact.setLinkedin(createUserDTO.getLinkedin());
        contact.setWebsite(createUserDTO.getWebsite());
        contact.setNote(createUserDTO.getContactNote());

        addContactOnUser(appUserCreated, contact);

        // set address
        Address address = new Address();
        address.setAddress(createUserDTO.getAddress());
        address.setAddress2(createUserDTO.getAddress2());
        address.setCity(createUserDTO.getCity());
        address.setCountry(createUserDTO.getCountry());
        address.setZipCode(createUserDTO.getZipCode());

        addAddressOnUser(appUserCreated, address);

        appUserCreated = userRepository.save(appUserCreated);

        log.info(String.format("AppUser %s has been created.", appUserCreated.getId()));
        return appUserCreated;
    }

    public void addContactOnUser(AppUser appUser, Contact contact) {
        contact.setAppUser(appUser);
        appUser.setContact(contact);

        log.debug(String.format("Contact information set on AppUser %s .", appUser.getId()));
    }

    public void addAddressOnUser(AppUser appUser, Address address) {
        address.setAppUser(appUser);
        appUser.setAddress(address);

        log.debug(String.format("Address information set on AppUser %s .", appUser.getId()));
    }

    public void addUserRole(AppUser appUser, long roleId) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException("Role cannot be null");
        }
        appUser.getRoles().add(roleOpt.get());
    }

    @Transactional
    public AppUser updateUser(Long id, CreateOrUpdateUserDTO updateUserDTO) {
        if (id == null) {
            throw new InvalidUserIdentifierException("Id cannot be null");
        }
        if (updateUserDTO == null) {
            throw new InvalidUserDataException("AppUser account data cannot be null");
        }

        Optional<AppUser> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("The appUser with Id = %s doesn't exists", id));
        }
        AppUser appUser = userOpt.get();

        // check if the username has not been registered
        AppUser appUserByUsername = getUserByUsername(updateUserDTO.getUsername());
        if (appUserByUsername != null) {
            // check if the appUser's id is different than the actual appUser
            if (!appUser.getId().equals(appUserByUsername.getId())) {
                String msg = String.format("The username %s it's already in use from another appUser with ID = %s",
                        updateUserDTO.getUsername(), appUserByUsername.getId());
                log.error(msg);
                throw new InvalidUserDataException(msg);
            }
        }

        passwordValidator.checkPassword(updateUserDTO.getPassword());
        emailValidator.checkEmail(updateUserDTO.getEmail());
        phoneValidator.checkPhone(updateUserDTO.getPhone());

        // check if the new email has not been registered yet
        AppUser appUserEmail = getUserByEmail(updateUserDTO.getEmail());
        if (appUserEmail != null) {
            // check if the appUser's email is different than the actual appUser
            if (!appUser.getId().equals(appUserEmail.getId())) {
                String msg = String.format("The email %s it's already in use from another appUser with ID = %s",
                        updateUserDTO.getEmail(), appUserEmail.getId());
                log.error(msg);
                throw new InvalidUserDataException(msg);
            }
        }

        // update the appUser
        appUser.setUsername(updateUserDTO.getUsername());

        // using the appUser's salt to secure the new validated password
//        appUser.setPassword(EncryptionService.encrypt(updateUserDTO.getPassword(), salt));
        appUser.setPassword(encodePassword(updateUserDTO.getPassword()));
        appUser.setFirstName(updateUserDTO.getFistname());
        appUser.setLastName(updateUserDTO.getLastname());

        // set gender
        Gender gender = Gender.getValidGender(updateUserDTO.getGender());
        appUser.setGender(gender);

        // date of birth
        appUser.setBirthDate(updateUserDTO.getBirthDate());

        appUser.setEnabled(updateUserDTO.isEnabled());
        appUser.setNote(updateUserDTO.getNote());

        // set contact: entity always present
        Contact contact = appUser.getContact();
        contact.setEmail(updateUserDTO.getEmail());
        contact.setPhone(updateUserDTO.getPhone());
        contact.setSkype(updateUserDTO.getSkype());
        contact.setFacebook(updateUserDTO.getFacebook());
        contact.setLinkedin(updateUserDTO.getLinkedin());
        contact.setWebsite(updateUserDTO.getWebsite());
        contact.setNote(updateUserDTO.getContactNote());

        appUser.setUpdatedDt(LocalDateTime.now());

        // set address
        Address address = appUser.getAddress();
        if (address == null) {
            address = new Address();
        }
        address.setAddress(updateUserDTO.getAddress());
        address.setAddress2(updateUserDTO.getAddress2());
        address.setCity(updateUserDTO.getCity());
        address.setCountry(updateUserDTO.getCountry());
        address.setZipCode(updateUserDTO.getZipCode());

        addAddressOnUser(appUser, address);

        AppUser appUserUpdated = userRepository.save(appUser);
        log.info(String.format("AppUser %s has been updated.", appUser.getId()));

        return appUserUpdated;
    }

    public Iterable<AppUser> getUserList() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (id == null) {
            throw new InvalidUserIdentifierException("Id cannot be null");
        }

        Optional<AppUser> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("AppUser not found with Id = %s", id));
        }

        // only not secured users can be deleted
        AppUser appUser = userOpt.get();
        if (appUser.isSecured()) {
            throw new UserIsSecuredException(String.format("AppUser %s is secured and cannot be deleted.", id));
        }

        userRepository.deleteById(id);
        log.info(String.format("AppUser %s has been deleted.", id));
    }

    @Transactional
    public AppUser login(String username, String password) {
        if ((Strings.isNullOrEmpty(username)) || (Strings.isNullOrEmpty(password))) {
            throw new InvalidLoginException("Username or Password cannot be null or empty");
        }

        AppUser appUser = getUserByUsername(username);
        if (appUser == null) {
            // invalid username
            throw new InvalidLoginException("Invalid username or password");
        }

        log.info(String.format("Login request from %s", username));

        // check the password
        if (EncryptionService.isPasswordValid(password, appUser.getPassword(), salt)) {
            // check if the appUser is enabled
            if (!appUser.isEnabled()) {
                // not enabled
                throw new InvalidLoginException("AppUser is not enabled");
            }

            // update the last login timestamp
            appUser.setLoginDt(LocalDateTime.now());
            userRepository.save(appUser);

            log.info(String.format("Valid login for %s", username));
        } else {
            throw new InvalidLoginException("Invalid username or password");
        }
        return appUser;
    }

    // add or remove a role on user

    @Transactional
    public AppUser addRole(Long id, Long roleId) {
        // check appUser
        Optional<AppUser> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("AppUser not found with Id = %s", id));
        }
        AppUser appUser = userOpt.get();

        // check role
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }

        Role role = roleOpt.get();

        appUser.getRoles().add(role);
        appUser.setUpdatedDt(LocalDateTime.now());

        userRepository.save(appUser);
        log.info(String.format("Added role %s on appUser id = %s", role.getRole(), appUser.getId()));

        return appUser;
    }

    @Transactional
    public AppUser removeRole(Long id, Long roleId) {
        // check appUser
        Optional<AppUser> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("AppUser not found with Id = %s", id));
        }
        AppUser appUser = userOpt.get();

        // check role
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }

        Role role = roleOpt.get();

        appUser.getRoles().remove(role);
        appUser.setUpdatedDt(LocalDateTime.now());

        userRepository.save(appUser);
        log.info(String.format("Removed role %s on appUser id = %s", role.getRole(), appUser.getId()));

        return appUser;
    }

}
