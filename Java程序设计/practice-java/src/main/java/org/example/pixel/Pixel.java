package org.example.pixel;

import org.example.Color;

/**
 * 像素
 *
 * @author obby-xiang
 * @since 2016-11-03
 */
public class Pixel extends Point {
    private Color color;

    public Pixel(int x, int y, Color color) {
        //以坐标值x,y和颜色实例构造对象
        set(x, y, color);
    }

    public Pixel(int x, int y, int color) {
        //以坐标值x,y和颜色值构造对象
        set(x, y, color);
    }

    public Pixel(Point p, Color color) {
        //以点实例和颜色实例构造对象
        set(p, color);
    }

    public Pixel(Point p, int color) {
        //以点实例和颜色值构造对象
        set(p, color);
    }

    public Pixel(Point p) {
        //以点实例构造对象
        super(p);
        this.color = new Color();//默认颜色
    }

    public Pixel(Color color) {
        //以颜色实例构造对象
        super();//默认坐标
        this.color = new Color(color);
    }

    public Pixel(Pixel p) {
        //以像素点实例构造对象
        set(p);
    }

    public Pixel() {
        //无参数构造方法
        this(new Point(), new Color());
    }

    public static void main(String[] args) {
        Pixel p = new Pixel(1, 1, 0xfffffff);
        System.out.println(p);
        p.set(9, 9, new Color());
        System.out.println(p);
    }

    public void set(int x, int y, Color color) {
        //以坐标值x,y和颜色实例设置像素点
        set(x, y, color.getColor());
    }

    public void set(int x, int y, int color) {
        //以坐标值x,y和颜色值设置像素点
        this.x = x;
        this.y = y;
        this.color = new Color(color);

    }

    public void set(Point p, Color color) {
        //以点实例和颜色实例设置像素点
        set(p.x, p.y, color);
    }

    public void set(Point p, int color) {
        //以点实例和颜色值设置像素点
        set(p.x, p.y, color);
    }

    public void set(Pixel p) {
        //以像素点实例设置像素点
        set(p.x, p.y, p.color);
    }

    public String toString() {
        //返回像素点的坐标和颜色
        return "Pixel\t" + super.toString() + " " + color.toString();
    }
}
/*
程序运行结果:
Pixel	Point(1,1) Color 0xfffffff RGB(255,255,255)
Pixel	Point(9,9) Color 0xff000000 RGB(0,0,0)
*/
