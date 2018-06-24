package com.svlugovoy.books.controller.system;

import com.svlugovoy.books.BooksRestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(BooksRestApplication.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
    @Test
    public void findBooks_bookRepositoryEmpty_emptyListReturned() throws Exception {
        //Given
        //When
        List<?> books = restTemplate.getForObject("/api/books",
        		List.class);
        //Then
        assertTrue(books.size() == 3);
    }

}

