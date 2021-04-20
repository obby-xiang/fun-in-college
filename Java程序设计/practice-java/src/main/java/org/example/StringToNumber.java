package org.example;

/**
 * 字符串转数字
 *
 * @author obby-xiang
 * @since 2016-11-17
 */
public class StringToNumber {
    public static double toNumber(String s) {
        //静态方法，把字符串串s转换成数字
        double n = 0.0;
        if (s == null) return 0.0;//空指针
        try {
            n = Double.parseDouble(s);//转换成浮点数
        } catch (NumberFormatException ex1) {
            //无法转换成浮点数
            try {
                n = Integer.parseInt(s, 16);//按16进制转换成整数
            } catch (NumberFormatException ex2) {
                System.out.println("\"" + s + "\"" + "字符串无法转换为数字," + ex2);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public static void main(String[] args) {
        System.out.println(StringToNumber.toNumber("999.999"));
        System.out.println(StringToNumber.toNumber("999"));
        System.out.println(StringToNumber.toNumber("fff"));
        System.out.println(StringToNumber.toNumber("zfff"));
        System.out.println(StringToNumber.toNumber("9.9.9"));
    }
}
/*
程序运行结果:
999.999
999.0
4095.0
"zfff"字符串无法转换为数字,java.lang.NumberFormatException: For input string: "zfff"
0.0
"9.9.9"字符串无法转换为数字,java.lang.NumberFormatException: For input string: "9.9.9"
0.0
*/
