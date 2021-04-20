package org.example;

import java.text.SimpleDateFormat;

/**
 * 日期
 *
 * @author obby-xiang
 * @since 2016-10-29
 */
public class Date {
    private final int[] mon = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};//年、月、日及（非闰年）每月的天数
    private int year;
    private int month;
    private int day;

    public Date() {
        //无参数的构造方法
        this.set();
    }

    public Date(int y, int m, int d) {
        //以年、月、日构造对象
        this.set(y, m, d);//调用本类方法
    }

    public Date(Date d) {
        //以实例构造对象
        this.set(d.year, d.month, d.day);
    }

    public void set() {
        //设置日期值为当天的日期
        java.util.Date date = new java.util.Date();
        String s;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        s = dateFormat.format(date);
        year = Integer.parseInt(s.substring(0, 4));
        month = Integer.parseInt(s.substring(4, 6));
        day = Integer.parseInt(s.substring(6, 8));
    }

    public void set(int y, int m, int d) {
        //以年、月、日设置日期值，如果日期不合法，则将日期设置为当天的日期
        mon[1] = (y % 4 == 0 && y % 100 != 0) || y % 400 == 0 ? 29 : 28;
        if (y >= 1970 && m > 0 && m <= 12 && d > 0 && d <= mon[m - 1]) {
            year = y;
            month = m;
            day = d;
        } else this.set();
    }

    public int getYear() {
        //获得年份
        return year;
    }

    public int getMonth() {
        //获得月份
        return month;
    }

    public int getDay() {
        //获得当月日期
        return day;
    }

    public String toString() {
        //返回中文日期字符串
        return "" + year + "年" + String.format("%02d", month) + "月" + String.format("%2d", day) + "日";
    }
}
