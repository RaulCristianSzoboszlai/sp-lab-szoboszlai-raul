package com.example.demo.persistence;

import com.example.demo.models.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter: adapts the Spring Data JPA {@link BooksRepository} to our {@link CrudRepository} interface.
 */
@Component
public class BooksRepositoryAdapter implements CrudRepository<Book, Integer> {

    private final BooksRepository booksRepository;

    public BooksRepositoryAdapter(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Integer id) {
        return booksRepository.findById(id);
    }

    @Override
    public Book save(Book book) {
        return booksRepository.save(book);
    }

    @Override
    public boolean existsById(Integer id) {
        return booksRepository.existsById(id);
    }

    @Override
    public void deleteById(Integer id) {
        booksRepository.deleteById(id);
    }
}
