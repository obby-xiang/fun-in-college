package org.example.student;

import org.example.Date;

/**
 * 学生
 *
 * @author obby-xiang
 * @since 2016-11-03
 */
public class Student extends Person {
    public static String[] Department = {"机电与信息工程学院", "海洋学院"};//院系
    public static String[][] Speciality = {{"数字媒体技术专业", "电子信息科学与技术专业"},
            {"生物科学专业"}};//每个院系的专业
    public static int[][] num = {{0, 0}, {0}};
    private static int count = 0;//Student类对象计数
    public String department;
    public String speciality;
    public String number;
    public String grade;
    public String[] subject;//院系、专业、学号、年级、学科
    public double[] score;//各科成绩
    public boolean member;//团员

    public Student(String name, Date birthday, String sex, String province, String city,
                   String grade, String department, String speciality, boolean member) {
        //构造方法
        super(name, birthday, sex, province, city);
        this.set(grade, department, speciality, member);
        count++;
    }

    public Student() {
        //无参数的构造方法
        this("", new Date(), "", "", "", "", "", "", false);
    }

    public Student(Person p, String grade, String department, String speciality, boolean member) {
        //构造方法
        this(p.name, new Date(p.birthday), p.sex, p.province, p.city, grade, department, speciality, member);
    }

    public Student(Student s) {
        //以实例构造对象
        this(s.name, new Date(s.birthday), s.sex, s.province, s.city, s.grade, s.department, s.speciality, s.member);
    }

    public static void howMany() {
        //获得Student类及子类的对象数量
        Person.howMany();
        System.out.println(Student.count + "个Student对象");
    }

    public static void main(String[] args) {
        Student s1 = new Student("张三", new Date(1997, 06, 28), "男", "山东省", "威海市", "2015级", "机电与信息工程学院", "数字媒体技术专业", true);
        String[] subject = {"java", "数据库系统", "数据结构", "概率论", "体育"};
        double[] score = {86, 90, 85, 92, 80};
        s1.setScore(subject, score);
        System.out.println(s1 + "\n");
        Student s2 = new Student();
        s2.set("李四", new Date(1998, 07, 12), "女", "江西省", "赣州市");
        s2.set("2016级", "机电与信息工程学院", "数字媒体技术专业", true);
        String[] subject1 = {"高等数学", "大学英语", "体育"};
        double[] score1 = {100, 98, 92};
        s2.setScore(subject1, score1);
        System.out.println(s2 + "\n");
        Student s3 = new Student();
        s3.set("王五", new Date(1998, 12, 12), "女", "广东省", "广州市");
        s3.set("2016级", "海洋学院", "生物科学专业", true);
        String[] subject2 = {"高等数学", "大学英语", "体育"};
        double[] score2 = {90, 89, 92};
        s3.setScore(subject2, score2);
        System.out.println(s3);
    }

    private String setNumber() {
        //自动编学号
        String s = "";
        int i, j;
        if (grade == "") s += "9999";//年级为空
        else s += String.format("%04d", Integer.parseInt(grade.substring(0, 4), 10));//学号以年级开头
        for (i = 0; i < Department.length; i++)
            if (department.equals(Department[i])) {
                //接着按学院编号
                s += String.format("%03d", i + 1);
                break;
            }
        if (i == Department.length) s += "000000000";//学院不存在的情况下取默认值
        else {
            for (j = 0; j < Speciality[i].length; j++)
                if (speciality.equals(Speciality[i][j])) {
                    //按专业编号
                    s += String.format("%03d", j + 1);
                    num[i][j]++;
                    s += String.format("%04d", num[i][j]);
                    break;
                }
            if (j == Speciality[i].length) s += "0000";
        }
        return s;
    }

    public void setScore(String[] subject, double[] score) {
        //设置各科成绩
        this.subject = subject;
        this.score = score;
    }

    public String getScore() {
        //获得成绩信息，包括总分及平均分
        String s = "各科成绩:";
        if (subject == null || score == null) return s;
        else {
            double m = 0.0;
            int i;
            for (i = 0; i < subject.length && i < score.length; i++) {
                s += (subject[i] + ":" + String.format("%.2f", score[i]) + ",");
                m += score[i];
            }
            if (i != 0) return s + String.format("总分%.2f,平均分%.2f", m, m / i);
            else return s;
        }
    }

    public void finalize() {
        //析构方法
        super.finalize();
        Student.count--;
    }

    public void set(String grade, String department, String speciality, boolean member) {
        //设置学生信息
        this.grade = grade == null ? "" : grade;
        this.department = department == null ? "" : department;
        this.speciality = speciality == null ? "" : speciality;
        this.number = this.setNumber();
        this.member = member;
    }

    public String toString() {
        return super.toString() + "," + grade + "," + department + "," + speciality + "," + number + (member ? ",团员" : "") + "\n" + getScore();
    }
}
/*
程序运行结果:
张三,1997年06月28日,男,山东省,威海市,2015级,机电与信息工程学院,数字媒体技术专业,20150010010001,团员
各科成绩:java:86.00,数据库系统:90.00,数据结构:85.00,概率论:92.00,体育:80.00,总分433.00,平均分86.60

李四,1998年07月12日,女,江西省,赣州市,2016级,机电与信息工程学院,数字媒体技术专业,20160010010002,团员
各科成绩:高等数学:100.00,大学英语:98.00,体育:92.00,总分290.00,平均分96.67

王五,1998年12月12日,女,广东省,广州市,2016级,海洋学院,生物科学专业,20160020010001,团员
各科成绩:高等数学:90.00,大学英语:89.00,体育:92.00,总分271.00,平均分90.33
*/
