package com.example.demo.persistence;

import com.example.demo.models.Author;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter: adapts the Spring Data JPA {@link AuthorsRepository} to our {@link CrudRepository} interface.
 */
@Component
public class AuthorsRepositoryAdapter implements CrudRepository<Author, Integer> {

    private final AuthorsRepository authorsRepository;

    public AuthorsRepositoryAdapter(AuthorsRepository authorsRepository) {
        this.authorsRepository = authorsRepository;
    }

    @Override
    public List<Author> findAll() {
        return authorsRepository.findAll();
    }

    @Override
    public Optional<Author> findById(Integer id) {
        return authorsRepository.findById(id);
    }

    @Override
    public Author save(Author author) {
        return authorsRepository.save(author);
    }

    @Override
    public boolean existsById(Integer id) {
        return authorsRepository.existsById(id);
    }

    @Override
    public void deleteById(Integer id) {
        authorsRepository.deleteById(id);
    }
}
