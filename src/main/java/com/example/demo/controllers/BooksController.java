package com.example.demo.controllers;

import com.example.demo.commands.*;
import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.persistence.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final CommandContext commandContext;

    @Autowired
    public BooksController(CrudRepository<Book, Integer> booksRepository,
                           CrudRepository<Author, Integer> authorsRepository) {
        this.commandContext = new CommandContext(booksRepository, authorsRepository);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(new GetAllBooksCommand(commandContext).execute());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        return new GetBookByIdCommand(commandContext, id).execute()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = new CreateBookCommand(commandContext, book).execute();
        return ResponseEntity.created(URI.create("/books/" + savedBook.getId())).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Integer id, @RequestBody Book book) {
        return new UpdateBookCommand(commandContext, id, book).execute()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        boolean deleted = new DeleteBookCommand(commandContext, id).execute();
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }
}
