package com.example.demo.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Section extends BaseElement {
    private String title;

    @OneToMany(cascade = CascadeType.ALL)
    protected List<BaseElement> children = new ArrayList<>();

    public Section() {
    }

    public Section(String title) {
        this.title = title;
    }

    public void add(Element e) {
        if (e instanceof BaseElement) {
            children.add((BaseElement) e);
        }
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

        for (BaseElement child : children) {
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
