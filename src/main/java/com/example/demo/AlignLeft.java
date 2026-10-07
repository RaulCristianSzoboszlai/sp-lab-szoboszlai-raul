package com.example.demo;

public class AlignLeft implements AlignStrategy {
    @Override
    public void render(Paragraph p, Context context) {
        System.out.println(p.getText());
    }
}
