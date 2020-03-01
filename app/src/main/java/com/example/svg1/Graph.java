package com.example.svg1;



import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

public class Graph {
    private Path path;
    private int color;
    private String name;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Graph(Path path, int color, String name) {
        this.path = path;
        this.color = color;
        this.name = name;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    void draw(Canvas canvas, Paint paint, boolean isSelect)
    {
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(8, 0, 0, 0xffffff);
        canvas.drawPath(path, paint);
            paint.clearShadowLayer();
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            canvas.drawPath(path,paint);
    }

    public boolean isSelect(float x,float y){
        RectF rectF=new RectF();
        path.computeBounds(rectF,true);
        Region region=new Region();
        region.setPath(path,new Region((int)rectF.left,(int)rectF.top,(int)rectF.right,(int)rectF.bottom));
        return region.contains((int)x, (int) y);
    }
}
