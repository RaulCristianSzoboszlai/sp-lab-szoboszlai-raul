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

    public void print() {
        System.out.println("Book: " + super.getTitle() + "\n");
        System.out.println("Authors:");
        for (Author author : authors) {
            author.print();
        }

        for (Element e : super.children) {
            e.print();
        }
    }

}
