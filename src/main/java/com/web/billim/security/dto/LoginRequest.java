package com.web.billim.security.dto;

//import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel(description = "Login Request")
public class LoginRequest {
    private String email;
    private String password;
}
