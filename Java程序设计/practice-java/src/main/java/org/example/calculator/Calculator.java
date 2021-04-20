package org.example.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

/**
 * 计算器
 *
 * @author obby-xiang
 * @since 2016-12-08
 */
public class Calculator implements ActionListener {
    public static final int OPERATOR = 0, DIGIT = 1, LBRACKET = 2, RBRACKET = 3, POINT = 4, EQUALITY = 5;
    //运算符、数字、左括号、右括号、小数点、等号
    protected JFrame jf = new JFrame("计算器");//框架
    protected JPanel mjp = new JPanel();//主面板
    protected JTextArea jt1 = new JTextArea(), jt2 = new JTextArea();//表达式文本域、运算结果文本域
    protected JButton[] jb = {new JButton("0"), new JButton("1"), new JButton("2"), new JButton("3"),
            new JButton("4"), new JButton("5"), new JButton("6"), new JButton("7"),
            new JButton("8"), new JButton("9"), new JButton("C"), new JButton("÷"),
            new JButton("×"), new JButton("back"), new JButton("-"), new JButton("+"),
            new JButton("."), new JButton("("), new JButton(")"), new JButton("=")};//按钮
    protected String[] keyPressed = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Shift + Backspace", "/",
            "Shift + 8（即*）", "Backspace", "-", "Shift + +（即+）",
            ".", "Shift + 9（即(）", "Shift + 0（即)）", "= 或 Enter"};//帮助信息
    protected int[][] keyCode = {
            {KeyEvent.VK_0, KeyEvent.VK_NUMPAD0}, {KeyEvent.VK_1, KeyEvent.VK_NUMPAD1},
            {KeyEvent.VK_2, KeyEvent.VK_NUMPAD2}, {KeyEvent.VK_3, KeyEvent.VK_NUMPAD3},
            {KeyEvent.VK_4, KeyEvent.VK_NUMPAD4}, {KeyEvent.VK_5, KeyEvent.VK_NUMPAD5},
            {KeyEvent.VK_6, KeyEvent.VK_NUMPAD6}, {KeyEvent.VK_7, KeyEvent.VK_NUMPAD7},
            {KeyEvent.VK_8, KeyEvent.VK_NUMPAD8}, {KeyEvent.VK_9, KeyEvent.VK_NUMPAD9},
            {KeyEvent.VK_BACK_SPACE}, {KeyEvent.VK_SLASH, KeyEvent.VK_DIVIDE},
            {KeyEvent.VK_8, KeyEvent.VK_MULTIPLY}, {KeyEvent.VK_BACK_SPACE},
            {KeyEvent.VK_MINUS}, {KeyEvent.VK_EQUALS, KeyEvent.VK_ADD}, {KeyEvent.VK_PERIOD, KeyEvent.VK_DECIMAL},
            {KeyEvent.VK_9}, {KeyEvent.VK_0}, {KeyEvent.VK_ENTER, KeyEvent.VK_EQUALS}};
    protected int[] inputKey = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, InputEvent.SHIFT_MASK, 0, InputEvent.SHIFT_MASK,
            0, 0, InputEvent.SHIFT_MASK, 0, InputEvent.SHIFT_MASK, InputEvent.SHIFT_MASK, 0};//按钮对应的按键
    protected int[] chType = {DIGIT, DIGIT, DIGIT, DIGIT,
            DIGIT, DIGIT, DIGIT, DIGIT,
            DIGIT, DIGIT, -1, OPERATOR,
            OPERATOR, -1, OPERATOR, OPERATOR,
            POINT, LBRACKET, RBRACKET, EQUALITY};//按钮上字符串代表的类型，-1表示无类型
    protected JPanel[] jp = {new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel()};//面板，用于放按钮
    protected boolean[] state = new boolean[jb.length];
    //按钮的状态，true表示点击该按钮会出现相应效果（即可用），false表示点击该按钮不会出现效果（即不可用）
    protected Font f = new Font("", Font.BOLD, 25);//字体
    protected DecimalFormat df = new DecimalFormat("#.#########");//数的格式

    public Calculator() {
        //无参数的构造方法
        addKey();
        this.setFrame();
    }

    public void addKey() {
        //为按钮设置按键
        for (int i = 0; i < keyCode.length; i++) {
            for (int j = 0; j < keyCode[i].length; j++)
                if (keyCode[i][j] == KeyEvent.VK_MULTIPLY || keyCode[i][j] == KeyEvent.VK_ADD)
                    jb[i].registerKeyboardAction(this, KeyStroke.getKeyStroke(keyCode[i][j], 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
                else
                    jb[i].registerKeyboardAction(this, KeyStroke.getKeyStroke(keyCode[i][j], inputKey[i]), JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
    }

    public void setFrame() {
        //设置框架
        jf.setSize(500, 650);//设置窗口大小
        jf.setLocationRelativeTo(null);//窗口居中
        mjp.setLayout(new GridLayout(7, 1, 10, 10));//设置主面板布局
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//窗口关闭时，程序结束
        jt1.setEditable(false);
        jt2.setEditable(false);//文本域不可编辑
        jt1.setFont(f);
        jt2.setFont(f);//设置文本域字体
        jt1.setLineWrap(true);
        jt2.setLineWrap(true);//设置文本域自动换行
        mjp.add(new JScrollPane(jt1));
        mjp.add(new JScrollPane(jt2));//文本域带滚动条，并添加该滚动窗格到主面板
        for (int i = 0; i < jb.length; i++) {
            jb[i].addActionListener(this);//为按钮注册动作事件监听器
            jb[i].setFont(f);//设置按钮字体
        }
        jp[0].setLayout(new GridLayout(1, 4, 10, 0));//面板布局，添加按钮到面板
        jp[0].add(jb[10]);
        jp[0].add(jb[11]);
        jp[0].add(jb[12]);
        jp[0].add(jb[13]);
        mjp.add(jp[0]);//添加面板到主面板
        jp[1].setLayout(new GridLayout(1, 4, 10, 0));
        jp[1].add(jb[7]);
        jp[1].add(jb[8]);
        jp[1].add(jb[9]);
        jp[1].add(jb[14]);
        mjp.add(jp[1]);
        jp[2].setLayout(new GridLayout(1, 4, 10, 0));
        jp[2].add(jb[4]);
        jp[2].add(jb[5]);
        jp[2].add(jb[6]);
        jp[2].add(jb[15]);
        mjp.add(jp[2]);
        jp[3].setLayout(new GridLayout(1, 4, 10, 0));
        jp[3].add(jb[1]);
        jp[3].add(jb[2]);
        jp[3].add(jb[3]);
        jp[3].add(jb[16]);
        mjp.add(jp[3]);
        jp[4].setLayout(new GridLayout(1, 4, 10, 0));
        jp[4].add(jb[0]);
        jp[4].add(jb[17]);
        jp[4].add(jb[18]);
        jp[4].add(jb[19]);
        mjp.add(jp[4]);
        jf.add(mjp);//添加主面板到框架
        mjp.setVisible(true);//主面板可见
        jf.setVisible(true);//框架可见
        setState();//设置按钮状态
    }

    public void setState() {
        //设置按钮状态
        if (jt1.getText().isEmpty()) {
            //表达式文本域为空
            for (int i = 0; i < jb.length; i++) {
                switch (chType[i]) {
                    case RBRACKET:
                    case OPERATOR:
                    case POINT:
                    case EQUALITY:
                        state[i] = false;
                        break;
                    default:
                        state[i] = true;
                }
                if (jb[i].getText() == "-")
                    //负号可用
                    state[i] = true;
                if (jb[i].getText() == "back")
                    //退格不可用
                    state[i] = false;
            }
        } else if (jt2.getText().isEmpty()) {
            //表达式文本域不为空，运算结果文本域为空
            char ch = jt1.getText().charAt(jt1.getText().length() - 1);
            switch (JudgeCh(ch)) {
                //判断表达式最后一个字符代表的类型，根据该类型调整按钮状态
                case OPERATOR:
                    for (int i = 0; i < jb.length; i++) {
                        switch (chType[i]) {
                            case OPERATOR:
                            case RBRACKET:
                            case EQUALITY:
                            case POINT:
                                state[i] = false;
                                break;
                            default:
                                state[i] = true;
                        }
                        if (jb[i].getText() == "-" && (ch == '×' || ch == '÷'))
                            state[i] = true;
                    }
                    break;
                case DIGIT:
                    for (int i = 0; i < jb.length; i++) {
                        switch (chType[i]) {
                            case LBRACKET:
                            case POINT:
                                state[i] = false;
                                break;
                            default:
                                state[i] = true;
                        }
                        if (jb[i].getText() == ".") {
                            //字符是数字时，判断该数字是否属于一个完整数的小数部分
                            //若不属于，小数点的按钮可用;否则不可用
                            int j = jt1.getText().length() - 1;
                            for (; j >= 0 && jt1.getText().charAt(j) >= '0' && jt1.getText().charAt(j) <= '9'; j--) ;
                            if (j < 0 || jt1.getText().charAt(j) != '.')
                                state[i] = true;
                        }
                    }
                    break;
                case LBRACKET:
                    for (int i = 0; i < jb.length; i++) {
                        switch (chType[i]) {
                            case OPERATOR:
                            case RBRACKET:
                            case POINT:
                            case EQUALITY:
                                state[i] = false;
                                break;
                            default:
                                state[i] = true;
                        }
                    }
                    break;
                case RBRACKET:
                    for (int i = 0; i < jb.length; i++) {
                        switch (chType[i]) {
                            case DIGIT:
                            case LBRACKET:
                            case POINT:
                                state[i] = false;
                                break;
                            default:
                                state[i] = true;
                        }
                    }
                    break;
                case POINT:
                    for (int i = 0; i < jb.length; i++) {
                        switch (chType[i]) {
                            case LBRACKET:
                            case RBRACKET:
                            case OPERATOR:
                            case POINT:
                            case EQUALITY:
                                state[i] = false;
                                break;
                            default:
                                state[i] = true;
                        }
                    }
            }
            //若表达式不存在左括号或表达式中所有的左括号已有右括号与之匹配，则右括号的按钮不可用
            int j = 0;
            for (int i = 0; i < jt1.getText().length(); i++) {
                if (jt1.getText().charAt(i) == '(')
                    j++;
                if (jt1.getText().charAt(i) == ')')
                    j--;
            }
            if (j == 0)
                state[18] = false;
        } else {
            //表达式文本域不为空，运算结果文本域不为空
            for (int i = 0; i < jb.length; i++) {
                switch (chType[i]) {
                    case POINT:
                    case RBRACKET:
                        state[i] = false;
                        break;
                    default:
                        state[i] = true;
                }
            }
        }
        for (int i = 0; i < jb.length; i++) {
            //设置按钮颜色，可用的按钮为绿色，不可用的按钮为灰色
            if (state[i] == true)
                jb[i].setBackground(Color.GREEN);
            else jb[i].setBackground(Color.GRAY);
        }
    }

    public void actionPerformed(ActionEvent e) {
        //动作事件处理方法
        for (int i = 0; i < jb.length; i++) {
            if (e.getSource() == jb[i]) {
                switch (chType[i]) {
                    //判断按钮上字符串代表的类型
                    case -1:
                        //无类型
                        if (jb[i].getText() == "C") {
                            //清空
                            jt1.setText("");
                            jt2.setText("");
                        } else if (jb[i].getText() == "back") {
                            //退格
                            if (state[i] == true)
                                jt1.setText(jt1.getText().substring(0, jt1.getText().length() - 1));
                            if (!jt2.getText().isEmpty())
                                jt2.setText("");
                        }
                        break;
                    case EQUALITY:
                        //等号
                        if (state[i] == true) {
                            int k = 0;
                            for (int j = 0; j < jt1.getText().length(); j++) {
                                if (jt1.getText().charAt(j) == '(')
                                    k++;
                                if (jt1.getText().charAt(j) == ')')
                                    k--;
                            }
                            for (; k > 0; k--)
                                //补上缺少的右括号
                                jt1.setText(jt1.getText() + ")");
                            jt2.setText(df.format(new Calculate(jt1.getText()).calculate()));
                            jt1.setText(jt1.getText() + "=");
                        }
                        break;
                    default:
                        //其他类型
                        if (!jt2.getText().isEmpty() && state[i] == true) {
                            //运算结果文本域不为空且按钮可用
                            if (chType[i] == OPERATOR)
                                //若是运算符，连接到运算结果，将其置于表达式文本域
                                jt1.setText(jt2.getText() + jb[i].getText());
                            else
                                //否则，设置表达式文本域的内容为该按钮上的字符串
                                jt1.setText(jb[i].getText());
                            jt2.setText("");//清空运算结果文本域
                        } else if (jt2.getText().isEmpty() && state[i] == true)
                            //运算结果文本域为空且按钮可用，将按钮上的字符串连接到表达式文本域的内容
                            jt1.setText(jt1.getText() + jb[i].getText());
                }
                break;
            }
        }
        setState();//设置按钮状态
    }

    public int JudgeCh(char ch) {
        //判断字符类型
        if (ch >= '0' && ch <= '9') return DIGIT;
        else if (ch == '+' || ch == '-' || ch == '×' || ch == '÷')
            return OPERATOR;
        else if (ch == '(') return LBRACKET;
        else if (ch == ')') return RBRACKET;
        else return POINT;
    }

}
