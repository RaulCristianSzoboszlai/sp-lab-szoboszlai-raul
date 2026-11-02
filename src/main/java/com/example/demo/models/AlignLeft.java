package com.example.demo.models;

public class AlignLeft implements AlignStrategy {
    @Override
    public void render(Paragraph p, Context context) {
        System.out.println(p.getText());
    }
}
