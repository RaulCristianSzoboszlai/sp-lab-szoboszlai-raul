package com.example.demo.commands;

/**
 * Command pattern: encapsulates a request (an operation on books) as an object.
 */
public interface Command<T> {
    T execute();
}
