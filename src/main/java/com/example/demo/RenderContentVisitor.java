package com.example.demo;

public class RenderContentVisitor implements Visitor {
    @Override
    public void visit(Book book) {
        System.out.println("Book: " + book.getTitle());
        System.out.println("Authors:");
        for (Author author : book.getAuthors()) {
            author.print();
        }
    }

    @Override
    public void visit(Section section) {
        System.out.println(section.getTitle());
    }

    @Override
    public void visit(TableOfContents tableOfContents) {
        System.out.println("Table of Contents");
    }

    @Override
    public void visit(Paragraph paragraph) {
        System.out.println("Paragraph: " + paragraph.getText());
    }

    @Override
    public void visit(ImageProxy imageProxy) {
        imageProxy.loadImage().accept(this);
    }

    @Override
    public void visit(Image image) {
        System.out.println("Image with name: " + image.getUrl());
    }

    @Override
    public void visit(Table table) {
        System.out.println("Table with title: " + table.getTitle());
    }
}