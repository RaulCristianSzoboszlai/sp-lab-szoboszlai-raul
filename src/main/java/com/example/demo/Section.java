package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Section implements Element {
    private String title;
    protected List<Element> children = new ArrayList<>();

    public Section(String title) {
        this.title = title;
    }

    public void add(Element e) {
        children.add(e);
    }

    public void remove(Element e) {
        children.remove(e);
    }

    public Element get(int index) {
        return children.get(index);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);

        for (Element child : children) {
            child.accept(visitor);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
