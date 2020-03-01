package com.example.svg1.Entity;

import android.graphics.Point;

public class Square {

    private int width;
    private int heigh;
    private int color;

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    private int scale;
    private Point point;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigh() {
        return heigh;
    }

    public void setHeigh(int heigh) {
        this.heigh = heigh;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Square{" +
                "width=" + width +
                ", heigh=" + heigh +
                ", color=" + color +
                ", scale=" + scale +
                ", point=" + point +
                '}';
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Square(int width, int heigh, int color, int scale, Point point) {
        this.width = width;
        this.heigh = heigh;
        this.color = color;
        this.scale = scale;
        this.point = point;
    }
}
