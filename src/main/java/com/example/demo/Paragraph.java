package com.example.demo;

public class Paragraph implements Element {
    private String text;

    public Paragraph(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void add(Element e) {
        throw new UnsupportedOperationException();
    }

    public void remove(Element e) {
        throw new UnsupportedOperationException();
    }

    public Element get(int index) {
        throw new UnsupportedOperationException();
    }

    public void print() {
        System.out.println("Paragraph: " + this.text);
    }
}
