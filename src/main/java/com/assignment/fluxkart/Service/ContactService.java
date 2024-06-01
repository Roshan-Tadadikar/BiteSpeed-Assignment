package com.assignment.fluxkart.Service;

import com.assignment.fluxkart.Dto.ContactRequest;
import com.assignment.fluxkart.Dto.ContactResponse;

import java.util.List;

public interface ContactService {

   ContactResponse addToCard(ContactRequest request);

}
