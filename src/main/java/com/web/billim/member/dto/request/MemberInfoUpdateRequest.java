package com.web.billim.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberInfoUpdateRequest {

    private String nickname;
    private String address;
    private MultipartFile profileImage;

}
