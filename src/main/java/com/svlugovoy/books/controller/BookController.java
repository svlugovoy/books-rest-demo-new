package com.svlugovoy.books.controller;

import com.svlugovoy.books.domain.Book;
import com.svlugovoy.books.repository.BookRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> findBooks() {
        return bookRepository.findAll();
    }

//    @GetMapping(path = "/{id}",
//            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public Book findBookById(@PathVariable String id) {
//        return bookRepository.findById(Long.valueOf(id)).get();
//    }

    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Book> findBookById(@PathVariable String id) {

        int bookId = NumberUtils.toInt(id);
        if(bookId <= 0) {
            return ResponseEntity.badRequest().build(); //400
        }

        Optional<Book> optional = bookRepository.findById((long) bookId);
        if (!optional.isPresent()) {
            return ResponseEntity.notFound().build(); //404
        }

        return ResponseEntity.ok(optional.get()); //200
    }


    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void createBook(@Valid @RequestBody Book book) {
        bookRepository.save(book);
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void updateBook(@PathVariable String id, @Valid @RequestBody Book book) {
        bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id) {
        bookRepository.deleteById(Long.valueOf(id));
    }
}
