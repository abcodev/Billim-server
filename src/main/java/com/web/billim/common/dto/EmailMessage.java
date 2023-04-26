package com.web.billim.common.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailMessage {

    private String to;
    private String subject;
    private String message;
}
