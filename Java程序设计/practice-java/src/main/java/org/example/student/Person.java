package org.example.student;

import org.example.Date;

/**
 * 人
 *
 * @author obby-xiang
 * @since 2016-11-03
 */
public class Person {
    private static int count = 0;
    public String name;
    public Date birthday;
    public String sex, province, city;

    public Person(String name, Date birthday, String sex, String province, String city) {
        this.set(name, birthday, sex, province, city);
        count++;
    }

    public Person(String name, Date birthday) {
        this(name, birthday, "", "", "");
    }

    public Person() {
        this("", null);
    }

    public Person(Person p) {
        this(p.name, new Date(p.birthday), p.sex, p.province, p.city);
    }

    public static void howMany() {
        System.out.println(Person.count + "个Person对象，");
    }

    public void finalize() {
        System.out.println("释放对象(" + this + ")");
        Person.count--;
    }

    public void set(String name, Date birthday, String sex, String province, String city) {
        this.name = name == null ? "" : name;
        this.birthday = birthday;
        this.sex = sex == null ? "" : sex;
        this.province = province == null ? "" : province;
        this.city = city == null ? "" : city;
    }

    public String toString() {
        return name + "," + (birthday == null ? "" : birthday.toString()) + "," + sex + "," + province + "," + city;
    }
}
