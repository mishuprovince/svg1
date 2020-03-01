package com.example.svg1.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.xml.sax.SAXException;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import com.example.svg1.Entity.Circle;
import com.example.svg1.Entity.Square;
import com.example.svg1.Entity.Text;
import com.example.svg1.Entity.Triangle;
import com.example.svg1.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class GraphView extends View {

    private Context context;
    
    private Circle mcircle;
    private Square msquare;
    private Triangle mtriangle;
    private Text mtext;
    private List<Text> texts=new ArrayList<>();
    private int colors[] = new int[]{0xff000000, 0xffffffff, 0xffffff00, 0xffff0000, 0xff0000ff};
    private Paint mPaint = new Paint();

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        loadsvg.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawcircle(canvas);
        drawsquare(canvas);
        drawtriangle(canvas);
        for(Text text:texts){
            drawText(canvas,text);
        }
    }

    private void drawText(Canvas canvas,Text textt) {
        Rect rect = new Rect();
        String text=textt.getText();
        mPaint.setTextSize(textt.getSize());
        mPaint.setColor(textt.getColor());
        //绘制中心的数值
        mPaint.getTextBounds(text,0,textt.getText().length(),rect);
        canvas.drawText(text,textt.getPoint().x+textt.getX(),textt.getY()+textt.getPoint().y+rect.height(),mPaint);
    }

    private void drawcircle(Canvas canvas) {
        mPaint.setColor(mcircle.getColor());
        canvas.drawCircle(mcircle.getCx() + mcircle.getPoint().x-100, mcircle.getCy() + mcircle.getPoint().y-100, mcircle.getR() * mcircle.getScale(), mPaint);
    }

    private void drawsquare(Canvas canvas) {
        mPaint.setColor(msquare.getColor());
        int scale = msquare.getScale();
        RectF rectF = new RectF();
        rectF.left = msquare.getPoint().x - msquare.getWidth() * scale / 2.0f;
        rectF.right = msquare.getPoint().x + msquare.getWidth() * scale / 2.0f;
        rectF.top = msquare.getPoint().y - msquare.getHeigh() * scale / 2.0f;
        rectF.bottom = msquare.getPoint().y + msquare.getHeigh() * scale / 2.0f;
        canvas.drawRect(rectF, mPaint);
    }

    private void drawtriangle(Canvas canvas) {
        mPaint.setColor(mtriangle.getColor());
        Path path=mtriangle.getPath();
        Matrix matrix = new Matrix();
        matrix.setTranslate(mtriangle.getPoint().x-100,mtriangle.getPoint().y-120);
        path.transform(matrix);
        canvas.drawPath(path, mPaint);
    }

    private Thread loadsvg = new Thread() {
        @Override
        public void run() {
            super.run();
            InputStream inputStream = context.getResources().openRawResource(R.raw.sample);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                Document doc = factory.newDocumentBuilder().parse(inputStream);

                Element rootElement = doc.getDocumentElement();
//                String canvas = rootElement.getAttribute("viewBox");
//                NodeList itemList = rootElement.getElementsByTagName("svg");
                NodeList itemList = rootElement.getChildNodes();
                for (int i = 0; i < itemList.getLength(); i++) {
                    Node n = itemList.item(i);
                    Element gelement;
                    if (n.getNodeType() == Node.ELEMENT_NODE)
                        gelement = (Element) n;
                    else
                        continue;
                    String point = gelement.getAttribute("transform");
//                    NodeList gList = gelement.getElementsByTagName("g");
                    NodeList gList = gelement.getChildNodes();
                    for (int j = 0; j < gList.getLength(); j++) {
                        Node node = gList.item(j);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element child = (Element) node;
                            String color = child.getAttribute("fill");
                            if ("circle".equals(node.getNodeName())) {
                                String cx = child.getAttribute("cx");
                                String cy = child.getAttribute("cy");
                                String r = child.getAttribute("r");
                                String scale = child.getAttribute("transform");
                                mcircle = new Circle(Integer.valueOf(cx), Integer.valueOf(cy), Integer.valueOf(r), judgecolor(color), traslateScale(scale), traslate(point));
                                Log.i("circle", mcircle.toString());
                            } else if ("rect".equals(node.getNodeName())) {
                                String width = child.getAttribute("width");
                                String height = child.getAttribute("height");
                                String scale = child.getAttribute("transform");
                                msquare = new Square(Integer.valueOf(width), Integer.valueOf(height), judgecolor(color), traslateScale(scale), traslate(point));
                                Log.i("square", msquare.toString());
                            } else if ("text".equals(node.getNodeName())) {
                                String x = child.getAttribute("x");
                                String y = child.getAttribute("y");
                                String size = child.getAttribute("font-size");
                                String text = child.getFirstChild().getNodeValue();
                                mtext = new Text(text, Integer.valueOf(x), Integer.valueOf(y), judgecolor("lll"), Integer.valueOf(size), traslate(point));
                                texts.add(mtext);
                                Log.i("text", mtext.toString());
                            } else {
                                String paths = child.getAttribute("d");
                                Path path = PathParser.createPathFromPathData(paths);
                                mtriangle = new Triangle(judgecolor(color), path, traslate(point));
                                Log.i("triangle", mtriangle.toString());
                            }
                        }
                    }
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    private int judgecolor(String color) {
        if (color.equals("white")) {
            return colors[1];
        } else if (color.equals("yellow"))
            return colors[2];
        else if (color.equals("blue")) {
            return colors[4];
        } else if (color.equals("red"))
            return colors[3];
        else
            return colors[0];
    }


    public static Point traslate(String transform) {
        StringBuffer stringx = new StringBuffer();
        StringBuffer stringy = new StringBuffer();
        boolean b = true;
        for (int i = 0; i < transform.length(); i++) {
            char c = transform.charAt(i);
            if (c > 47 && c < 58) {
                if (b)
                    stringx.append(c);
                else
                    stringy.append(c);
            }
            if (c == 44) {
                b = false;
            }
        }
        int x = Integer.valueOf(stringx.toString());
        int y = Integer.valueOf(stringy.toString());
        return new Point(x, y);
    }

    private int traslateScale(String scale) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < scale.length(); i++) {
            char c = scale.charAt(i);
            if (c > 47 && c < 58) {
                stringBuffer.append(c);
            }
        }
        return Integer.valueOf(stringBuffer.toString());
    }

}
