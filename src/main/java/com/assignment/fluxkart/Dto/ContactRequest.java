package com.assignment.fluxkart.Dto;

import com.assignment.fluxkart.Validations.ValidContactRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@ValidContactRequest
public class ContactRequest {
    private String email;
    private Integer phoneNumber;
}
