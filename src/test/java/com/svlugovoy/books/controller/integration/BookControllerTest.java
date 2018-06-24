package com.svlugovoy.books.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svlugovoy.books.BooksRestApplication;
import com.svlugovoy.books.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(BooksRestApplication.class)
@AutoConfigureMockMvc
public class BookControllerTest {
	@Autowired
    private MockMvc mockMvc;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void saveBook_nameNull_badRequestReturned() throws Exception {
    	//Given
    	Book book = new Book();
    	//When
    	ResultActions actions = mockMvc.perform(post("/api/books")
    			.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
    			.content(MAPPER.writeValueAsString(book)));
    	//Then
    	actions.andExpect(status().isBadRequest());
    			
    }

}

