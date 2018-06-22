package com.svlugovoy.books.repository;

import com.svlugovoy.books.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
