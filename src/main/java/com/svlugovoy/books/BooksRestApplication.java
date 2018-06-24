package com.svlugovoy.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BooksRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksRestApplication.class, args);
    }
}
