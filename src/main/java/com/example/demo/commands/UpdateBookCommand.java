package com.example.demo.commands;

import com.example.demo.models.Book;

import java.util.Optional;

public class UpdateBookCommand implements Command<Optional<Book>> {

    private final CommandContext context;
    private final Integer id;
    private final Book bookData;

    public UpdateBookCommand(CommandContext context, Integer id, Book bookData) {
        this.context = context;
        this.id = id;
        this.bookData = bookData;
    }

    @Override
    public Optional<Book> execute() {
        return context.getBooksRepository().findById(id).map(book -> {
            book.setTitle(bookData.getTitle());
            book.setAuthors(context.resolveAuthors(bookData.getAuthors()));
            book.setChildren(bookData.getChildren());
            return context.getBooksRepository().save(book);
        });
    }
}
