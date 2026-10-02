package com.example.demo;

public class Image implements Element {
    private String url;

    public Image(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        System.out.println("Image with name: " + this.url);
    }

}
