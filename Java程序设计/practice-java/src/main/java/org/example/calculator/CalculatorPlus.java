package org.example.calculator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @since 2016-12-08
 */
public class CalculatorPlus extends Calculator{
    protected static final int STANDARD=0,SCIENCE=1;//计算器的类型，标准和科学
    protected static final int ABDIGIT=7;//用字母表示的常量类型，如π
    protected String signal="";//表达式的缩写形式
    //说明：由于复杂的运算符不止一个字符，退格时又需要删除该完整的运算符，所以复杂的运算符用单个字母表示，
    //'A'+该运算符所在按钮的序号（从0开始）
    protected int type;//计算器类型
    protected HelpFrame help;
    protected JMenuBar jmb=new JMenuBar();//菜单栏
    protected JMenu jm=new JMenu("类型");//菜单
    protected JMenuItem jmi=new JMenuItem("帮助");
    protected JRadioButtonMenuItem jbm1=new JRadioButtonMenuItem("标准"),
            jbm2=new JRadioButtonMenuItem("科学");//单选菜单项
    protected ButtonGroup bg=new ButtonGroup();//按钮组
    protected JButton jbp[]={
            new JButton("√"),new JButton("<html><sup><font size=4>y</font></SUP>√x</html>"),
            new JButton("<html>x<sup><font size=4>2</font></SUP></html>"),new JButton("<html>x<sup><font size=4>3</font></SUP></html>"),
            new JButton("%"),
            new JButton("<html>x<sup><font size=4>y</font></SUP></html>"),new JButton("<html>e<sup><font size=4>x</font></SUP></html>"),
            new JButton("sin"),new JButton("cos"),new JButton("tan"),
            new JButton("ln"),new JButton("lg"),new JButton("<html>sin<sup><font size=4>-1</font></SUP></html>"),
            new JButton("<html>cos<sup><font size=4>-1</font></SUP></html>"),new JButton("<html>tan<sup><font size=4>-1</font></SUP></html>"),
            new JButton("<html>10<sup><font size=4>x</font></SUP></html>"),
            new JButton("1/x"),new JButton("n!"),new JButton("π"),new JButton("e")};//按钮
    protected String show[]={
            "√(","^(1÷","^(2)","^(3)","%",
            "^(","e^(","sin(","cos(","tan(",
            "ln(","lg(","arcsin(","arccos(","arctan(",
            "10^(","^(-1)","!","π","e"};//对应按钮显示的字符串
    protected String keyPressedp[]={
            "R","Shift + R","Ctrl + 2","Ctrl +3","Shift + 5（即%）",
            "Shift + 6（即^）","Ctrl + E","S","C","T",
            "L","Shift + L","Shift + S","Shift + C","Shift + T",
            "Ctrl + 0","Ctrl + -","Shift + 1（即!）","P","E"};//帮助信息
    protected int keyCodep[]={
            KeyEvent.VK_R,KeyEvent.VK_R,KeyEvent.VK_2,KeyEvent.VK_3,KeyEvent.VK_5,
            KeyEvent.VK_6,KeyEvent.VK_E,KeyEvent.VK_S,KeyEvent.VK_C,KeyEvent.VK_T,
            KeyEvent.VK_L,KeyEvent.VK_L,KeyEvent.VK_S,KeyEvent.VK_C,KeyEvent.VK_T,
            KeyEvent.VK_0,KeyEvent.VK_MINUS,KeyEvent.VK_1,KeyEvent.VK_P,KeyEvent.VK_E};
    protected int inputKeyp[]={
            0,InputEvent.SHIFT_MASK,InputEvent.CTRL_MASK,InputEvent.CTRL_MASK,InputEvent.SHIFT_MASK,
            InputEvent.SHIFT_MASK,InputEvent.CTRL_MASK,0,0,0,
            0,InputEvent.SHIFT_MASK,InputEvent.SHIFT_MASK,InputEvent.SHIFT_MASK,InputEvent.SHIFT_MASK,
            InputEvent.CTRL_MASK,InputEvent.CTRL_MASK,InputEvent.SHIFT_MASK,0,0};//按钮对应的按键
    protected int chTypep[]={LBRACKET,OPERATOR,RBRACKET,RBRACKET,RBRACKET,
            OPERATOR,LBRACKET,LBRACKET,LBRACKET,LBRACKET,
            LBRACKET,LBRACKET,LBRACKET,LBRACKET,LBRACKET,
            LBRACKET,RBRACKET,RBRACKET,ABDIGIT,ABDIGIT};//按钮上字符串代表的类型
    //说明：由于左运算符都带有左括号，所以将其看成左括号；右运算符和右括号使用情况类似，所以将其看成右括号
    protected boolean statep[]=new boolean[jbp.length];//按钮的状态
    protected JPanel jpp[]={new JPanel(),new JPanel(),new JPanel()};//面板，用于放按钮
    protected Font f1=new Font("",Font.BOLD,20);//字体

