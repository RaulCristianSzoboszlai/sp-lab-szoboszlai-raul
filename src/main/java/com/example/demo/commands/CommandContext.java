package com.example.demo.commands;

import com.example.demo.persistence.AuthorsRepository;
import com.example.demo.persistence.BooksRepository;
import org.springframework.stereotype.Component;

@Component
public class CommandContext {

    private final BooksRepository booksRepository;
    private final AuthorsRepository authorsRepository;

    public CommandContext(BooksRepository booksRepository, AuthorsRepository authorsRepository) {
        this.booksRepository = booksRepository;
        this.authorsRepository = authorsRepository;
    }

    public BooksRepository getBooksRepository() {
        return booksRepository;
    }

    public AuthorsRepository getAuthorsRepository() {
        return authorsRepository;
    }
}