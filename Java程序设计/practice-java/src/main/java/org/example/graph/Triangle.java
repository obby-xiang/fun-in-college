package org.example.graph;

import javax.swing.*;
import java.awt.*;

/**
 * 三角形
 *
 * @author obby-xiang
 * @since 2016-10-31
 */
public class Triangle extends ClosedFigure {
    public Point point2, point3;
    protected double a, b, c;

    public Triangle(Point p1, Point p2, Point p3) {
        //以三点构造对象
        super(p1, "三角形");
        point2 = p2;
        point3 = p3;
        a = Line.length(p1, p2);
        b = Line.length(p2, p3);
        c = Line.length(p3, p1);
    }

    public Triangle(Point p1, double a, double b, double c) {
        //以一点及三边长度构造对象
        super(p1, "三角形");
        point2 = point3 = null;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public static double area(Point p1, Point p2, Point p3) {
        //静态方法计算面积
        return new Triangle(p1, p2, p3).area();
    }

    public static void main(String[] args) {
        Triangle triangle = new Triangle(new Point(200, 250), new Point(50, 50), new Point(300, 50));
        System.out.print("triangle:");
        triangle.print();
        triangle.revolve(90);
        System.out.print("将triangle绕点" + triangle.point.toString() + "旋转90度   ");
        System.out.print("triangle:");
        triangle.print();
        triangle.zoom(60);
        triangle.draw();
        System.out.print("将triangle指定顶点" + triangle.point.toString() + "缩放60% ");
        System.out.print("triangle:");
        triangle.print();
        Point p1 = new Point(300, 253), p2 = new Point(350, 300);
        System.out.println("点p1" + p1 + ",点p2" + p2);
        System.out.println("点p1在三角形triangle内? " + triangle.contains(p1));
        System.out.println("点p2在三角形triangle内? " + triangle.contains(p2));
    }

    public String toString() {
        return this.getClass().getName() + this.shape
                + ",三点坐标" + this.point.toString() + ","
                + (this.point2 == null ? "null" : this.point2.toString()) + ","
                + (this.point3 == null ? "null" : this.point3.toString())
                + ",三边各长" + String.format("%1.2f,%1.2f,%1.2f", a, b, c);
    }

    public double perimeter() {
        //计算周长
        return a + b + c;
    }

    public double area() {
        //计算面积
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    public void revolve(int angle) {
        //旋转
        if (point2 != null && point3 != null) {
            Line line = new Line(point, point2), line2 = new Line(point, point3);
            line.revolve(angle);
            point2 = line.point2;
            line2.revolve(angle);
            point3 = line2.point2;
        }
    }

    public void zoom(int percentage) {
        //缩放
        if (point2 != null && point3 != null) {
            Line line = new Line(point, point2), line2 = new Line(point, point3);
            line.zoom(percentage);
            line2.zoom(percentage);
            point2 = line.point2;
            point3 = line2.point2;
        }
        a = a * percentage / 100;
        b = b * percentage / 100;
        c = c * percentage / 100;
    }

    public void draw() {
        //画图
        if (point2 != null && point3 != null) {
            new TriangleFrame(this.toString(), point, point2, point3);
        }
    }

    public boolean contains(Point p) {
        //判断点是否在三角形内
        if (new Line(point, point2).contains(p) || new Line(point, point3).contains(p) || new Line(point2, point3).contains(p))
            return true;
        else return area() >= area(p, point, point2) + area(p, point, point3) + area(p, point2, point3);
    }
}

/*
程序运行结果:
triangle:Triangle三角形,三点坐标(200.00,250.00),(50.00,50.00),(300.00,50.00),三边各长250.00,250.00,223.61,周长723.61,面积25000.00
将triangle绕点(200.00,250.00)旋转90度   triangle:Triangle三角形,三点坐标(200.00,250.00),(400.00,100.00),(400.00,350.00),三边各长250.00,250.00,223.61,周长723.61,面积25000.00
将triangle指定顶点(200.00,250.00)缩放60% triangle:Triangle三角形,三点坐标(200.00,250.00),(320.00,160.00),(320.00,310.00),三边各长150.00,150.00,134.16,周长434.16,面积9000.00
点p1(300.00,253.00),点p2(350.00,300.00)
点p1在三角形triangle内? true
点p2在三角形triangle内? false
 */
class TriangleFrame extends JFrame {
    private static final long serialVersionUID = 8803660589596094968L;
    private final JCanvas canvas;
    private final Point point1;
    private final Point point2;
    private final Point point3;

    public TriangleFrame(String title, Point p1, Point p2, Point p3) {
        super(title);
        point1 = p1;
        point2 = p2;
        point3 = p3;
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
        private static final long serialVersionUID = -3966016519993623906L;

        public JCanvas() {
            super();
        }

        public void paint(Graphics g) {
            g.setColor(new Color(0xffff0000));
            g.drawString(point1.toString(), (int) point1.x, (int) point1.y);
            g.drawString(point2.toString(), (int) point2.x, (int) point2.y);
            g.drawString(point3.toString(), (int) point3.x, (int) point3.y);
            g.setColor(new Color(0xff000000));
            g.drawLine((int) point1.x, (int) point1.y, (int) point2.x, (int) point2.y);
            g.drawLine((int) point1.x, (int) point1.y, (int) point3.x, (int) point3.y);
            g.drawLine((int) point2.x, (int) point2.y, (int) point3.x, (int) point3.y);
        }
    }
}
