package com.web.billim.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Schema(title = "비밀번호 재설정 요청")
@ApiModel(value = "비밀번호 재설정 요청")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatePasswordRequest {
    private String password;
    private String newPassword;
}
