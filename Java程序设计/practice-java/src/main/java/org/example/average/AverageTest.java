package org.example.average;

/**
 * 求平均值异常处理测试
 *
 * @author obby-xiang
 * @since 2016-11-17
 */
public class AverageTest {
    int[] value;

    public static double average_(int[] value) throws Exception {
        //计算一组整型的平均值，静态方法
        return new AverageTest().average(value);
    }

    public static double average_(String[] value) throws Exception {
        //计算字符串数组的平均值，静态方法
        return new AverageTest().average(value);
    }

    public static void print(int[] value) throws Exception {
        //输出整型数组的元素，并输出平均值，静态方法
        AverageTest at = new AverageTest();
        double average = at.average(value);
        for (int i = 0; i < at.value.length; i++)
            System.out.print(at.value[i] + " ");
        System.out.println("\n" + "平均值:" + String.format("%.2f", average));
    }

    public static void print(String[] value) throws Exception {
        //把字符串数组转换成整型数组，输出整数，并输出平均值，静态方法
        AverageTest at = new AverageTest();
        double average = at.average(value);
        for (int i = 0; i < at.value.length; i++)
            System.out.print(at.value[i] + " ");
        System.out.println("\n" + "平均值:" + String.format("%.2f", average));
    }

    public static void main(String[] args) throws Exception {
        int[] value1 = {12, 36, 78, 96, 45, -15};
        String[] value2 = {"000099", "fff", "-1023", "28"};
        String[] value3 = {"123", "java", "-55"};
        System.out.print("value1[]:");
        AverageTest.print(value1);
        System.out.print("value2[]:");
        AverageTest.print(value2);
        System.out.print("value3[]:");
        AverageTest.print(value3);
    }

    public double average(int[] value) throws Exception {
        //计算一组整型的平均值
        int s = 0;
        if (value == null)
            //数组指针为空，抛出异常
            throw new NullPointerException("数组指针为空,无法求平均值");
        if (value.length == 0)
            //数组长度为0，抛出异常
            throw new IllegalArgumentException("数组元素个数为0,无法求平均值");
        for (int i = 0; i < value.length; i++) {
            s += value[i];
        }
        this.value = value;
        return 1.0 * s / value.length;
    }

    public double average(String[] value) throws Exception {
        //把字符串数组转换成整型数组，计算平均值
        int s = 0;
        if (value == null)
            //数组指针为空，抛出异常
            throw new NullPointerException("数组指针为空,无法求平均值");
        if (value.length == 0)
            //数组长度为0，抛出异常
            throw new IllegalArgumentException("数组元素个数为0,无法求平均值");
        this.value = new int[value.length];
        for (int i = 0; i < value.length; i++) {
            try {
                this.value[i] = Integer.parseInt(value[i]);//按十进制转换成整数
            } catch (NumberFormatException ex1) {
                try {
                    this.value[i] = Integer.parseInt(value[i], 16);//按十六进制转换成整数
                } catch (NumberFormatException ex2) {
                    //不能转换成整数，抛出异常
                    throw new NumberFormatException("\"" + value[i] + "\"" + "字符串不能转换成整数,无法求平均值");
                }
            }
            s += this.value[i];
        }
        return 1.0 * s / value.length;
    }
}
/*
程序运行结果：
value1[]:12 36 78 96 45 -15
平均值:42.00
value2[]:99 4095 -1023 28
平均值:799.75
value3[]:Exception in thread "main" java.lang.NumberFormatException: "java"字符串不能转换成整数,无法求平均值
	at AverageTest.average(AverageTest.java:42)
	at AverageTest.print(AverageTest.java:64)
	at AverageTest.main(AverageTest.java:78)
 */
