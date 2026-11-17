package com.example.demo.observer;

import com.example.demo.models.Book;

/**
 * Observer pattern - Observer participant: gets notified by a {@link Subject} when its state changes.
 */
public interface Observer {
    void update(Book book);
}
