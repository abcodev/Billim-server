package com.web.billim;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/ping/ping")
    public String ping() {
        return "pong";
    }
}
