package com.web.billim.security.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Login Request")
public class LoginRequest {
    @ApiModelProperty(value = "Email", required = true)
    private String email;
    @ApiModelProperty(value = "Password", required = true)
    private String password;


}
