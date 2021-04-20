package org.example.average;

/**
 * 求平均值：加权平均值
 *
 * @author obby-xiang
 * @since 2016-11-11
 */
public class Average_c implements Average {
    public double[] value, weight;

    public Average_c() {
        //无参数的构造方法
        value = null;
        weight = null;
    }

    public Average_c(double[] value, double[] weight) {
        //以浮点数数组构造对象
        if (value == null || value.length == 0) this.value = null;//数组未初始化或元素个数为0
        else this.value = value;
        if (weight == null || weight.length == 0) this.weight = null;//数组未初始化或元素个数为0
        else this.weight = weight;
    }

    public Average_c(String[] value, String[] weight) {
        //以字符串数组构造对象
        if (value == null || value.length == 0) this.value = null;//数组未初始化或元素个数为0
        else {
            this.value = new double[value.length];
            for (int i = 0; i < value.length; i++)
                this.value[i] = Double.parseDouble(value[i]);
        }
        if (weight == null || weight.length == 0) this.weight = null;//数组未初始化或元素个数为0
        else {
            this.weight = new double[weight.length];
            for (int i = 0; i < weight.length; i++)
                this.weight[i] = Double.parseDouble(weight[i]);
        }
    }

    public static double average(double[] value, double[] weight) {
        //计算浮点数数组加权平均值，静态方法
        return new Average_c(value, weight).average();
    }

    public static double average(String[] value, String[] weight) {
        //计算字符串数组加权平均值，静态方法
        return new Average_c(value, weight).average();
    }

    public static void main(String[] args) {
        double[] value1 = {-99.86, 45, 68, 45, 12, 84, 54, -55}, weight1 = {12, 23, 58, 96, 36.2},
                value2 = null, weight2 = {56.3, 56, 21, -78};
        String[] value3 = {"98", "65.65", "11.11", "-56.23", "198", "123.56"}, weight3 = {};
        System.out.println("value1,weight1\n" + new Average_c(value1, weight1));
        System.out.println("value2,weight2\n" + new Average_c(value2, weight2));
        System.out.println("value3,weight3\n" + new Average_c(value3, weight3));
    }

    public double average() {
        //计算加权平均值
        if (value == null)
            //数组未初始化或元素个数为0
            return 0.0;
        else {
            double sum = 0.0;
            int i;
            if (weight == null) i = 0;//数组未初始化或元素个数为0
            else {
                for (i = 0; i < value.length && i < weight.length; i++)
                    sum += (weight[i] * value[i]);
            }
            for (; i < value.length; i++)
                //权重个数不足的以1计
                sum += value[i];
            return sum / value.length;
        }
    }

    public String toString() {
        String s = "value[]数组,";
        if (value == null) s += "数组未初始化或元素个数为0";
        else {
            s += (value.length + "个数值: ");
            for (int i = 0; i < value.length - 1; i++) s += (value[i] + "  ");
            if (value.length > 0) s += value[value.length - 1];
        }
        s += "\nweight[]数组,";
        if (weight == null) s += "数组未初始化或元素个数为0";
        else {
            s += (weight.length + "个数值: ");
            for (int i = 0; i < weight.length - 1; i++) s += (weight[i] + "  ");
            if (weight.length > 0) s += weight[weight.length - 1];
        }
        if (value == null) s += "\n无法求平均值";
        else s += ("\n加权平均值(权重不足以1计):" + String.format("%.2f", this.average()));
        return s;
    }
}
/*
程序运行结果:
value1,weight1
value[]数组,8个数值: -99.86  45.0  68.0  45.0  12.0  84.0  54.0  -55.0
weight[]数组,5个数值: 12.0  23.0  58.0  96.0  36.2
加权平均值(权重不足以1计):1077.26
value2,weight2
value[]数组,数组未初始化或元素个数为0
weight[]数组,4个数值: 56.3  56.0  21.0  -78.0
无法求平均值
value3,weight3
value[]数组,6个数值: 98.0  65.65  11.11  -56.23  198.0  123.56
weight[]数组,数组未初始化或元素个数为0
加权平均值(权重不足以1计):73.35
 */
