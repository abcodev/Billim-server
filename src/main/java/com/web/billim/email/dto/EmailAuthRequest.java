package com.web.billim.email.dto;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
//@ApiModel(value = "이메일 인증 코드")
public class EmailAuthRequest {
    private String email;
    private String authToken;
}
