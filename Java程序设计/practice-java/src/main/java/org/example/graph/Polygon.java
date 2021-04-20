package org.example.graph;

import javax.swing.*;
import java.awt.*;

/**
 * 多边形
 *
 * @author obby-xiang
 * @since 2016-10-31
 */
public class Polygon extends ClosedFigure {
    private final Point[] points;

    public Polygon(final Point[] points) {
        //以若干个顶点构造对象
        super(points[0], "多边形");
        this.points = points;
    }

    public static void main(String[] args) {
        Point[] p = new Point[5];
        p[0] = new Point(200, 200);
        p[1] = new Point(180, 220);
        p[2] = new Point(120, 340);
        p[3] = new Point(210, 303);
        p[4] = new Point(250, 180);
        Polygon polygon = new Polygon(p);
        System.out.print("polygon:");
        polygon.print();
        polygon.revolve(95);
        System.out.print("将polygon绕点" + polygon.point.toString() + "顺时针旋转95度");
        System.out.print(" polygon:");
        polygon.print();
        polygon.zoom(110);
        polygon.draw();
        System.out.print("将polygon指定顶点" + polygon.point.toString() + "缩放110%");
        System.out.print(" polygon:");
        polygon.print();
        Point p1 = new Point(87, 180), p2 = new Point(350, 300);
        System.out.println("点p1" + p1 + ",点p2" + p2);
        System.out.println("点p1在多边形polygon内? " + polygon.contains(p1));
        System.out.println("点p2在多边形polygon内? " + polygon.contains(p2));
    }

    public String toString() {
        String str = this.getClass().getName() + this.shape + "," + points.length + "个点" + this.points[0].toString();
        for (int i = 1; i < points.length; i++) {
            str += ", " + this.points[i].toString();
        }
        return str;
    }

    public double perimeter() {
        //计算周长
        double per = 0;
        int n = points.length;
        for (int i = 0; i < n; i++)
            per += Line.length(points[i], points[(i + 1) % n]);
        return per;
    }

    public double area() {
        //计算面积
        double s = 0;
        for (int i = 1; i < points.length - 1; i++)
            s += Triangle.area(points[0], points[i], points[i + 1]);
        return s;
    }

    public void revolve(int angle) {
        //旋转
        Line line;
        for (int i = 1; i < points.length; i++) {
            line = new Line(points[0], points[i]);
            line.revolve(angle);
            points[i] = line.point2;
        }
    }

    public void zoom(int percentage) {
        //缩放
        Line line;
        for (int i = 1; i < points.length; i++) {
            line = new Line(points[0], points[i]);
            line.zoom(percentage);
            points[i] = line.point2;
        }
    }

    public void draw() {
        //画图
        new PolygonFrame(this.toString(), points);
    }

    public boolean contains(Point p) {
        //判断点是否在多边形内
        for (int i = 1; i < points.length - 1; i++) {
            if (new Triangle(points[0], points[i], points[i + 1]).contains(p))
                return true;
        }
        return false;
    }
}

/*
程序运行结果:
polygon:Polygon多边形,5个点(200.00,200.00), (180.00,220.00), (120.00,340.00), (210.00,303.00), (250.00,180.00),周长442.95,面积8095.00
将polygon绕点(200.00,200.00)顺时针旋转95度 polygon:Polygon多边形,5个点(200.00,200.00), (181.82,178.33), (67.51,108.10), (96.52,200.98), (215.57,251.55),周长442.95,面积8095.00
将polygon指定顶点(200.00,200.00)缩放110% polygon:Polygon多边形,5个点(200.00,200.00), (180.00,176.17), (54.26,98.91), (86.17,201.08), (217.12,256.71),周长487.24,面积9794.95
点p1(87.00,180.00),点p2(350.00,300.00)
点p1在多边形polygon内? true
点p2在多边形polygon内? false
 */
class PolygonFrame extends JFrame {
    private static final long serialVersionUID = -3239468700096108507L;
    private final JCanvas canvas;
    private final Point[] points;

    public PolygonFrame(String title, Point[] p) {
        super(title);
        this.points = p;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 0, dim.width / 2, dim.height / 2);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel jpanel = new JPanel();
        this.getContentPane().add(jpanel);
        this.canvas = new JCanvas();
        this.getContentPane().add(canvas);
        this.setVisible(true);
    }

    class JCanvas extends Canvas {
        private static final long serialVersionUID = -6939996344561654519L;

        public JCanvas() {
            super();
        }

        public void paint(Graphics g) {
            int i;
            g.setColor(new Color(0xffff0000));
            for (i = 0; i < points.length; i++)
                g.drawString("." + points[i].toString(), (int) points[i].x, (int) points[i].y);
            g.setColor(new Color(0xff000000));
            for (i = 0; i < points.length - 1; i++) {
                g.drawLine((int) points[i].x, (int) points[i].y, (int) points[i + 1].x, (int) points[i + 1].y);
            }
            g.drawLine((int) points[i].x, (int) points[i].y, (int) points[0].x, (int) points[0].y);
        }
    }
}
