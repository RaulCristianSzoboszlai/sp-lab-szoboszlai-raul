package com.example.demo.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Section extends BaseElement {
    private String title;

    @OneToMany(targetEntity = BaseElement.class, cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<BaseElement> children = new ArrayList<>();

    public Section() {
    }

    public Section(String title) {
        this.title = title;
    }

    @Override
    public void add(Element e) {
        if (!(e instanceof BaseElement)) {
            throw new IllegalArgumentException("Only persistable elements (BaseElement) can be added");
        }
        children.add((BaseElement) e);
    }

    @Override
    public void remove(Element e) {
        children.remove(e);
    }

    @Override
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

    public List<BaseElement> getChildren() {
        return children;
    }

    public void setChildren(List<BaseElement> children) {
        // keep the same collection instance so Hibernate can track orphan removal
        this.children.clear();
        if (children != null) {
            this.children.addAll(children);
        }
    }
}
