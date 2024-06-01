package com.assignment.fluxkart;

import com.assignment.fluxkart.Repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FluxkartApplicationTests {

	@Autowired
	ContactRepository contactRepository;

	@Test
	void contextLoads() {
//		contactRepository.findLinkedIdByPhoneNumber(123456);
	}

}
