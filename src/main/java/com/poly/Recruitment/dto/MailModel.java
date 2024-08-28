package com.poly.Recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailModel {
    private String from = "beetravel265@gmail.com";
    private String to;
    private String subject;
    private String content;
}