    public void addKeyp(){
        //为按钮设置按键
        super.addKey();
        for(int i=0;i<keyCodep.length;i++)
            jbp[i].registerKeyboardAction(this,KeyStroke.getKeyStroke(keyCodep[i],inputKeyp[i]),JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public CalculatorPlus(){
        //无参数的构造方法
        jbm1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,InputEvent.CTRL_MASK));
        jbm2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,InputEvent.CTRL_MASK));//为单选菜单项设置快捷键
        help=new HelpFrame(jb,jbp,keyPressed,keyPressedp);
        addKeyp();
        setFrameS();
        jf.requestFocus();
    }

    public void update(){
        //计算器类型转换时清空框架
        jf.remove(jmb);
        jmb.removeAll();
        jm.removeAll();
        bg.remove(jbm1);
        bg.remove(jbm2);
        jbm1.removeActionListener(this);
        jbm2.removeActionListener(this);
        jmi.removeActionListener(this);
        jf.remove(mjp);
        mjp.removeAll();
        for(int i=0;i<jp.length;i++)
            jp[i].removeAll();
        for(int i=0;i<jpp.length;i++)
            jpp[i].removeAll();
        for(int i=0;i<jb.length;i++)
            jb[i].removeActionListener(this);
        for(int i=0;i<jbp.length;i++)
            jbp[i].removeActionListener(this);
    }


    public void setFrameS(){
        //设置标准型计算器框架
        update();//清空原有框架
        type=STANDARD;//标准类型
        jm.addSeparator();//添加分割线
        jbm1.addActionListener(this);
        jbm2.addActionListener(this);//为单选菜单项注册动作事件监听器
        jmi.addActionListener(this);
        jbm1.setSelected(true);//指定“标准”单选菜单项为选中状态
        bg.add(jbm1);
        bg.add(jbm2);//按钮组添加单选菜单项
        jm.add(jbm1);
        jm.add(jbm2);//菜单添加单选菜单
        jmb.add(jm);//菜单栏添加菜单
        jmb.add(jmi);
        jf.setJMenuBar(jmb);//将菜单栏放置在框架窗口上方
        super.setFrame();//调用父类设置框架方法
        setStateP();//设置按钮状态
    }

    public void setFrameP(){
        //设置科学型计算器框架
        update();//清空原有框架
        type=SCIENCE;//科学类型
        jf.setSize(500,650);//设置窗口大小
        jf.setLocationRelativeTo(null);//窗口居中
        mjp.setLayout(new GridLayout(10,1,10,10));//设置主面板布局
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//窗口关闭时，程序结束
        jm.addSeparator();//添加分割线
        jbm1.addActionListener(this);
        jbm2.addActionListener(this);//为单选菜单项注册动作事件监听器
        jmi.addActionListener(this);
        jbm2.setSelected(true);//指定“科学”单选菜单项为选中状态
        bg.add(jbm1);
        bg.add(jbm2);//按钮组添加单选菜单项
        jm.add(jbm1);
        jm.add(jbm2);//菜单添加单选菜单项
        jmb.add(jm);//菜单栏添加菜单
        jmb.add(jmi);
        jf.setJMenuBar(jmb);//将菜单栏放置在框架窗口上方
        jt1.setEditable(false);
        jt2.setEditable(false);//文本域不可编辑
        jt1.setFont(f);
        jt2.setFont(f);//设置文本域字体
        jt1.setLineWrap(true);
        jt2.setLineWrap(true);//设置文本域自动换行
        mjp.add(new JScrollPane(jt1));
        mjp.add(new JScrollPane(jt2));//文本域带滚动条，并添加该滚动窗格到主面板
        for(int i=0;i<jb.length;i++){
            jb[i].addActionListener(this);//为按钮注册动作事件监听器
            jb[i].setFont(f1);//设置按钮字体
        }
        for(int i=0;i<jbp.length;i++){
            jbp[i].addActionListener(this);//为按钮注册动作事件监听器
            jbp[i].setFont(f1);//设置按钮字体
        }
        jp[0].setLayout(new GridLayout(1,5,10,0));//面板布局，添加按钮到面板
        jp[0].add(jbp[0]);
        jp[0].add(jbp[1]);
        jp[0].add(jbp[2]);
        jp[0].add(jbp[3]);
        jp[0].add(jbp[4]);
        mjp.add(jp[0]);//添加面板到主面板
        jp[1].setLayout(new GridLayout(1,5,10,0));
        jp[1].add(jbp[5]);
        jp[1].add(jbp[6]);
        jp[1].add(jbp[7]);
        jp[1].add(jbp[8]);
        jp[1].add(jbp[9]);
        mjp.add(jp[1]);
        jp[2].setLayout(new GridLayout(1,5,10,0));
        jp[2].add(jbp[10]);
        jp[2].add(jbp[11]);
        jp[2].add(jbp[12]);
        jp[2].add(jbp[13]);
        jp[2].add(jbp[14]);
        mjp.add(jp[2]);
        jp[3].setLayout(new GridLayout(1,5,10,0));
        jp[3].add(jbp[15]);
        jp[3].add(jb[10]);
        jp[3].add(jb[11]);
        jp[3].add(jb[12]);
        jp[3].add(jb[13]);
        mjp.add(jp[3]);
        jp[4].setLayout(new GridLayout(1,5,10,0));
        jp[4].add(jbp[16]);
        jp[4].add(jb[7]);
        jp[4].add(jb[8]);
        jp[4].add(jb[9]);
        jp[4].add(jb[14]);
        mjp.add(jp[4]);
        jpp[0].setLayout(new GridLayout(1,5,10,0));
        jpp[0].add(jbp[17]);
        jpp[0].add(jb[4]);
        jpp[0].add(jb[5]);
        jpp[0].add(jb[6]);
        jpp[0].add(jb[15]);
        mjp.add(jpp[0]);
        jpp[1].setLayout(new GridLayout(1,5,10,0));
        jpp[1].add(jbp[18]);
        jpp[1].add(jb[1]);
        jpp[1].add(jb[2]);
        jpp[1].add(jb[3]);
        jpp[1].add(jb[16]);
        mjp.add(jpp[1]);
        jpp[2].setLayout(new GridLayout(1,5,10,0));
        jpp[2].add(jbp[19]);
        jpp[2].add(jb[0]);
        jpp[2].add(jb[17]);
        jpp[2].add(jb[18]);
        jpp[2].add(jb[19]);
        mjp.add(jpp[2]);
        jf.add(mjp);//添加主面板到框架
        mjp.setVisible(true);//主面板可见
        jf.setVisible(true);//框架可见
        setStateP();//设置按钮状态
    }

    public void setStateP(){
        //设置按钮状态
        if(jt1.getText().isEmpty()){
            //表达式文本域为空
            for(int i=0;i<jb.length;i++){
                switch(chType[i]){
                    case RBRACKET:case OPERATOR:case POINT:case EQUALITY:
                        state[i]=false;
                        break;
                    default:
                        state[i]=true;
                }
                if(jb[i].getText()=="-")
                    //负号可用
                    state[i]=true;
                if(jb[i].getText()=="back")
                    //退格不可用
                    state[i]=false;
            }
            for(int i=0;i<jbp.length;i++){
                switch(chTypep[i]){
                    case OPERATOR:case RBRACKET:
                        statep[i]=false;
                        break;
                    default:
                        statep[i]=true;
                }
            }
        }
        else if(jt2.getText().isEmpty()){
            //表达式文本域不为空，运算结果文本域为空
            char ch=jt1.getText().charAt(jt1.getText().length()-1);
            switch(JudgeCh(ch)){
                //判断表达式最后一个字符代表的类型，根据该类型调整按钮状态
                case OPERATOR:
                    for(int i=0;i<jb.length;i++){
                        switch(chType[i]){
                            case OPERATOR:case RBRACKET:case EQUALITY:case POINT:
                                state[i]=false;
                                break;
                            default:
                                state[i]=true;
                        }
                        if(jb[i].getText()=="-"&&(ch=='×'||ch=='÷'))
                            state[i]=true;
                    }
                    for(int i=0;i<jbp.length;i++){
                        switch(chTypep[i]){
                            case OPERATOR:case RBRACKET:
                                statep[i]=false;
                                break;
                            default:
                                statep[i]=true;
                        }
                    }
                    break;
                case DIGIT:
                    for(int i=0;i<jb.length;i++){
                        switch(chType[i]){
                            case LBRACKET:case POINT:
                                state[i]=false;
                                break;
                            default:
                                state[i]=true;
                        }
                        if(jb[i].getText()=="."){
                            //字符是数字时，判断该数字是否属于一个完整数的小数部分
                            //若不属于，小数点的按钮可用;否则不可用
                            int j=jt1.getText().length()-1;
                            for(;j>=0&&jt1.getText().charAt(j)>='0'&&jt1.getText().charAt(j)<='9';j--);
                            if(j<0||jt1.getText().charAt(j)!='.')
                                state[i]=true;
                        }
                    }
                    for(int i=0;i<jbp.length;i++){
                        switch(chTypep[i]){
                            case ABDIGIT:case LBRACKET:
                                statep[i]=false;
                                break;
                            default:
                                statep[i]=true;
                        }
                    }
                    break;
                case LBRACKET:
                    for(int i=0;i<jb.length;i++){
                        switch(chType[i]){
                            case OPERATOR:case RBRACKET:case POINT:case EQUALITY:
                                state[i]=false;
                                break;
                            default:
                                state[i]=true;
                        }
                        if(jb[i].getText()=="-")
                            state[i]=true;
                    }
                    for(int i=0;i<jbp.length;i++){
                        switch(chTypep[i]){
                            case OPERATOR:case RBRACKET:
                                statep[i]=false;
                                break;
                            default:
                                statep[i]=true;
                        }
                    }
                    break;
                case RBRACKET:
                    for(int i=0;i<jb.length;i++){
                        switch(chType[i]){
                            case DIGIT:case LBRACKET:case POINT:
                                state[i]=false;
                                break;
                            default:
                                state[i]=true;
                        }
                    }
                    for(int i=0;i<jbp.length;i++){
                        switch(chTypep[i]){
                            case ABDIGIT:case LBRACKET:
                                statep[i]=false;
                                break;
                            default:
                                statep[i]=true;
                        }
                    }
                    break;
                case POINT:
                    for(int i=0;i<jb.length;i++){
                        switch(chType[i]){
                            case LBRACKET:case RBRACKET:case OPERATOR:case POINT:case EQUALITY:
                                state[i]=false;
                                break;
                            default:
                                state[i]=true;
                        }
                    }
                    for(int i=0;i<jbp.length;i++)
                        statep[i]=false;
                    break;
                case ABDIGIT:
                    for(int i=0;i<jb.length;i++){
                        switch(chType[i]){
                            case LBRACKET:case POINT:case DIGIT:
                                state[i]=false;
                                break;
                            default:
                                state[i]=true;
                        }
                    }
                    for(int i=0;i<jbp.length;i++){
                        switch(chTypep[i]){
                            case ABDIGIT:case LBRACKET:
                                statep[i]=false;
                                break;
                            default:
                                statep[i]=true;
                        }
                    }
            }
            //若表达式不存在左括号或表达式中所有的左括号已有右括号与之匹配，则右括号的按钮不可用
            int j=0;
            for(int i=0;i<jt1.getText().length();i++){
                if(jt1.getText().charAt(i)=='(')
                    j++;
                if(jt1.getText().charAt(i)==')')
                    j--;
            }
            if(j==0)
                state[18]=false;
        }
        else if(jt2.getText().equals("错误")||jt2.getText().equals("∞")){
            //表达式文本域不为空，运算结果文本域内容为“错误”或无穷大
            for(int i=0;i<jb.length;i++){
                switch(chType[i]){
                    case OPERATOR:case RBRACKET:case POINT:case EQUALITY:
                        state[i]=false;
                        break;
                    default:
                        state[i]=true;
                }
                if(jb[i].getText()=="-")
                    state[i]=true;
            }
            for(int i=0;i<jbp.length;i++){
                switch(chTypep[i]){
                    case OPERATOR:case RBRACKET:
                        statep[i]=false;
                        break;
                    default:
                        statep[i]=true;
                }
            }
        }
        else{
            //表达式文本域不为空，运算结果文本域内容为数
            for(int i=0;i<jb.length;i++){
                switch(chType[i]){
                    case RBRACKET:case EQUALITY:case POINT:
                        state[i]=false;
                        break;
                    default:
                        state[i]=true;
                }
            }
            for(int i=0;i<jbp.length;i++)
                statep[i]=true;
        }
        //设置按钮颜色，可用的按钮为绿色，不可用的按钮为灰色
        for(int i=0;i<jb.length;i++){
            if(state[i]==true)
                jb[i].setBackground(Color.GREEN);
            else jb[i].setBackground(Color.GRAY);
        }
        for(int i=0;i<jbp.length;i++){
            if(statep[i]==true)
                jbp[i].setBackground(Color.GREEN);
            else jbp[i].setBackground(Color.GRAY);
        }
    }

    public void actionPerformed(ActionEvent e){
        //动作事件处理方法
        if(e.getSource()==jmi){
            help.show();
            return;
        }
        if(e.getSource()==jbm1||e.getSource()==jbm2){
            //设置计算器类型
            if(jbm1.isSelected()&&type!=STANDARD){
                jt1.setText("");
                jt2.setText("");
                signal="";
                setFrameS();
            }
            if(jbm2.isSelected()&&type!=SCIENCE){
                jt1.setText("");
                jt2.setText("");
                signal="";
                setFrameP();
            }
            setStateP();//设置按钮状态
            return;
        }
        for(int i=0;i<jb.length;i++){
            if(e.getSource()==jb[i]){
                switch(chType[i]){
                    //判断按钮上字符串代表的类型
                    case -1:
                        //无类型
                        if(jb[i].getText()=="C"){
                            //清空
                            jt1.setText("");
                            jt2.setText("");
                            signal="";
                        }
                        else if(jb[i].getText()=="back"){
                            //退格
                            if(state[i]==true){
                                char ch=signal.charAt(signal.length()-1);
                                int l;
                                if(ch>='A'&&ch<'A'+jbp.length)
                                    //复杂运算符
                                    l=show[ch-'A'].length();
                                else l=1;//简单运算符
                                signal=signal.substring(0,signal.length()-1);
                                jt1.setText(jt1.getText().substring(0,jt1.getText().length()-l));
                                if(!jt2.getText().isEmpty())
                                    jt2.setText("");
                            }
                        }
                        break;
                    case EQUALITY:
                        //等号
                        if(state[i]==true){
                            int k=0;
                            for(int j=0;j<jt1.getText().length();j++){
                                if(jt1.getText().charAt(j)=='(')
                                    k++;
                                if(jt1.getText().charAt(j)==')')
                                    k--;
                            }
                            for(;k>0;k--){
                                //补上缺少的右括号
                                jt1.setText(jt1.getText()+")");
                                signal+=")";
                            }
                            try{
                                jt2.setText(df.format(new Calculate(jt1.getText()).calculate()));
                            }
                            catch(Exception ex){
                                //异常
                                jt2.setText("错误");
                            }
                            signal+="=";
                            jt1.setText(jt1.getText()+"=");
                        }
                        break;
                    default:
                        //其他类型
                        if(!jt2.getText().isEmpty()&&state[i]==true){
                            //运算结果文本域不为空且按钮可用
                            if(!(jt2.getText().equals("错误")||jt2.getText().equals("∞"))
                                    &&chType[i]==OPERATOR)
                                jt1.setText(jt2.getText()+jb[i].getText());
                            else
                                jt1.setText(jb[i].getText());
                            signal=jt1.getText();
                            jt2.setText("");
                        }
                        else if(jt2.getText().isEmpty()&&state[i]==true){
                            //运算结果文本域为空且按钮可用，将按钮上的字符串连接到表达式文本域的内容
                            signal+=jb[i].getText();
                            jt1.setText(jt1.getText()+jb[i].getText());
                        }
                }
                setStateP();//设置按钮状态
                return;
            }
        }
        for(int i=0;i<jbp.length;i++){
            //新增加的按钮
            if(e.getSource()==jbp[i]){
                if(!jt2.getText().isEmpty()&&statep[i]==true){
                    //运算结果文本域不为空且按钮可用
                    if(!(jt2.getText().equals("错误")||jt2.getText().equals("∞"))
                            &&(chTypep[i]==OPERATOR||chTypep[i]==RBRACKET)){
                        jt1.setText(jt2.getText()+show[i]);
                        signal=jt2.getText()+(char)('A'+i);
                    }
                    else{
                        jt1.setText(show[i]);
                        signal=""+(char)('A'+i);
                    }
                    jt2.setText("");
                }
                else if(jt2.getText().isEmpty()&&statep[i]==true){
                    //运算结果文本域为空且按钮可用，将按钮上的字符串连接到表达式文本域的内容
                    signal+=(char)('A'+i);
                    jt1.setText(jt1.getText()+show[i]);
                }
                break;
            }
        }
        setStateP();//设置按钮状态
    }

    public int JudgeCh(char ch){
        //判断字符类型
        if(ch=='^')
            return OPERATOR;
        else if(ch=='!'||ch=='%')
            return RBRACKET;
        else if(ch=='e'||ch=='π')
            return ABDIGIT;
        return super.JudgeCh(ch);
    }

    public static void main(String args[]){
        new CalculatorPlus();
    }
}
class HelpFrame{
    //帮助窗口
    private JFrame jf=new JFrame("帮助");
    private JLabel jl[];
    private JButton jb[];
    private JPanel jp[];
    private Font f=new Font("",Font.BOLD,20);

