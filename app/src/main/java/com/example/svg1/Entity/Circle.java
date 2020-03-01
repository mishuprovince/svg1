package com.example.svg1.Entity;

import android.graphics.Point;

public class Circle {

    private int cx,cy,r,color,scale;
    private Point point;

    @Override
    public String toString() {
        return "Circle{" +
                "cx=" + cx +
                ", cy=" + cy +
                ", r=" + r +
                ", color=" + color +
                ", scale=" + scale +
                ", point=" + point +
                '}';
    }

    public Circle(int cx, int cy, int r, int color, int scale, Point point) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.color = color;
        this.scale = scale;
        this.point = point;
    }

    public int getCx() {
        return cx;
    }

    public void setCx(int cx) {
        this.cx = cx;
    }

    public int getCy() {
        return cy;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
