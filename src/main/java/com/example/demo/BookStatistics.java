package com.example.demo;

public class BookStatistics implements Visitor {
    private int images = 0;
    private int tables = 0;
    private int paragraphs = 0;
    private int sections = 0;

    @Override
    public void visit(Book book) {}

    @Override
    public void visit(Section section) {
        sections++;
    }

    @Override
    public void visit(TableOfContents tableOfContents) {}

    @Override
    public void visit(Paragraph paragraph) {
        paragraphs++;
    }

    @Override
    public void visit(ImageProxy imageProxy) {
        images++;
    }

    @Override
    public void visit(Image image) {
        images++;
    }

    @Override
    public void visit(Table table) {
        tables++;
    }

    public void printStatistics() {
        System.out.println("Book Statistics:");
        System.out.println("*** Number of sections: " + sections);
        System.out.println("*** Number of images: " + images);
        System.out.println("*** Number of tables: " + tables);
        System.out.println("*** Number of paragraphs: " + paragraphs);
    }
}
