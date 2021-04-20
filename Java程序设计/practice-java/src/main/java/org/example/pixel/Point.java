package org.example.pixel;

/**
 * 点
 *
 * @author obby-xiang
 * @since 2016-10-18
 */
public class Point {
    public int x, y;

    public Point() {
        //无参数的构造方法
        this(0, 0);
    }

    public Point(int x, int y) {
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
        return "Point(" + this.x + "," + this.y + ")";
    }
}
