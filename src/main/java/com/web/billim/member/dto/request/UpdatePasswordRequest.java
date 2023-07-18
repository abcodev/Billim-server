package com.web.billim.member.dto.request;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@ApiModel(value = "비밀번호 변경 요청")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatePasswordRequest {
    private String password;
    private String newPassword;
}