    public HelpFrame(JButton jb[],JButton jbp[],String keyPressed[],String keyPressedp[]){
        jl=new JLabel[jb.length+jbp.length];
        this.jb=new JButton[jb.length+jbp.length];
        jp=new JPanel[jb.length+jbp.length];
        jf.setSize(500,500);//窗口大小
        jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setLayout(new BorderLayout());
        JLabel jl1=new JLabel("按钮对应的按键");
        jl1.setFont(f);
        JPanel jp1=new JPanel(new BorderLayout());
        jp1.add(jl1);
        jf.add(jp1,"North");
        JPanel jp2=new JPanel(new GridLayout(20,2,0,10));
        int i;
        for(i=0;i<jb.length;i++){
            jp[i]=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
            this.jb[i]=new JButton(jb[i].getText());
            jl[i]=new JLabel(":"+keyPressed[i]);
            jl[i].setFont(f);
            this.jb[i].setFont(f);
            jp[i].add(this.jb[i]);
            jp[i].add(jl[i]);
            jp2.add(jp[i]);
        }
        for(;i<jb.length+jbp.length;i++){
            jp[i]=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
            this.jb[i]=new JButton(jbp[i-jb.length].getText());
            jl[i]=new JLabel(":"+keyPressedp[i-jb.length]);
            jl[i].setFont(f);
            this.jb[i].setFont(f);
            jp[i].add(this.jb[i]);
            jp[i].add(jl[i]);
            jp2.add(jp[i]);
        }
        jf.add(new JScrollPane(jp2),"Center");
    }

    public void show(){
        //显示
        jf.setExtendedState(JFrame.NORMAL);
        jf.setSize(500,500);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    public void hide(){
        //隐藏
        jf.setVisible(false);
    }
}

