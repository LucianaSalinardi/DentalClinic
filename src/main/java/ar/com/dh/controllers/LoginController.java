//package ar.com.dh.controllers;
//
//import ar.com.dh.dtos.requests.UserReqDto;
//import ar.com.dh.dtos.responses.UserResponseDto;
//import ar.com.dh.exceptions.ApiException;
//import ar.com.dh.services.LoginService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RequestMapping(path = "/clinical-dental")
//@RestController
//public class LoginController {
//
//    private final LoginService loginService;
//
//    public LoginController(LoginService loginService) {
//        this.loginService = loginService;
//    }
//    @PostMapping("/sign-in/user")
//    public ResponseEntity<UserResponseDto> login(@RequestBody UserReqDto userDto) throws ApiException {
//        return ResponseEntity.ok(loginService.login(userDto.getEmail(), userDto.getPassword()));
//    }
//
////    @PostMapping("/sign-in/admin")
////    public UserResponseDto loginAdmin(@RequestBody UserReqDto adminDto) throws ApiException {
////        return service.login(username, password);
////    }
//
//}
