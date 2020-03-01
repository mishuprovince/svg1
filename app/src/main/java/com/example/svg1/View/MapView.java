package com.example.svg1.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;


import com.example.svg1.Entity.DrawMap;
import com.example.svg1.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MapView extends View {

    private Context context;
    private Paint paint;
    private List<DrawMap> drawMapList = new ArrayList<>();
    private DrawMap selectMap;
    private RectF selectRect=new RectF();
    private float mAnimScale;

    private ValueAnimator mValueAnim;
    private float scaleX = 1.0f, scaleY = 1.0f, scale = 1.0f;
    //    private RectF totalRect=new RectF(-85.74f,-135.75f,2223.92f,1161.65f);
//    private RectF totalRect=new RectF(0.06f,0.05f,776.58f,568.0f);
//    private int[] colorArray = new int[]{0xffff0000, 0xffffff00, 0xff0000ff, 0xFF80CBF1};
    private RectF totalRect = new RectF();

    /**
     * 画布的矩阵
     */
    private final Matrix mCanvasMatrix = new Matrix();
    /**
     * 触碰点的矩阵
     */
    private final Matrix mTouchChangeMatrix = new Matrix();
    private String[] graphname = new String[]{"圆形", "正方形", "三角形"};


    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        mValueAnim = ValueAnimator.ofFloat(0, 1);
        mValueAnim.setInterpolator(new LinearInterpolator());//设置匀速
        mValueAnim.setDuration(500);
        mValueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimScale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        loadgraph.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawMapList != null && drawMapList.size() > 0) {
            canvas.save();
//            canvas.scale(scaleX, scaleY);

            canvas.scale(scale, scale);
            for (DrawMap proviceItem : drawMapList) {
                if (selectMap != proviceItem) {
                    proviceItem.draw(canvas, paint, false);
                }
            }
            if (selectMap != null) {
                selectMap.draw(canvas, paint, true);
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (drawMapList != null || drawMapList.size() > 0) {
            DrawMap selectItem = null;
            for (DrawMap proviceItem : drawMapList) {
                if (proviceItem.isSelect(event.getX() / scaleX, event.getY() / scaleY)) {
                    selectItem = proviceItem;
                }
            }
            if (selectItem != null) {
                selectMap = selectItem;
                selectMap.getPath().computeBounds(selectRect,true);
                postInvalidate();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.i("width", width + "");
        Log.i("heigh", height + "");
        if (totalRect != null) {
            double mapWidth = totalRect.width();
            double mapHeigh = totalRect.height();
            Log.i("map", mapWidth + "");
            Log.i("map2", mapHeigh + "");

            scaleX = (float) (width / mapWidth);
            scaleY = (float) (height / mapHeigh);
            scale =Math.min(scaleX, scaleY);
            scale/=scale;
            if (scale > 1) {
                scale = (scale - 1) * mAnimScale + 1;
            } else if (scale < 1) {
                scale = 1 - (1 - scale) * mAnimScale;
            }
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY));
    }

    private void setScale(RectF rect, float beishu) {
        float diffHorizontal = (rect.right - rect.left) * (beishu - 1f);
        float diffVertical = (rect.bottom - rect.top) * (beishu - 1f);

        rect.top -= diffVertical / 2f;
        rect.bottom += diffVertical / 2f;

        rect.left -= diffHorizontal / 2f;
        rect.right += diffHorizontal / 2f;
    }

    private void handleCurState() {
        if (selectRect.equals(totalRect)) {
            return;
        }

        float curCenterX = selectRect.left + selectRect.width() / 2;
        float curCenterY = selectRect.top + selectRect.height() / 2;

        float lastCenterX = totalRect.left + totalRect.width() / 2;
        float lastCenterY = totalRect.top + totalRect.height() / 2;

        float dx = curCenterX - lastCenterX;
        float dy = curCenterY - lastCenterY;

        // 进行缩放
        if (!selectRect.isEmpty()) {

            scale = calculateScale(selectRect.width(), selectRect.height(),
                    getWidth(), getHeight()) / scale;

            if (scale > 1) {
                scale = (scale - 1) * mAnimScale + 1;
            } else if (scale < 1) {
                scale = 1 - (1 - scale) * mAnimScale;
            }
        }

        // 需要 多偏移区域 与 整地图 的外区域
        mCanvasMatrix.preTranslate(-dx * mAnimScale, -dy * mAnimScale);
        mCanvasMatrix.preScale(
                scale,
                scale,
                curCenterX,
                curCenterY);


        mTouchChangeMatrix.postTranslate(dx, dy);
        mTouchChangeMatrix.postScale(1 / scale,
                1 / scale,
                curCenterX,
                curCenterY);
    }

    private  void translate(){

    }

    private float calculateScale(float x,float y,float totalx,float totaly){
        return 0;
    }

    private Thread loadgraph = new Thread() {
        @Override
        public void run() {

            InputStream inputStream = context.getResources().openRawResource(R.raw.ic_smaple);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            Document doc = null;
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                doc = builder.parse(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Element rootElement = doc.getDocumentElement();

            getpath(rootElement);
        }
    };

    private void getpath(Element rootElement){
        float left = 100;
        float right = -1;
        float top = 100;
        float bottom = -1;
        NodeList item_g=rootElement.getElementsByTagName("g");
        NodeList items_path = rootElement.getElementsByTagName("path");
        for (int i = 0; i < items_path.getLength(); i++) {
            Element element = (Element) items_path.item(i);
            String pathData = element.getAttribute("android:pathData");
            String color = element.getAttribute("android:fillColor");
            Path path = PathParser.createPathFromPathData(pathData);
            drawMapList.add(new DrawMap(path, Color.parseColor(color), graphname[i % 3]));
            RectF rect = new RectF();
            path.computeBounds(rect, true);
            left = Math.min(left, rect.left);
            right = Math.max(right, rect.right);
            top = Math.min(top, rect.top);
            bottom = Math.max(bottom, rect.bottom);
        }
        totalRect = new RectF(left, top, right, bottom);
        postInvalidate();
    }
}
