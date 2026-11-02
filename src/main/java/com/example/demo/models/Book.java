package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class Book extends Section {
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Author> authors = new ArrayList<>();

    public Book() {
        super();
    }

    public Book(String title) {
        super(title);
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void addContent(Element e) {
        super.add(e);
    }

    public List<Author> getAuthors() {
        return this.authors;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);

        for (Element child : super.children) {
            child.accept(visitor);
        }
    }
}
