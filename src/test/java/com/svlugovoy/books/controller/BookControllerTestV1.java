package com.svlugovoy.books.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svlugovoy.books.BooksRestApplication;
import com.svlugovoy.books.domain.Book;
import com.svlugovoy.books.repository.BookRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(BooksRestApplication.class)
@AutoConfigureMockMvc
public class BookControllerTestV1 {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeEach
    public void setup() {
        Book book1 = new Book();
        book1.setAuthor("Smith");
        book1.setName("Good Book");
        book1.setYear(2015);
        Book book2 = new Book();
        book2.setAuthor("Bloch");
        book2.setName("Good Java");
        book2.setYear(2010);
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));
        when(bookRepository.findById(3L)).thenReturn(Optional.empty());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
    }

    @Test
    public void findBooks_StorageNonEmpty_TwoBooksReturned_Json() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/books"));
        assertAll(
                () -> result.andExpect(status().isOk()),
                () -> result.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)),
                () -> result.andExpect(jsonPath("$", Matchers.hasSize(2))),
                () -> result.andExpect(jsonPath("$.[0].name", Matchers.equalTo("Good Book"))),
                () -> result.andExpect(jsonPath("$.[1].name", Matchers.equalTo("Good Java")))
        );
    }

    @Test
    public void findBookById_OneBookReturned_Xml() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/books/1").accept(MediaType.APPLICATION_XML));
        assertAll(
                () -> result.andExpect(status().isOk()),
                () -> result.andExpect(content().contentType("application/xml;charset=UTF-8")),
                () -> result.andExpect(content().xml("<Book><id/><author>Smith</author><name>Good Book</name><published>2015</published></Book>"))
        );
    }

    @Test
    public void findBookById_InvalidId_NotFoundReturned() throws Exception {
        mockMvc.perform(get("/api/books/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findBookById_InvalidId_BadRequestReturned() throws Exception {
        mockMvc.perform(get("/api/books/4asd"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findBookById_ValidId_BookIsReturned() throws Exception {
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.equalTo("Good Book")));
    }

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
