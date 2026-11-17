package com.example.demo.observer;

import com.example.demo.models.Book;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * Observer pattern - ConcreteObserver participant: pushes every new book, as a JSON
 * server-sent event, to the client "listening" on the wrapped {@link SseEmitter}.
 */
public class SseObserver implements Observer {

    private final SseEmitter emitter;

    public SseObserver(SseEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void update(Book book) {
        try {
            emitter.send(book, MediaType.APPLICATION_JSON);
        } catch (IOException | IllegalStateException e) {
            // the client went away (or the emitter is already completed); end the stream,
            // the emitter's completion/error callbacks detach this observer from the subject
            emitter.completeWithError(e);
        }
    }
}
