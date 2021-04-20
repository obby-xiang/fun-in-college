package org.example.account;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 操作银行账户
 *
 * @author obby-xiang
 * @since 2016-11-03
 */
public class Operate {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");//时间格式
    public static DecimalFormat df = new DecimalFormat("0.00");//小数格式

    public static Account openAccount(String name, String id, String password) {
        //以姓名、身份证号码和密码开户
        System.out.println("开户(姓名:" + name + "   身份证号码:" + id + ")");
        if (name.length() == 0) {
            //姓名为空
            System.out.println("操作结果:没有设置姓名，开户失败");
            return null;
        } else if (id.length() == 0) {
            //省份证号码为空
            System.out.println("操作结果:没有设置身份证号码，开户失败");
            return null;
        } else if (password.length() == 0) {
            //密码为空
            System.out.println("操作结果:没有设置密码，开户失败");
            return null;
        } else {
            Account account = new Account(name, id, password);
            System.out.println("操作结果:开户成功   账号:" + account.getAccountNumber());
            return account;
        }
    }

    public static Account openAccount(String name, String id) {
        //以姓名、身份证号码开户，密码默认为000000
        return openAccount(name, id, "000000");//调用本类方法
    }

    public static void getInformation(Account account, String password) {
        //获取账户信息
        System.out.println("查看账户信息(账户:" + account.getAccountNumber() + ")");
        if (password.equals(account.getPassword()) == false)
            System.out.println("操作结果:密码错误，查看账户信息失败");//密码错误
        else
            System.out.println("操作结果:查看账户信息成功   " + account);//输出账户信息
    }

    public static void cancelAccount(Account account, String password) {
        //销户
        System.out.println("销户(账户:" + account.getAccountNumber() + ")");
        if (password.equals(account.getPassword()) == false)
            System.out.println("操作结果:密码错误，销户失败");//密码错误
        else {
            if (account.getRemain() > 0)
                System.out.println("操作结果:账户内剩有余额，销户失败   余额:" + df.format(account.getRemain()) + "元");//账户内剩有余额
            else {
                account.finalize();
                System.out.println("操作结果:销户成功");
            }
        }
    }

    public static void deposit(Account account, String password, double m) {
        //存款
        float money;
        money = Float.valueOf(df.format(m));
        System.out.println("存款(账户:" + account.getAccountNumber() + "   金额:" + df.format(money) + "元)");
        if (password.equals(account.getPassword()) == false) {
            System.out.println("操作结果:密码错误，存款失败");//密码错误
        } else if (money <= 0)
            System.out.println("操作结果:存款金额有误，存款失败   余额:" + df.format(account.getRemain()) + "元");//存款金额有误
        else {
            account.setRemain(account.getRemain() + money);
            System.out.println("操作结果:存款成功   余额:" + df.format(account.getRemain()) + "元");
            account.setRecord(dateFormat.format(new Date()) + "   存款:" + df.format(money) + "元" + "   余额:" + df.format(account.getRemain()) + "元");
        }
    }

    public static void withdraw(Account account, String password, double m) {
        //取款
        float money;
        money = Float.valueOf(df.format(m));
        System.out.println("取款(账户:" + account.getAccountNumber() + "   金额:" + df.format(money) + "元)");
        if (password.equals(account.getPassword()) == false) {
            System.out.println("操作结果:密码错误，取款失败");//密码错误
        } else if (money <= 0)
            System.out.println("操作结果:取款金额有误，取款失败   余额:" + df.format(account.getRemain()) + "元");//取款金额有误
        else if (money > account.getRemain())
            System.out.println("操作结果:余额不足，取款失败   余额:" + df.format(account.getRemain()) + "元");//余额不足
        else {
            account.setRemain(account.getRemain() - money);
            System.out.println("操作结果:取款成功   余额:" + df.format(account.getRemain()) + "元");
            account.setRecord(dateFormat.format(new Date()) + "   取款:" + df.format(money) + "元" + "   余额:" + df.format(account.getRemain()) + "元");
        }
    }

    public static void getRecord(Account account, String password) {
        //查询明细
        System.out.println("查询明细(账户:" + account.getAccountNumber() + ")");
        if (password.equals(account.getPassword()) == false)
            System.out.println("操作结果:密码错误，取款失败");//密码错误
        else System.out.println("操作结果:\n" + account.getRecord());
    }

    public static void getRemain(Account account, String password) {
        //查询余额
        System.out.println("查询余额(账户:" + account.getAccountNumber() + ")");
        if (password.equals(account.getPassword()) == false)
            System.out.println("操作结果:密码错误，取款失败");//密码错误
        else System.out.println("操作结果:" + df.format(account.getRemain()) + "元");
    }

    public static void revisePassword(Account account, String password1, String password2) {
        //修改密码
        System.out.println("修改密码(账户:" + account.getAccountNumber() + ")");
        if (password1.equals(account.getPassword()) == false)
            System.out.println("操作结果:原密码错误，修改密码失败");//密码错误
        else {
            if (password2.length() == 0) System.out.println("操作结果:没有设置修改后的密码，修改失败");//没有设置修改后的密码
            else {
                account.setPassword(password2);
                System.out.println("操作结果:修改密码成功");
            }
        }
    }

    public static void main(String[] args) {
        Account account = Operate.openAccount("张三", "9876543210");
        Operate.getInformation(account, "000000");//查看账户信息
        Operate.revisePassword(account, "000000", "123456");//修改密码
        Operate.getRemain(account, "000000");//用原密码查询余额
        Operate.getRemain(account, "123456");//用修改后的密码查询余额
        Operate.deposit(account, "123456", 100);//存款100
        Operate.withdraw(account, "123456", 11.11);//取款11.11
        Operate.getRecord(account, "123456");//查询明细
        Operate.getRemain(account, "123456");//查询余额
        Operate.withdraw(account, "123456", account.getRemain());//取出账户内剩有的余额
        Operate.cancelAccount(account, "123456");//销户
    }
}
/*
程序运行结果:
开户(姓名:张三   身份证号码:9876543210)
操作结果:开户成功   账号:123456789000
查看账户信息(账户:123456789000)
操作结果:查看账户信息成功   账号:123456789000   开户时间:2016年11月 3日   姓名:张三   身份证号码:9876543210
修改密码(账户:123456789000)
操作结果:修改密码成功
查询余额(账户:123456789000)
操作结果:密码错误，取款失败
查询余额(账户:123456789000)
操作结果:0.00元
存款(账户:123456789000   金额:100.00元)
操作结果:存款成功   余额:100.00元
取款(账户:123456789000   金额:11.11元)
操作结果:取款成功   余额:88.89元
查询明细(账户:123456789000)
操作结果:
2016年11月03日 16:58   存款:100.00元   余额:100.00元
2016年11月03日 16:58   取款:11.11元   余额:88.89元

查询余额(账户:123456789000)
操作结果:88.89元
取款(账户:123456789000   金额:88.89元)
操作结果:取款成功   余额:0.00元
销户(账户:123456789000)
操作结果:销户成功
*/
