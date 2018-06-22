package com.svlugovoy.books.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svlugovoy.books.domain.Book;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookRestClient {

    private final RestTemplate restTemplate;

    private final String baseUrl;

    private final ObjectMapper objectMapper;

    public BookRestClient(String baseUrl) {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomErrorHandler());
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
    }

    public List<Book> findBooks() {
        return restTemplate.getForObject(baseUrl, List.class);
    }

    public List<Book> findBooks1() {
        ResponseEntity<List<Book>> responseEntity = restTemplate.exchange(
                baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {
                }
        );

        return responseEntity.getBody();
    }


    public List<Book> findBooks2() {

        List<Map<?, ?>> data = restTemplate.getForObject(baseUrl, List.class);

        List<Book> books = data.stream()
                .map(item -> objectMapper.convertValue(item, Book.class))
                .collect(Collectors.toList());

        return books;
    }

    public Book findBookById(int bookId) {
        return restTemplate.getForEntity(baseUrl + '/' + bookId, Book.class).getBody();
    }

    public URI saveBook1(Book book) {
        return restTemplate.postForLocation(baseUrl, book);
    }

    public void saveBook2(Book book) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

        HttpEntity<Book> request = new HttpEntity<>(book, map);

        restTemplate.postForLocation(baseUrl, request);
    }


    public static void main(String[] args) {
        BookRestClient client = new BookRestClient(
                "http://localhost:8081/api/books");
        Book book = new Book();
        book.setAuthor("XXX");
        book.setName("QWERTY");
        book.setYear(2010);

        System.out.println(client.findBooks1());
        client.saveBook1(book);
        client.saveBook2(book);
        System.out.println(client.findBookById(1));
        System.out.println(client.findBooks2());
        System.out.println(client.findBooks());

    }

}
