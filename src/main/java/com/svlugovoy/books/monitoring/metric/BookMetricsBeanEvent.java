package com.svlugovoy.books.monitoring.metric;

import com.svlugovoy.books.event.BookSavedEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BookMetricsBeanEvent {

    private Counter savedBooksCounter;

    public BookMetricsBeanEvent(MeterRegistry meterRegistry) {
        savedBooksCounter = meterRegistry.counter
                ("books.saved.event");
    }

    @EventListener
    @Async
    public void onSaveBook(BookSavedEvent event) {
        savedBooksCounter.increment();
    }

}
