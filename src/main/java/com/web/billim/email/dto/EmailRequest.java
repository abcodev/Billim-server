package com.web.billim.email.dto;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@ApiModel(value = "이메일 전송")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailRequest {
    private  String email;
}
