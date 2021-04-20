package org.example.average;

/**
 * 求平均值：去掉一个最大数和一个最小数后相加求平均值
 *
 * @author obby-xiang
 * @since 2016-11-11
 */
public class Average_b implements Average {
    public double[] value;
    private double max, min;//数组中的最大值和最小值

    public Average_b() {
        //无参数的构造方法
        value = null;
    }

    public Average_b(double[] value) {
        //以浮点数数组构造对象
        if (value == null || value.length == 0) this.value = null;//数组未初始化或元素个数为0
        else this.value = value;
    }

    public Average_b(String[] value) {
        //以字符串数组构造对象
        if (value == null || value.length == 0) this.value = null;//数组未初始化或元素个数为0
        else {
            this.value = new double[value.length];
            for (int i = 0; i < value.length; i++)
                this.value[i] = Double.parseDouble(value[i]);
        }
    }

    public static double average(double[] value) {
        //计算浮点数数组平均值，静态方法
        return new Average_b(value).average();
    }

    public static double average(String[] value) {
        //计算字符串数组平均值，静态方法
        return new Average_b(value).average();
    }

    public static void main(String[] args) {
        double[] value1 = {-99.86, 45, 68, 45, 12, 845, 54, -555}, value2 = {233, 123.3};
        String[] value3 = {"98", "65.65", "11.11", "-56.23", "198", "123.56"};
        System.out.println("value1  " + new Average_b(value1));
        System.out.println("value2  " + new Average_b(value2));
        System.out.println("value3  " + new Average_b(value3));
    }

    public double average() {
        //计算平均值，去掉一个最大数和一个最小数后相加求平均值
        if (value == null || value.length < 3)
            //数组未初始化或元素个数少于3
            return 0.0;
        else {
            double sum = 0.0;
            for (int i = 0; i < value.length; i++)
                sum += value[i];
            return (sum - min - max) / value.length;
        }
    }

    public double getMax() {
        //获得最大值
        max = value[0];
        for (int i = 0; i < value.length; i++)
            max = (value[i] > max) ? value[i] : max;
        return max;
    }

    public double getMin() {
        //获得最小值
        min = value[0];
        for (int i = 0; i < value.length; i++)
            min = (value[i] < min) ? value[i] : min;
        return min;
    }

    public String toString() {
        String s = "value[]数组,";
        if (value == null) s += "数组未初始化或元素个数为0\n无法求平均值";
        else {
            s += ("value[]数组," + value.length + "个数值: ");
            for (int i = 0; i < value.length - 1; i++) s += (value[i] + "  ");
            if (value.length > 0) s += value[value.length - 1];
            if (value.length >= 3)
                s += (("\n去掉一个最大数 " + getMax() + "和一个最小数" + getMin() + "后相加求平均值:") + String.format("%.2f", this.average()));
            else s += "\n数值个数少于3个,无法求平均值";
        }
        return s;
    }
}
/*
程序运行结果:
value1  value[]数组,value[]数组,8个数值: -99.86  45.0  68.0  45.0  12.0  845.0  54.0  -555.0
去掉一个最大数 845.0和一个最小数-555.0后相加求平均值:15.52
value2  value[]数组,value[]数组,2个数值: 233.0  123.3
数值个数少于3个,无法求平均值
value3  value[]数组,value[]数组,6个数值: 98.0  65.65  11.11  -56.23  198.0  123.56
去掉一个最大数 198.0和一个最小数-56.23后相加求平均值:49.72
 */
