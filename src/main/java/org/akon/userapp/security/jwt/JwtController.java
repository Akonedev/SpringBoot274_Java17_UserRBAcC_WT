package org.akon.userapp.security.jwt;

import org.akon.userapp.security.configuration.MyUserDetailService;
import org.akon.userapp.load.dtos.UserDTO;
import org.akon.userapp.load.dtos.requests.RegisterUserAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {


    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private MyUserDetailService userService;


    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/user/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        Authentication authentication = logUserByEmail(jwtRequest.getEmail(), jwtRequest.getPassword());
        String jwt = jwtUtils.generateToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        Object principal = authentication.getPrincipal();
        return new ResponseEntity<>(new JwtResponse(((User) principal).getUsername()), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/user/register")
    public ResponseEntity<UserDTO> registerNewUserAccount(@RequestBody RegisterUserAccountDTO registerUserAccountDTO) {
        return new ResponseEntity<>(new UserDTO(userService.registerUserAccount(registerUserAccountDTO)), null, HttpStatus.CREATED);
    }

    public Authentication logUserByEmail(String mail, String password) {
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(new UsernamePasswordAuthenticationToken(mail, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @GetMapping(value = "/isConnected")
    public ResponseEntity getUSerConnected() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return new ResponseEntity(((UserDetails) principal).getUsername(), HttpStatus.OK);
        }
        return new ResponseEntity("AppUser is not connected", HttpStatus.FORBIDDEN);
    }


//    @PostMapping("/user/login")
////    @PostMapping
//    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequestByName jwtRequestByName) {
//        Authentication authentication = logUserByName(jwtRequestByName.getUsername(), jwtRequestByName.getPassword());
//        String jwt = jwtUtils.generateToken(authentication);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Authorization", "Bearer " + jwt);
//        Object principal = authentication.getPrincipal();
//        return new ResponseEntity<>(new JwtResponse(((User) principal).getUsername()), httpHeaders, HttpStatus.OK);
//    }

//    public Authentication logUserByName(String username, String password) {
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return authentication;
//    }



}