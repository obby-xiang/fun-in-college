package org.example;

/**
 * 颜色
 *
 * @author obby-xiang
 * @since 2016-10-28
 */
public class Color {
    private int color;//表示颜色的整数

    public Color() {
        //无参数的构造方法
        color = 0xff000000;
    }

    public Color(int color) {
        //以表示颜色的整数构造对象
        this.set(color);
    }

    public Color(int red, int green, int blue) {
        //以红、绿、蓝三原色值构造对象
        this.set(red, green, blue);
    }

    public Color(Color color) {
        //以实例构造对象
        set(color);
    }

    public static void main(String[] args) {
        Color x = new Color(2, 5, 6);
        System.out.println(x);
        x.set(256);
        System.out.println(x);
    }

    public void set(int color) {
        //以表示颜色的整数设置颜色值
        this.color = color >= 0 ? color : 0xff000000;//如果整数值不在范围内，取值0xff000000
    }

    public void set(int red, int green, int blue) {
        //以红、蓝、绿三原色值设置颜色值
        red = red >= 0 && red <= 0xff ? red : 0;
        green = green >= 0 && green <= 0xff ? green : 0;
        blue = blue >= 0 && blue <= 0xff ? blue : 0;//如果单色值不在[0,255]的范围内，取值为0
        color = 0xff000000 + red * 0x10000 + green * 0x100 + blue;
    }

    public void set(Color color) {
        //以实例设置颜色值
        set(color.color);
    }

    public int getColor() {
        //获得表示颜色的整数
        return color;
    }

    public String toString() {
        //返回表示颜色的整数值和RGB值
        int color = this.color >= 0xff000000 ? this.color - 0xff000000 : this.color;
        return "Color 0x" + Integer.toHexString(this.color) +
                " RGB(" + color / 0x10000 % 0x100 + "," + color / 0x100 % 0x100 + "," + color % 0x100 + ")";
    }
}
/*
程序运行结果:
Color 0xff020506 RGB(2,5,6)
Color 0x100 RGB(0,1,0)
*/
