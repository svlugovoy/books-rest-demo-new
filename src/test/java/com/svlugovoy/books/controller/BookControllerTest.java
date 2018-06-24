package com.svlugovoy.books.controller;

import com.svlugovoy.books.BooksRestApplication;
import com.svlugovoy.books.domain.Book;
import com.svlugovoy.books.repository.BookRepository;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(BooksRestApplication.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<Book> jacksonTester;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void saveBook_nameNull_badRequestReturned() throws Exception {
        //Given
        Book book = new Book();
        //When
        ResultActions actions = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jacksonTester.write(book).getJson()));
        //Then
        actions.andExpect(status().isBadRequest());
    }

    @Ignore
    @Test
    public void saveBook_validBook_bookResponseReturned() throws Exception {
        //Given
        Book book = new Book();
        book.setAuthor("Some user");
        book.setName("Java");
        book.setYear(2000);
        //When
        ResultActions actions = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(book).getJson()));
        //Then
        actions.andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Java")));
    }

    @Test
    public void findBooks_bookRepositoryEmpty_emptyListReturned() throws Exception {
        //Given
        given(bookRepository.findAll()).willReturn(Collections.emptyList());
        //When
        ResultActions actions = mockMvc.perform(get("/api/books"));
        //Then
        actions.andExpect(status().isOk()).andExpect
                (content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

}

