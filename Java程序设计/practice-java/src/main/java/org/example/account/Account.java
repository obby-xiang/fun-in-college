package org.example.account;

import org.example.Date;

/**
 * 银行账户
 *
 * @author obby-xiang
 * @since 2016-10-29
 */
public class Account {
    private static int count = 0;//本类及子类实例计数
    private final String accountNumber;
    private final String name;
    private final String id;
    private final Date date;//注册日期
    private String password;
    private String record = "";//账号、密码、姓名、身份证号码、消费明细
    private float remain;//账户余额

    public Account(String name, String id) {
        //以姓名和身份证号码构造对象，密码默认为000000
        this(name, id, "000000");//调用本类构造方法
    }

    public Account(String name, String id, String password) {
        //以姓名、身份证号和密码构造对象
        this.name = name;
        this.id = id;
        this.password = password;
        accountNumber = "123456789" + String.format("%03d", count);//账号
        date = new Date();//开户日期为开户当天的日期
        remain = 0;//初始余额为0
        count++;//用户人数增1
    }

    public static int getCount() {
        //获得用户人数
        return count;
    }

    public String getAccountNumber() {
        //获得账号
        return accountNumber;
    }

    protected String getPassword() {
        //获得密码
        return password;
    }

    protected void setPassword(String password) {
        //设置密码
        this.password = password;
    }

    public String getName() {
        //获得姓名
        return name;
    }

    public String getId() {
        //获得身份证号码
        return id;
    }

    public Date getDate() {
        //获得开户日期
        return date;
    }

    public float getRemain() {
        //获得余额
        return remain;
    }

    protected void setRemain(float remain) {
        //设置余额
        this.remain = remain;
    }

    public String getRecord() {
        //获得明细
        return record;
    }

    protected void setRecord(String record) {
        //设置明细
        this.record += (record + "\n");
    }

    public void finalize() {
        //析构方法
        count--;
    }

    public String toString() {
        //账户信息
        return "账号:" + accountNumber + "   开户时间:" + date.toString() + "   姓名:" + name + "   身份证号码:" + id;
    }
}
