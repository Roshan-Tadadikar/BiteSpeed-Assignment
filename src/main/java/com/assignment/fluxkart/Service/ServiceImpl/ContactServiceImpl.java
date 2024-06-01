package com.assignment.fluxkart.Service.ServiceImpl;

import com.assignment.fluxkart.Dto.ContactRequest;
import com.assignment.fluxkart.Dto.ContactResponse;
import com.assignment.fluxkart.Exceptions.ResourceNotFoundException;
import com.assignment.fluxkart.Models.Contact;
import com.assignment.fluxkart.Repository.ContactRepository;
import com.assignment.fluxkart.Repository.NewContactRepo;
import com.assignment.fluxkart.Service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    NewContactRepo newContactRepo;

    @Override
    public ContactResponse addToCard(ContactRequest request) {
        if (request == null || ((request.getEmail() == null || request.getEmail().isEmpty()) && request.getPhoneNumber() == null)) {
            throw new ResourceNotFoundException("Message", "Please provide at least email or phone number");
        }

        boolean EmailPresent = request.getEmail() != null && !request.getEmail().isEmpty();
        boolean PhoneNoPresent = request.getPhoneNumber() != null;

        if((EmailPresent && !PhoneNoPresent) || (PhoneNoPresent && !EmailPresent)){
           return handleEmailOrPhNoPresent(request, EmailPresent ? true : false);
        }

        return handleBothPresent(request);
    }

    public ContactResponse handleBothPresent(ContactRequest request){
        boolean emailExists = contactRepository.countByEmail(request.getEmail()) > 0;
        boolean phoneNoExists = contactRepository.countByPhoneNumber(request.getPhoneNumber()) > 0;
        if(!emailExists && !phoneNoExists){
            return getSingleContact(addNewContact(request,null));
        }
        Integer primaryId = null;

        if(emailExists && !phoneNoExists){
            primaryId = newContactRepo.findPrimaryIdByEmailIfExistOrMinId(request.getEmail());
        }else if(phoneNoExists && !emailExists){
            primaryId = newContactRepo.findPrimaryIdByPhNumberIfExistOrMinId(request.getPhoneNumber());
        }

        if((emailExists && !phoneNoExists) || (phoneNoExists && !emailExists)){
            addNewContact(request,primaryId);
            return getAllContactsUsingLinkedId(primaryId);
        }

        Integer primaryIdOfEmail = newContactRepo.findPrimaryIdByEmailIfExistOrMinId(request.getEmail());
        Integer primaryIdOfPhNo = newContactRepo.findPrimaryIdByPhNumberIfExistOrMinId(request.getPhoneNumber());

        if(primaryIdOfEmail.equals(primaryIdOfPhNo)){
            return getAllContactsUsingLinkedId(primaryIdOfPhNo);
        }

        Optional<Contact> foundContact = contactRepository.findByEmailAndPhoneNumber(request.getEmail(), request.getPhoneNumber());

        if(foundContact.isPresent()){
            return getSingleContact(foundContact.get());
        }
        
        Integer newPrimaryId = Math.min(primaryIdOfPhNo, primaryIdOfEmail);
        
        if(newPrimaryId != primaryIdOfPhNo){
            contactRepository.updateContactDetailsByLinkedId(newPrimaryId, primaryIdOfPhNo);
            contactRepository.updateContactDetailsById(newPrimaryId, primaryIdOfPhNo);
        }else{
            contactRepository.updateContactDetailsByLinkedId(newPrimaryId, primaryIdOfEmail);
            contactRepository.updateContactDetailsById(newPrimaryId, primaryIdOfEmail);
        }
        return getAllContactsUsingLinkedId(newPrimaryId);
    }

    public ContactResponse handleEmailOrPhNoPresent(ContactRequest request, boolean EmailPresent){
        boolean checkIfExists = false;
        if(EmailPresent){
            checkIfExists = contactRepository.countByEmail(request.getEmail()) > 0;
        }else{
            checkIfExists =  contactRepository.countByPhoneNumber(request.getPhoneNumber()) >0;
        }

        if(!checkIfExists){
            return getSingleContact(addNewContact(request,null));
        }

        Integer linkedIdOrMinId = null;
        if(EmailPresent){
            linkedIdOrMinId = newContactRepo.findPrimaryIdByEmailIfExistOrMinId(request.getEmail());
        }else linkedIdOrMinId = newContactRepo.findPrimaryIdByPhNumberIfExistOrMinId(request.getPhoneNumber());

       return getAllContactsUsingLinkedId(linkedIdOrMinId);
    }

    public Contact addNewContact(ContactRequest request,Integer primaryId){
        Contact newContact = new Contact();
        boolean EmailPresent = request.getEmail() != null && !request.getEmail().isEmpty();
        boolean PhoneNoPresent = request.getPhoneNumber() != null;

        if(EmailPresent){
            newContact.setEmail(request.getEmail());
        }
        if(PhoneNoPresent){
            newContact.setPhoneNumber(request.getPhoneNumber());
        }
        newContact.setCreatedAt(LocalDateTime.now());
        newContact.setLinkedPrecedence(primaryId==null ? "primary":"secondary");
        if(primaryId!=null){
            newContact.setLinkedId(primaryId);
        }
        Contact updatedContact = contactRepository.save(newContact);
        return  updatedContact;
    }

    public ContactResponse getSingleContact(Contact updatedContact){
        ContactResponse response = new ContactResponse();
        response.setPrimaryId(updatedContact.getId());
        response.setEmails(new HashSet<>());
        response.setPhoneNumbers(new HashSet<>());
        if(updatedContact.getEmail()!=null){
           response.getEmails().add(updatedContact.getEmail());
        }
        if(updatedContact.getPhoneNumber()!=null){
            response.getPhoneNumbers().add(updatedContact.getPhoneNumber());
        }
        response.setSecondaryContactIds(new HashSet<>());
        return response;
    }

    public ContactResponse getAllContactsUsingLinkedId(Integer primaryId) {
        ContactResponse response = new ContactResponse();
        response.setPrimaryId(primaryId);
        Set<String> emailIds = contactRepository.findDistinctEmailsByLinkedIdOrId(primaryId);
        Set<Integer> phoneNumbers = contactRepository.findDistinctPhoneNumbersByLinkedIdOrId(primaryId);
        Set<Integer> secondaryIds = contactRepository.findIdsByLinkedId(primaryId);
        Set<String> emailIdsWithoutNull = new HashSet<>();
        Set<Integer> phoneNumberWithoutNull = new HashSet<>();
        for (Integer number : phoneNumbers)
            if (number != null)
                phoneNumberWithoutNull.add(number);
        for (String email : emailIds)
            if (email != null)
                emailIdsWithoutNull.add(email);
        response.setEmails(emailIdsWithoutNull);
        response.setPhoneNumbers(phoneNumberWithoutNull);
        response.setSecondaryContactIds(secondaryIds);
        return response;
    }
}
