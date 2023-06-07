package com.web.billim.member.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "아이디 찾기 요청")
public class FindIdRequest {
    private String name;
    private String email;
}
