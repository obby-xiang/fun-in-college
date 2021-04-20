package org.example.graph;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * 椭圆
 *
 * @author obby-xiang
 * @since 2016-10-31
 */
public class Ellipse extends ClosedFigure {
    public static DecimalFormat df = new DecimalFormat("0.00");//小数格式
    public double length, width;

    public Ellipse(Point point, double length, double width) {
        //以椭圆中心、长轴和短轴构造对象
        super(point, "椭圆");
        this.length = length;
        this.width = width;
    }

    public Ellipse(Point p1, Point p2) {
        //以外切矩形不相邻的两点构造对象
        super(new Point(), "椭圆");
        length = Math.abs(p1.x - p2.x);
        width = Math.abs(p1.y - p2.y);
        point.x = length / 2 + p1.x;
        point.y = width / 2 + p1.y;//中心坐标
    }

    public static void main(String[] args) {
        Point p = new Point(300, 300);
        Ellipse ellipse = new Ellipse(p, 100, 300);
        ellipse.draw();
        System.out.print("ellipse:");
        ellipse.print();
        ellipse.zoom(80);
        System.out.print("将ellipse指定椭圆中心" + ellipse.point.toString() + "缩放80% ");
        System.out.print("ellipse:");
        ellipse.print();
        Point p1 = new Point(300, 310), p2 = new Point(350, 300);
        System.out.println("点p1" + p1 + ",点p2" + p2);
        System.out.println("点p1在椭圆ellipse内? " + ellipse.contains(p1));
        System.out.println("点p2在椭圆ellipse内? " + ellipse.contains(p2));
        ellipse.revolve(90);
    }

    public String toString() {
        return this.getClass().getName() + shape + ",椭圆中心坐标" + point.toString() +
                ",长轴" + (length > width ? df.format(length) : df.format(width)) +
                ",短轴" + (length < width ? df.format(length) : df.format(width));
    }

    public double perimeter() {
        //计算周长
        return Math.PI * (length < width ? length : width) + 2 * Math.abs(length - width);
    }

    public double area() {
        //计算面积
        return Math.PI * length * width / 4;
    }

    public void revolve(int angle) {
        //旋转，只是将显示的图形旋转
        double k = angle * Math.PI / 180;
        Point p = new Point(point.x - length / 2, point.y - width / 2);
        new EllipseFrame(this + "旋转" + angle + "度", point, p, length, width, k);
    }

    public void zoom(int percentage) {
        //缩放
        length = length * percentage / 100;
        width = width * percentage / 100;
    }

    public void draw() {
        //画图
        Point p = new Point(point.x - length / 2, point.y - width / 2);
        new EllipseFrame(this.toString(), point, p, length, width, 0);
    }

    public boolean contains(Point p) {
        //判断点是否在椭圆内(包括在椭圆上)
        double a, b;
        //运用数学知识求解
        if (length > width) {
            a = length / 2;
            b = width / 2;
            return (p.x - point.x) * (p.x - point.x) / (a * a) + (p.y - point.y) * (p.y - point.y) / (b * b) <= 1;
        } else {
            a = width / 2;
            b = length / 2;
            return (p.x - point.x) * (p.x - point.x) / (b * b) + (p.y - point.y) * (p.y - point.y) / (a * a) <= 1;
        }
    }
}

/*
程序运行结果:
ellipse Ellipse椭圆,椭圆中心坐标(300.00,300.00),长轴300.00,短轴100.00,周长714.16,面积23561.94
将ellipse指定椭圆中心(300.00,300.00)缩放80% ellipse Ellipse椭圆,椭圆中心坐标(300.00,300.00),长轴240.00,短轴80.00,周长571.33,面积15079.64
点p1(300.00,310.00),点p2(350.00,300.00)
点p1在椭圆ellipse内? true
点p2在椭圆ellipse内? false
 */
class EllipseFrame extends JFrame {
    private static final long serialVersionUID = -8332387801067185659L;
    private final JCanvas canvas;
    private final Point p0;
    private final Point point;
    private final double length;
    private final double width;
    private final double angle;

    public EllipseFrame(String title, Point point, Point p, double length, double width, double angle) {
        super(title);
        p0 = point;
        this.point = p;
        this.length = length;
        this.width = width;
        this.angle = angle;
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
        private static final long serialVersionUID = 5296587980078709826L;

        public JCanvas() {
            super();
        }

        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g.setColor(new Color(0xffff0000));
            g.drawString("." + p0.toString(), (int) p0.x, (int) p0.y);
            g2d.rotate(angle, (int) p0.x, (int) p0.y);
            g.setColor(new Color(0xff000000));
            g.drawOval((int) point.x, (int) point.y, (int) length, (int) width);
        }
    }
}
