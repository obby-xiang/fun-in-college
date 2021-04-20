package org.example.graph;

/**
 * 图形
 *
 * @author obby-xiang
 * @since 2016-10-31
 */
public abstract class Figure {
    public Point point;//坐标点

    protected Figure(Point point) {
        //构造方法
        this.point = point;
    }

    protected Figure() {
        //构造方法
        this(new Point());
    }

    public abstract void revolve(int angle);//抽象方法，旋转

    public abstract void zoom(int percentage);//抽象方法，缩放

    public abstract void draw();//抽象方法，绘图
}
