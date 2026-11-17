package com.example.demo.observer;

import com.example.demo.models.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Observer pattern - ConcreteSubject participant: its state is the collection of books;
 * every time a new book is added, all the attached observers are notified.
 * <p>
 * Singleton Spring bean, shared by the controller that changes its state (BooksController)
 * and by the controller that attaches observers to it (BooksSseController).
 */
@Component
public class AllBooksSubject implements Subject {

    // thread-safe: observers are attached / detached from different HTTP request threads
    private final List<Observer> observers = new CopyOnWriteArrayList<>();
    private final List<Book> books = new CopyOnWriteArrayList<>();

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Book book) {
        for (Observer observer : observers) {
            observer.update(book);
        }
    }

    /**
     * Changes the state of the subject and notifies all the observers about the newly added book.
     */
    public void add(Book book) {
        books.add(book);
        notifyObservers(book);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public int getObserversCount() {
        return observers.size();
    }
}
