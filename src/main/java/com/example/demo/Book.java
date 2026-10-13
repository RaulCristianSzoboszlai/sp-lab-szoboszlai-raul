package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Book extends Section {
    private List<Author> authors = new ArrayList<>();

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
