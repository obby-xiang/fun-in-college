package org.example.calculator;

import java.util.Stack;
import java.text.DecimalFormat;

/**
 * @since 2016-12-08
 */
public class Calculate {

    private String expression="",simp="";//表达式、（部分计算后的）化简式

    protected String Lstr[]={"√(","sin(","cos(","tan(","ln(","lg(","arcsin(","arccos(","arctan("};//左运算符
    protected String Rstr[]={"!","%"};//由运算符
    protected DecimalFormat df=new DecimalFormat("#.#########"),df1=new DecimalFormat("#");//数的格式

    public Calculate(String s){
        //以字符串构造对象
        expression=s;
    }


    public int Precede(char a,char b){
        //算符优先级
        switch(a){
            case '+':case '-':
                switch(b){
                    case '+':case '-':case ')':case '#':
                        return 1;
                    default:
                        return -1;
                }
            case '×':case '÷':
                switch(b){
                    case '(':case '^':
                        return -1;
                    default:
                        return 1;
                }
            case '(':
                switch(b){
                    case ')':
                        return 0;
                    default:
                        return -1;
                }
            case ')':
                return 1;
            case '^':
                switch(b){
                    case '(':
                        return -1;
                    default:
                        return 1;
                }
            default:
                //'#'
                switch(b){
                    case '#':
                        return 0;
                    default:
                        return -1;
                }
        }
    }
    public double Operate(double a,char ch,double b){
        //计算加减乘除及次方
        switch(ch){
            case '+':
                return a+b;
            case '-':
                return a-b;
            case '×':
                return a*b;
            case '÷':
                return a/b;
            default:
                //次方
                return Math.pow(a,b);
        }
    }
    public double Operate(String op,double dou){
        //左运算符的计算
        if(op=="√(") return Math.sqrt(dou);
        else if(op=="sin(") return Math.sin(dou);
        else if(op=="cos(") return Math.cos(dou);
        else if(op=="tan(") return Math.tan(dou);
        else if(op=="ln(") return Math.log(dou);
        else if(op=="lg(") return Math.log10(dou);
        else if(op=="arcsin(") return Math.asin(dou);
        else if(op=="arccos(") return Math.acos(dou);
        else return Math.atan(dou);//arctan
    }


