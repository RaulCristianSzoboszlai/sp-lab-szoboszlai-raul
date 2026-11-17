package com.example.demo.observer;

import com.example.demo.models.Book;

/**
 * Observer pattern - Subject participant: keeps a collection of observers
 * and notifies them whenever its state changes.
 */
public interface Subject {
    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers(Book book);
}
