package com.web.billim.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(
           @RequestParam("email") String email){

        return new ResponseEntity<>(email, HttpStatus.OK);
    }
}
