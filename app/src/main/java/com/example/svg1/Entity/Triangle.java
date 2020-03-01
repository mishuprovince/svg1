package com.example.svg1.Entity;

import android.graphics.Path;
import android.graphics.Point;



public class Triangle {
    private int color;

    @Override
    public String toString() {
        return "Triangle{" +
                "color=" + color +
                ", path=" + path +
                ", point=" + point +
                '}';
    }

    private Path path;
    private Point point;

    public Triangle(int color, Path path, Point point) {
        this.color = color;
        this.path = path;
        this.point = point;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
