package com.assignment.fluxkart.Controller;

import com.assignment.fluxkart.Dto.ContactRequest;
import com.assignment.fluxkart.Dto.ContactResponse;
import com.assignment.fluxkart.Service.ServiceImpl.ContactServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/identify")
@Slf4j
public class AddToCardController {

    @Autowired
    ContactServiceImpl contactService;

    @PostMapping
    public ResponseEntity addToCart(@RequestBody ContactRequest request){
        log.info("*** Inside addToCardController ***");
        ContactResponse response = contactService.addToCard(request);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
