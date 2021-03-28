package org.example;

public class SmithNumber {
    //输出400以内的Smith数
    public static int add_digit(int n){
        //计算整数所有数位上的数字和并返回
        int s=0,i=1;
        while(n/i>=1){
            s+=((n/i)%10);
            i*=10;
        }
        return s;
    }
    public static int add_factor(int n){
        //计算整数全部素数因子的数字和
        int i,s=0,m=n;
        for(i=2;m>=i&&i<=Math.sqrt(n);i++){
            //素数因子不含1，故i=2开始.因子除本身外，最大为√n，故i<=Math.sqrt(n).
            //m是剩下的因子，m<i又m!=0，m%i也就不可能为0,就没必要继续循环，故m>=i可以适当减少循环次数.
            if(m%i==0){
                s+=add_digit(i);//计算因子的数字和
                m=m/i;
                i--;//当能除尽时，下一次继续除以i，这就保证得到的因子是素数.
                //比如初始m=60,i=2;60/2=30,30/2=15,15/3=5,5/5=1.得到的因子依次是2,2,3,5,均为素数因子.
            }
        }
        if(m!=1&&s!=0) s=s+add_digit(m);
        //m=1有两种可能，一是n=1，二是上述循环已经找出所有的素数因子；s=0也有两种可能，一是n=1，二是n为素数.
        //n为素数时没有除了本身之外的素数因子.故当m!=1并且s!=0时，m是一个素数因子.
        return s;
    }
    public static void main(String[] args) {
        int i,t=0;
        for(i=1;i<=400;i++){
            if(add_digit(i)==add_factor(i)){
                System.out.printf(i+" ");//当整数符合Smith数条件时输出.
                t++;//计算Smith数的个数
            }
        }
        if(t==0) System.out.println("NONE");//如果在给定范围没有Smith数输出NONE
    }

}
