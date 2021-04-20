package org.example.graph;

import java.text.DecimalFormat;

/**
 * 点
 *
 * @author obby-xiang
 * @since 2016-10-31
 */
public class Point {
    public static DecimalFormat df = new DecimalFormat("0.00");//小数格式
    public double x, y;

    public Point() {
        //无参数的构造方法
        this(0, 0);
    }

    public Point(double x, double y) {
        //以坐标值x,y构造对象
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        //以实例构造对象
        this.x = p.x;
        this.y = p.y;
    }

    public String toString() {
        //返回坐标
        return "(" + df.format(this.x) + "," + df.format(this.y) + ")";
    }
}
