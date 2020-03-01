package com.example.svg1.Entity;

import android.graphics.Point;

public class Text {
    private String text;
    private int x,y,color,size;

    private Point point;
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Text{" +
                "text='" + text + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", color=" + color +
                ", size=" + size +
                ", point=" + point +
                '}';
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Text(String text, int x, int y, int color, int size, Point point) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.size = size;
        this.point = point;
    }
}
