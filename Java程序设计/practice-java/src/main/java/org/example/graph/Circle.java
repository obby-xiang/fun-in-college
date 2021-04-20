package org.example.graph;

import java.text.DecimalFormat;

/**
 * 圆
 *
 * @author obby-xiang
 * @since 2016-10-31
 */
public class Circle extends Ellipse {
    public static DecimalFormat df = new DecimalFormat("0.00");//小数格式

    public Circle(Point point, double radius) {
        //以圆心及直径构造
        super(point, radius, radius);
        this.shape = "圆";
    }

    public static void main(String[] args) {
        Point p = new Point(300, 300);
        Circle circle = new Circle(p, 200);
        circle.draw();
        System.out.print("circle:");
        circle.print();
        circle.zoom(80);
        System.out.print("将circle指定圆心" + circle.point.toString() + "缩放80% ");
        System.out.print("circle:");
        circle.print();
        Point p1 = new Point(300, 310), p2 = new Point(150, 300);
        System.out.println("点p1" + p1 + ",点p2" + p2);
        System.out.println("点p1在圆circle内? " + circle.contains(p1));
        System.out.println("点p2在圆circle内? " + circle.contains(p2));
    }

    public String toString() {
        return this.getClass().getName() + shape + ",圆心坐标" + this.point + ",直径" + df.format(length);
    }

    public double perimeter() {
        //计算周长
        return super.perimeter();
    }

    public double area() {
        //计算面积
        return super.area();
    }

    public void revolve(int angle) {
        //旋转
        return;
    }

    public void zoom(int percentage) {
        //缩放
        length = length * percentage / 100;
        width = length;
    }

    public void draw() {
        //画图
        super.draw();
    }

    public boolean contians(Point p) {
        //判断点是否在圆内(或圆上)
        return super.contains(p);
    }
}
/*
程序运行结果:
circle:Circle圆,圆心坐标(300.00,300.00),直径200.00,周长628.32,面积31415.93
将circle指定圆心(300.00,300.00)缩放80% circle:Circle圆,圆心坐标(300.00,300.00),直径160.00,周长502.65,面积20106.19
点p1(300.00,310.00),点p2(150.00,300.00)
点p1在圆circle内? true
点p2在圆circle内? false
*/
