package com.example.demo.commands;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.persistence.CrudRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared context of the commands; stores the repositories used to access the data layer.
 */
public class CommandContext {

    private final CrudRepository<Book, Integer> booksRepository;
    private final CrudRepository<Author, Integer> authorsRepository;

    public CommandContext(CrudRepository<Book, Integer> booksRepository,
                          CrudRepository<Author, Integer> authorsRepository) {
        this.booksRepository = booksRepository;
        this.authorsRepository = authorsRepository;
    }

    public CrudRepository<Book, Integer> getBooksRepository() {
        return booksRepository;
    }

    public CrudRepository<Author, Integer> getAuthorsRepository() {
        return authorsRepository;
    }

    /**
     * Authors received with an id are linked to the already persisted authors;
     * authors without an id are new and get persisted together with the book (cascade).
     */
    List<Author> resolveAuthors(List<Author> authors) {
        List<Author> resolved = new ArrayList<>();
        if (authors == null) {
            return resolved;
        }
        for (Author author : authors) {
            if (author.getId() == null) {
                resolved.add(author);
            } else {
                resolved.add(authorsRepository.findById(author.getId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Author with id " + author.getId() + " does not exist")));
            }
        }
        return resolved;
    }
}
