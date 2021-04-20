package org.example.graph;

/**
 * 正方形
 *
 * @author obby-xiang
 * @since 2016-10-31
 */
public class Square extends Rectangle {
    public Square(Point point, double width) {
        //以左上角顶点及边长构造对象
        super(point, width, width);
        this.shape = "正方形";
    }

    public Square(Point p1, Point p2) {
        //以两不相邻顶点构造对象
        super(p1, p2);
    }

    public static double perimeter(double width) {
        //静态方法，计算周长
        return 4 * width;
    }

    public static double area(double width) {
        //静态方法，计算面积
        return width * width;
    }

    public static void main(String[] args) {
        Point p = new Point(100, 100);
        Square square = new Square(p, 150);
        System.out.print("square:");
        square.print();
        square.revolve(60);
        System.out.print("将square绕点" + new Point(square.point.x + (square.point2.x - square.point.x) / 2,
                square.point.y + (square.point2.y - square.point.y) / 2) + "顺时针旋转60度");
        System.out.print(" square:");
        square.print();
        square.zoom(130);
        square.draw();
        System.out.print("将square指定顶点" + square.point.toString() + "缩放130%");
        System.out.print(" square:");
        square.print();
        Point p1 = new Point(139, 250), p2 = new Point(300, 500);
        System.out.println("点p1" + p1 + ",点p2" + p2);
        System.out.println("点p1在正方形square内? " + square.contains(p1));
        System.out.println("点p2在正方形square内? " + square.contains(p2));
    }

    public String toString() {
        return this.getClass().getName() + shape
                + ",四点坐标" + point.toString() + ","
                + point2.toString()
                + this.point3.toString() + ","
                + this.point4.toString()
                + ",边长" + width;
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
        super.revolve(angle);
    }

    public void zoom(int percentage) {
        //缩放
        super.zoom(percentage);
    }

    public void draw() {
        //画图
        super.draw();
    }

    public boolean contians(Point p) {
        //判断点是否在正方形内
        return super.contains(p);
    }
}
/*
程序运行结果:
square:Square正方形,四点坐标(100.00,100.00),(250.00,250.00)(250.00,100.00),(100.00,250.00),边长150.0,周长600.00,面积22500.00
将square绕点(175.00,175.00)顺时针旋转60度 square:Square正方形,四点坐标(202.45,72.55),(147.55,277.45)(277.45,202.45),(72.55,147.55),边长150.0,周长600.00,面积22500.00
将square指定顶点(202.45,72.55)缩放130% square:Square正方形,四点坐标(202.45,72.55),(131.08,338.92)(299.95,241.42),(33.58,170.05),边长195.0,周长780.00,面积38025.00
点p1(139.00,250.00),点p2(300.00,500.00)
点p1在正方形square内? true
点p2在正方形square内? false
*/
