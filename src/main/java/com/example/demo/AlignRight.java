package com.example.demo;

public class AlignRight implements AlignStrategy {
    @Override
    public void render(Paragraph p, Context context) {
        String text = p.getText();
        int padding = context.getWidth() - text.length();

        if (padding > 0) {
            System.out.println(" ".repeat(padding) + text);
        } else {
            System.out.println(text);
        }
    }
}
