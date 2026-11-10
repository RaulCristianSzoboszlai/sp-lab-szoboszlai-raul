package com.example.demo.models;

import jakarta.persistence.Entity;

@Entity
// "Table" is a reserved SQL keyword in H2, so map this entity to a different table name
@jakarta.persistence.Table(name = "TableElement")
public class Table extends BaseElement {
    private String title;

    public Table() {
    }

    public Table(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void add(Element e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Element e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element get(int index) {
        throw new UnsupportedOperationException();
    }
}