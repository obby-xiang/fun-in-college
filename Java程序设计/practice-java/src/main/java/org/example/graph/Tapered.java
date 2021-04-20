package org.example.graph;

/**
 * 圆锥
 *
 * @author obby-xiang
 * @since 2016-11-10
 */
interface Area {
    //可计算面积接口
    double area();
}

interface Volume {
    //可计算体积接口
    double volume();
}

public class Tapered implements Area, Volume {
    //圆锥类，实现可计算面积接口和可计算体积接口
    protected Circle circle;//底面图形，圆
    protected double height;//高度

    public Tapered() {
        //无参数的构造方法
        this(new Circle(new Point(1, 1), 1), 1);
    }

    public Tapered(Circle circle, double height) {
        //以圆和高度构造对象
        this.circle = circle;
        this.height = height;
    }

    public static void main(String[] args) {
        Tapered tapered = new Tapered(new Circle(new Point(200, 200), 99), 99);
        System.out.println(tapered);
    }

    public double area() {
        //计算表面积
        double r = circle.length / 2;
        return circle.perimeter() * Math.sqrt(r * r + height * height) / 2 + circle.area();
    }

    public double volume() {
        //计算体积
        return circle.area() * height / 3;
    }

    public String toString() {
        return getClass().getName() + "圆锥," + "底面是 " + circle.toString() + ";高" + height + ";表面积" + String.format("%.2f", this.area()) + ",体积" + String.format("%.2f", this.volume());
    }
}
/*
程序运行结果:
Tapered圆锥,底面是 Circle圆,圆心坐标(200.00,200.00),直径99.00;高99.0;表面积24910.24,体积254023.68
*/
