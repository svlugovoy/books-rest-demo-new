package com.svlugovoy.books.event;

import com.svlugovoy.books.domain.Book;
import org.springframework.context.ApplicationEvent;

public class BookSavedEvent extends ApplicationEvent {
	
	private final Book book;

	public BookSavedEvent(Object source, Book book) {
		super(source);
		this.book = book;
	}
	
	public Book getBook() {
		return book;
	}

	private static final long serialVersionUID = 2461738659856693744L;

}
