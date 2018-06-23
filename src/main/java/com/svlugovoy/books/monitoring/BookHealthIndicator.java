package com.svlugovoy.books.monitoring;

import com.svlugovoy.books.repository.BookRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class BookHealthIndicator implements HealthIndicator {

	private final BookRepository bookRepository;

	public BookHealthIndicator(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public Health health() {
		if (bookRepository.count() > 0) {
			return Health.up().build();
		}
		return Health.down().withDetail("Library failure", "No books present").build();
	}

}
