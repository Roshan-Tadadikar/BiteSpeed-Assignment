package com.assignment.fluxkart.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ContactRequest {
    private String email;
    private Integer phoneNumber;
}
