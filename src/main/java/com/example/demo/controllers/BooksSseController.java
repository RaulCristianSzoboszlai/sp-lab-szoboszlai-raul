package com.example.demo.controllers;

import com.example.demo.observer.AllBooksSubject;
import com.example.demo.observer.SseObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Opens a server-sent events stream; every client gets its own {@link SseObserver}
 * attached to the {@link AllBooksSubject}, so it is pushed every newly created book.
 */
@RestController
public class BooksSseController {

    private final AllBooksSubject allBooksSubject;
    // open streams, closed on shutdown so the server does not wait for never-ending SSE requests
    private final Set<SseEmitter> openEmitters = ConcurrentHashMap.newKeySet();

    @Autowired
    public BooksSseController(AllBooksSubject allBooksSubject) {
        this.allBooksSubject = allBooksSubject;
    }

    @RequestMapping("/books-sse")
    public ResponseBodyEmitter getBooksSse() {
        final SseEmitter emitter = new SseEmitter(0L);
        final SseObserver observer = new SseObserver(emitter);
        allBooksSubject.attach(observer);
        openEmitters.add(emitter);
        // stop notifying clients that disconnected
        emitter.onCompletion(() -> close(emitter, observer));
        emitter.onTimeout(() -> close(emitter, observer));
        emitter.onError(e -> close(emitter, observer));
        return emitter;
    }

    private void close(SseEmitter emitter, SseObserver observer) {
        allBooksSubject.detach(observer);
        openEmitters.remove(emitter);
    }

    @EventListener(ContextClosedEvent.class)
    public void completeOpenEmitters() {
        openEmitters.forEach(SseEmitter::complete);
        openEmitters.clear();
    }
}
