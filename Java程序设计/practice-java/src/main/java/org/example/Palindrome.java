package org.example;

import java.util.Scanner;

/**
 * 回文串
 *
 * @author obby-xiang
 * @since 2016-09-22
 */
public class Palindrome {
    //判断回文串，是输出YES，否输出NO.
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int i, j;
        String s;
        while (sc.hasNextLine()) {
            s = sc.nextLine();//输入字符串.
            for (i = 0, j = s.length() - 1; i < s.length() / 2; i++, j--) {
                //比较字符，第一个和最后一个比较，第二个和倒数第二个比较…
                //长度为奇数时，i+1最多只要到(长度-1)/2；长度为偶数时，i+1最多只要到长度/2.故i<s.length()/2.
                if (s.charAt(i) != s.charAt(j))//当比较的字符不相等，停止循环.
                    break;
            }
            if (i < s.length() / 2) System.out.println("NO");
            else if (s.length() != 0) System.out.println("YES");
        }
        sc.close();
    }

}
