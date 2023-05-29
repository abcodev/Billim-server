package com.web.billim.member.controller;

import com.web.billim.security.domain.LoginReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

//    @PostMapping("/login")
//    @ResponseBody
//    public ResponseEntity<HttpServletResponse> login(
//            @RequestBody LoginReq loginReq
//            HttpServletResponse httpResponse){
//
//        return ResponseEntity.ok(httpResponse);
//
//    }

    @GetMapping("/test")
    public ResponseEntity<String> test1(){
        String test = "check";
        return ResponseEntity.ok(test);
    }
}
