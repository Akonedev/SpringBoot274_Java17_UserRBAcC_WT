//package org.akon.userapp.security.user;
//
//import org.akon.userapp.security.jwt.JwtController;
//import org.akon.userapp.security.jwt.JwtFilter;
//import org.akon.userapp.security.jwt.JwtUtils;
//import org.akon.userapp.security.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//
//@RestController
//public class UserController {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    JwtUtils jwtUtils;
//
//    @Autowired
//    JwtController jwtController;
//
////    @PostMapping("/users")
////    public ResponseEntity add(@Valid @RequestBody AppUser appUser) {
////
////        AppUser existingAppUser = userRepository.findOneByEmail(appUser.getEmail());
////        if(existingAppUser != null) {
////            return new ResponseEntity("AppUser already existing", HttpStatus.BAD_REQUEST);
////        }
////        AppUser user = saveUser(appUser);
////        Authentication authentication = jwtController.logUserByEmail(appUser.getEmail(), appUser.getPassword());
////        String jwt = jwtUtils.generateToken(authentication);
////        HttpHeaders httpHeaders = new HttpHeaders();
////        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
////        return new ResponseEntity<>(user, httpHeaders, HttpStatus.OK);
////    }
//
////    @GetMapping(value = "/isConnected")
////    public ResponseEntity getUSerConnected() {
////        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////        if (principal instanceof UserDetails) {
////            return new ResponseEntity(((UserDetails) principal).getUsername(), HttpStatus.OK);
////        }
////        return new ResponseEntity("AppUser is not connected", HttpStatus.FORBIDDEN);
////    }
//
////    private AppUser saveUser(AppUser appUser) {
////        AppUser user = new AppUser();
////        user.setEmail(appUser.getEmail());
////        user.setPassword(new BCryptPasswordEncoder().encode(appUser.getPassword()));
////        user.setLastName(StringUtils.capitalize(appUser.getLastName()));
////        user.setFirstName(StringUtils.capitalize(appUser.getFirstName()));
////        userRepository.save(user);
////        return user;
////    }
//
//}