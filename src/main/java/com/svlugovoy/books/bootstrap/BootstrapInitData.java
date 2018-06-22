package com.svlugovoy.books.bootstrap;

import com.svlugovoy.books.domain.Book;
import com.svlugovoy.books.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapInitData implements CommandLineRunner {

    private final BookRepository bookRepository;

    public BootstrapInitData(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Book b1 = new Book(1L, "Joshua Bloch", "Effective Java (3rd Edition)", 2017);
        Book b2 = new Book(2L, "Kathy Sierra", "Head First Java, 2nd Edition", 2005);
        Book b3 = new Book(3L, "Brian Goetz", "Java Concurrency in Practice", 2006);

        bookRepository.save(b1);
        bookRepository.save(b2);
        bookRepository.save(b3);

        System.out.println("Data Loaded = " + bookRepository.count() );

    }
}