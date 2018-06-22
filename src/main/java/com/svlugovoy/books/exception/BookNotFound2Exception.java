package com.svlugovoy.books.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFound2Exception extends RuntimeException {
	
	public BookNotFound2Exception() {
		super("Custom message");
	}

	private static final long serialVersionUID = 3591666710943050205L;

}
