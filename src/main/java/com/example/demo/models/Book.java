package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

@Entity
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE) // a Book is a root aggregate; no "type" discriminator needed in JSON
public class Book extends Section {
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

    public void setAuthors(List<Author> authors) {
        this.authors.clear();
        if (authors != null) {
            this.authors.addAll(authors);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);

        for (Element child : super.children) {
            child.accept(visitor);
        }
    }
}
