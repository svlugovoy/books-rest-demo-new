package com.svlugovoy.books.repository;

import com.svlugovoy.books.domain.Book;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

}