    public double EvaluateExpression(String s){
        //计算只含简单运算符的表达式
        Stack<Character> chStack=new Stack<Character>();//运算符栈
        Stack<Double> douStack=new Stack<Double>();//数栈
        s+="#";
        chStack.push('#');
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='-'){
                if(i==0||s.charAt(i-1)=='×'||s.charAt(i-1)=='÷'||s.charAt(i-1)=='('){
                    //'-'为负号
                    int j;
                    for(j=i+1;j<s.length()&&((s.charAt(j)>='0'&&s.charAt(j)<='9')||s.charAt(j)=='.');j++);
                    douStack.push(-1*Double.parseDouble(s.substring(i+1,j)));//数入数栈
                    i=j-1;
                    continue;
                }
            }
            if(s.charAt(i)>='0'&&s.charAt(i)<='9'){
                //取出完整的数
                int j;
                for(j=i;j<s.length()&&((s.charAt(j)>='0'&&s.charAt(j)<='9')||s.charAt(j)=='.');j++);
                douStack.push(Double.parseDouble(s.substring(i,j)));//数入数栈
                i=j-1;
            }
            else if(s.charAt(i)=='e')
                //e为数
                douStack.push(Math.E);
            else if(s.charAt(i)=='π')
                //π为数
                douStack.push(Math.PI);
            else{
                switch(Precede(chStack.peek(),s.charAt(i))){
                    //比较运算符栈栈顶运算符和当前运算符的优先级
                    case -1:
                        //栈顶运算符优先级低
                        chStack.push(s.charAt(i));//当前运算符进运算符栈
                        break;
                    case 0:
                        //优先级相等
                        chStack.pop();//运算符栈栈顶运算符出栈
                        break;
                    case 1:
                        //栈顶运算符优先级高，取出栈顶运算符及两个数进行运算，并将运算结果入数栈
                        char ch=chStack.pop();
                        double b=douStack.pop();
                        double a=douStack.pop();
                        douStack.push(Operate(a,ch,b));
                        i--;//继续比较
                        break;
                }
            }
        }
        return douStack.peek();//数栈内唯一的数即表达式运算结果
    }


    public boolean findR(String s){
        //寻找第一个右运算符并计算该运算符作用的部分，存在右运算符返回true，否则返回false
        int begin,end=-1,v;//该运算符作用的部分的起始和终点位置
        for(int i=0;i<Rstr.length;i++){
            v=s.indexOf(Rstr[i]);
            if(v!=-1){
                if(end==-1) end=v;
                else if(v<end)
                    end=v;
            }
        }
        if(end==-1)
            return false;//不存在右运算符
        if(s.charAt(end-1)>='0'&&s.charAt(end-1)<='9'){
            //运算符前面是数字，提取完整的数并计算
            boolean point=false;
            for(begin=end-1;begin>=0&&((s.charAt(begin)>='0'&&s.charAt(begin)<='9')||s.charAt(begin)=='.');begin--){
                //寻找起始位置
                if(s.charAt(begin)=='.') point=true;//数为小数
            }
            begin++;
            simp=s.substring(0,begin);//起始位置之前的部分不变
            if(s.charAt(end)=='!'){
                //计算阶乘，并将结果连接到字符串
                double d=Double.parseDouble(s.substring(begin,end));
                if(point==false)
                    simp+=df1.format(gamma(d));//整数的阶乘仍为整数
                else
                    simp+=df.format(gamma(d));
            }
            else
                simp+=df.format(0.01*Double.parseDouble(s.substring(begin,end)));//计算百分数，并将结果连接到字符串
            simp+=s.substring(end+1);//终点位置之后的部分不变
        }
        else{
            //运算符前面是右括号
            //说明：由于当前的右运算符是第一个，所以在这之前不存在其他右运算符，
            //用findL调用该方法，可以保证在这之前不存在左运算符，
            //当不存在左运算符时调用该方法，又当前的右运算符是第一个，所以在这之前不存在左运算符和其他右运算符，
            //也就保证了该右括号内的表达式可以调用EvaluateExpression进行计算
            int k;
            for(k=1,begin=end-2;begin>=0&&k!=0;begin--){
                //寻找起始位置
                if(s.charAt(begin)=='(')
                    k--;
                else if(s.charAt(begin)==')')
                    k++;
            }
            begin++;
            simp=s.substring(0,begin);//起始位置之前的部分不变
            double dou=EvaluateExpression(s.substring(begin,end));//计算该右括号内的表达式，并将结果连接到字符串
            boolean point=false;
            for(int i=0;i<df.format(dou).length();i++){
                if(df.format(dou).charAt(i)=='.')
                    point=true;//数为小数
            }
            if(s.charAt(end)=='!'){
                //计算阶乘，并将结果连接到字符串
                if(dou<0)
                    //不存在负数的阶乘，抛出异常
                    throw new ArithmeticException();
                else{
                    if(point==false)
                        simp+=df1.format(gamma(dou));//整数的阶乘仍为整数
                    else simp+=df.format(gamma(dou));
                }
            }
            else simp+=df.format(0.01*dou);//计算百分数，并将结果连接到字符串
            simp+=s.substring(end+1);//终点位置之后的部分不变
        }
        return true;
    }

    public boolean findL(String s){
        //寻找最后一个左运算符并计算该运算符作用的部分，存在左运算符返回true，否则返回false
        int begin=-1,end,v;
        String op="";
        for(int i=0;i<Lstr.length;i++){
            v=s.lastIndexOf(Lstr[i]);
            if(v!=-1){
                if(begin==-1){
                    begin=v;
                    op=Lstr[i];
                }
                else if(v>begin){
                    begin=v;
                    op=Lstr[i];
                }
            }
        }
        if(begin==-1) return false;
        if(begin>2&&s.substring(begin-3,begin).equals("arc")){
            //由于三角函数名是反三角函数名的子串，所以需要进一步判断是否反三角函数
            begin-=3;
            op="arc"+op;
        }
        //说明：每个左运算符都带有左括号，由于当前左运算符是最后一个左运算符，所以该运算符之后不存在其他的做运算符，
        //调用findR将该左括号内所有带右运算符的算式计算出结果，直到该左括号内不存在右运算符，
        //也就保证了该左括号内的表达式可以调用EvaluateExpression进行计算
        int k;
        for(k=1,end=begin+op.length();end<s.length()&&k!=0;end++){
            //寻找终点位置
            if(s.charAt(end)=='(')
                k++;
            else if(s.charAt(end)==')')
                k--;
        }
        end--;
        simp=s.substring(begin+op.length(),end);//取出该左括号内的表达式
        while(findR(simp));//计算表达式内的带右运算符的表达式
        expression=s.substring(0, begin)+"("+df.format(Operate(op,EvaluateExpression(simp)))
                +")"+s.substring(end+1);//计算该左运算符作用的部分
        return true;
    }

    public double calculate(){
        //计算表达式
        while(findL(expression));//将表达式中带左运算符的部分计算
        while(findR(expression)){
            //将表达式中带右运算符的部分计算
            expression=simp;
        }
        //经过上面的操作，表达式的运算符只剩下简单的运算符
        return EvaluateExpression(expression);//计算表达式
    }

    //以下是伽玛函数，用于求阶乘
    private static  double array[]={0.99999999999980993, 676.5203681218851, -1259.1392167224028,
            771.32342877765313, -176.61502916214059, 12.507343278686905,
            -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7};
    private int num=7;
    public double gamma(double z){
        z+=1;
        if(z<0.5)
            return Math.PI/(Math.sin(Math.PI *z)*gamma(1-z));
        else{
            z-=1;
            double x=array[0];
            for(int i=1;i<(num+2);i++)
                x+=array[i]/(z+i);
            double t=z+num+0.5;
            return Math.sqrt(2*Math.PI)*Math.pow(t,(z+0.5))*Math.exp(-t)*x;
        }
    }
}
