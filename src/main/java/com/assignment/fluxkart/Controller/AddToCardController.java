package com.assignment.fluxkart.Controller;

import com.assignment.fluxkart.Dto.ContactRequest;
import com.assignment.fluxkart.Dto.ContactResponse;
import com.assignment.fluxkart.Service.ServiceImpl.ContactServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class AddToCardController {

    @Autowired
    ContactServiceImpl contactService;

    @GetMapping("/**")
    public ResponseEntity welComeController(){
        log.info("*** Inside welComeController ***");
        Map<String, String> map = new HashMap<>();
        final String url = "https://bitespeed-assignment-production.up.railway.app/identify";
        final String jsonBodyExample= "{'email':'emailofYourChoice@something.com', phoneNumber:'6 Digit Number'";
        final String message = "Hello there, since only get method is allowed kindly do postMapping on url below with json";

        map.put("Message",message);
        map.put("url", url);
        map.put("JsonExample",jsonBodyExample);
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @PostMapping("/identify")
    public ResponseEntity addToCart(@Valid @RequestBody ContactRequest request){
        log.info("*** Inside addToCardController ***");
        ContactResponse response = contactService.addToCard(request);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
