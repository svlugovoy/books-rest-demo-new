package com.svlugovoy.books.monitoring.metric;

import com.svlugovoy.books.domain.Book;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class BookMetricsBean {
    private Counter savedBooksCounter;

    public BookMetricsBean(MeterRegistry meterRegistry) {
        savedBooksCounter = meterRegistry.counter
                ("books.saved");
    }

    public void onSaveBook(Book book) {
        savedBooksCounter.increment();
    }

}
