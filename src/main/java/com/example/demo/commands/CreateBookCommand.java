package com.example.demo.commands;

import com.example.demo.models.Book;

public class CreateBookCommand implements Command<Book> {

    private final CommandContext context;
    private final Book bookData;

    public CreateBookCommand(CommandContext context, Book bookData) {
        this.context = context;
        this.bookData = bookData;
    }

    @Override
    public Book execute() {
        Book book = new Book(bookData.getTitle());
        book.setAuthors(context.resolveAuthors(bookData.getAuthors()));
        book.setChildren(bookData.getChildren());
        return context.getBooksRepository().save(book);
    }
}
