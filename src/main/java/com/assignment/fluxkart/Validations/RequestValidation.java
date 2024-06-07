package com.assignment.fluxkart.Validations;

import com.assignment.fluxkart.Dto.ContactRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class RequestValidation implements ConstraintValidator<ValidContactRequest, ContactRequest> {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public boolean isValid(ContactRequest value, ConstraintValidatorContext context) {
        boolean valid = true;

        if (value.getEmail() != null && !value.getEmail().isEmpty()) {
            if (!EMAIL_PATTERN.matcher(value.getEmail()).matches()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Email should be valid")
                        .addPropertyNode("email")
                        .addConstraintViolation();
                valid = false;
            }
        }

        if (value.getPhoneNumber() != null) {
            String phoneNumberStr = value.getPhoneNumber().toString();
            if (phoneNumberStr.length() != 6) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Phone number should be 6 digits")
                        .addPropertyNode("phoneNumber")
                        .addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }
}
