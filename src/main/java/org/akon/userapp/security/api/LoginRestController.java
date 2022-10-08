//package org.akon.userapp.security.api;
//
//import org.akon.userapp.security.configuration.MyUserDetailService;
//import org.akon.userapp.security.dtos.UserDTO;
//import org.akon.userapp.security.dtos.requests.LoginRequestDTO;
//import org.akon.userapp.security.jwt.JwtRequestByName;
//import org.akon.userapp.security.jwt.JwtResponse;
//import org.akon.userapp.security.jwt.JwtUtils;
//import org.akon.userapp.security.user.AppUser;
//import org.akon.userapp.security.services.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping(value = "/api/login")
//@Slf4j
//public class LoginRestController {
//
////    @Autowired
////    private UserService userService;
//
//    @Autowired
//    JwtUtils jwtUtils;
//
//    @Autowired
//    MyUserDetailService service;
//    @Autowired
//    AuthenticationManagerBuilder authenticationManagerBuilder;
//
////    @PostMapping
////    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
////        AppUser user = userService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
////        return ResponseEntity.ok(new UserDTO(user));
////    }
////@PostMapping("/authenticate")
//@PostMapping
//public ResponseEntity<?> createAuthToken(@RequestBody JwtRequestByName jwtRequest) {
//    Authentication authentication = logUser(jwtRequest.getUsername(), jwtRequest.getPassword());
//    String jwt = jwtUtils.generateToken(authentication);
//    HttpHeaders httpHeaders = new HttpHeaders();
//    httpHeaders.add("Authorization", "Bearer " + jwt);
//    Object principal = authentication.getPrincipal();
//    return new ResponseEntity<>(new JwtResponse(((AppUser) principal).getUsername()), httpHeaders, HttpStatus.OK);
//}
//
//    public Authentication logUser(String mail, String password) {
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(new UsernamePasswordAuthenticationToken(mail, password));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return authentication;
//    }
//}