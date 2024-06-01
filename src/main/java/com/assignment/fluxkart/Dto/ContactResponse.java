package com.assignment.fluxkart.Dto;

import lombok.Data;

import java.util.Set;

@Data
public class ContactResponse {
    private Integer primaryId;
    private Set<String> emails;
    private Set<Integer> phoneNumbers;
    private Set<Integer> secondaryContactIds;
}
