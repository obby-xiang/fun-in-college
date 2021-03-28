package org.example;

import java.util.Scanner;

/**
 * @since 2016-09-22
 */
public class ChineseNumerals {
    //金额的中文大写形式,输入小数范围[0,10^13)，小数点后超过两位的只保留十分位和百分位.
    public static int digit(String str,int f[],int z[]){
        //把浮点数整数部分从高位到低位数字依次存入z，小数部分从高位到低位数字依次存入f，返回整数部分位数.
        int i,m,j;
        str+="   ";
        for(i=0;i<str.length()-3;i++){
            if(str.charAt(i)!='0'||(str.charAt(i)=='0'&&str.charAt(i+1)!='0')) break;
        }
        for(j=0;i<str.length()-3;i++,j++){
            if(str.charAt(i)=='.'||str.charAt(i)==' ') break;
            z[j]=str.charAt(i)-'0';
        }
        //整数部分从第一个不为0的数字开始存入z.
        m=j;
        if(m==0) m=1;
        //如果没有数字存入z，则整数部分为0.
        for(i=i+1,j=0;j<2;i++,j++){
            if(str.charAt(i)>='0'&&str.charAt(i)<='9')
                f[j]=str.charAt(i)-'0';
        }
        return m;
    }
    public static void main(String[] args) {
        String s1="零壹贰叁肆伍陆柒捌玖",s2="拾佰仟",s3="元万亿",s;
        double n;
        int m=0,i,j,k;
        Scanner sc=new Scanner(System.in);
        while(sc.hasNextLine()){
            int f[]=new int[2],z[]=new int[20];
            String str="";
            s=sc.nextLine();//输入字符串（代表浮点数）.
            n=Double.valueOf(s);//字符串转化为双精度浮点数
            if(n<0||n>999999999999.99)
                System.out.println("ERROR");//判断浮点数是否超出给定范围
            else{
                m=digit(s,f,z);
                for(i=0,j=m;i<m;i++,j--){
                    if(z[i]!=0){
                        //如果数字不为0.
                        str+=s1.charAt(z[i]);//把中文大写数字存入str.
                        if((j-1)%4!=0) str+=s2.charAt((j-1)%4-1);
                        //如果数字所在位数序号（1~m）不能被4整除，加上对应的单位.如123456,1和5后面加拾，4后面加佰，3后面加仟.
                    }
                    else{
                        //如果数字为0.
                        if(str.length()>=1&&str.charAt(str.length()-1)!='零'&&(j-1)%4!=0){
                            //如果str长度不为0，str最后一个字符不是零，数字所在位数序号不能被4整除
                            for(k=i;k<=i+(j-1)%4;k++)
                                if(z[k]!=0){
                                    str+='零';//如果所在的（从个位开始往高位）每4位数不都为0，则str添上零.
                                    //比如100001234,1234是第一个（从个位开始往高位）每4位，0000是第二个（从个位开始往高位）每4位，均为0，故不用读出零.
                                    //比如10003,0003是第一个每4位，不都为0，故需要读出零.
                                    break;
                                }
                        }
                    }
                    if(j!=1&&(j-1)%4==0&&(j+4<=n)){
                        for(k=i;k>i-4;k--)
                            if(z[k]!=0){
                                str+=s3.charAt((j-1)/4);
                                break;
                            }
                        //（从个位开始往高位）每4位的最低位还有单位，如果所在的（从个位开始往高位）每4位都为0，则不读出.
                        //比如100001234，不用读出万；1000234，需要读出万.
                    }
                }
                if(str.length()==0) str+='零';//如果上述读完后str仍为空，则表示整数部分为0，添上0.
                str+='元';//整数部分读完，添上元.
                if(f[0]!=0||f[1]!=0)
                    //如果小数部分不都为零，则需要读出角和分.
                    str+=""+s1.charAt(f[0])+'角'+s1.charAt(f[1])+'分';
                else str+='整';//如果小数部分都为0，则添上整.
                System.out.println(str);
            }
        }
        sc.close();
    }
}
