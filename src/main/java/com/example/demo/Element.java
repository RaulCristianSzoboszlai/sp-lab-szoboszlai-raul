package com.example.demo;

public interface Element {
    void accept(Visitor visitor);
    void add(Element e);
    void remove(Element e);
    Element get(int index);
}
