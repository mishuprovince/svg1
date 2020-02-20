package com.example.svg1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;


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


public class SvgView extends View {
    private Context context;
    private Paint paint;
    private List<Graph> GraphList = new ArrayList<>();
    private Graph selectGraph;
    private int[] colorArray = new int[]{0xFF30A9E5, 0xFF80CBF1, 0xFFB0D7F8};
    private String[] graphname=new String[]{"圆形","正方形","三角形"};

    public SvgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        loadgraph.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (GraphList != null) {
            for (Graph item : GraphList) {
                if (item != selectGraph) {
                    item.draw(canvas, paint, false);
                }
            }
            if (selectGraph != null) {
                selectGraph.draw(canvas, paint, true);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (GraphList != null) {
            Graph graph = null;
            for (Graph item : GraphList) {
                if (item.isSelect((int) (event.getX()), (int) (event.getY() ))) {
                    Toast.makeText(context, item.getName()+"", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        return true;
    }

    private Thread loadgraph = new Thread() {
        @Override
        public void run() {
            InputStream inputStream = context.getResources().openRawResource(R.raw.graphs);
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(inputStream);
                Element rootElement = doc.getDocumentElement();
                NodeList items = rootElement.getElementsByTagName("path");
                for (int i = 0; i < items.getLength(); i++) {
                    Element element = (Element) items.item(i);
                    String pathData = element.getAttribute("android:pathData");
                    Path path = PathParser.createPathFromPathData(pathData);
                    GraphList.add(new Graph(path,colorArray[i],graphname[i]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    };
}
