package com.cl.testapp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户信息
 * Created by Administrator on 2017-02-24.
 */
@Entity
public class User {

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private int age;
    private int sex;
    private String info;
    private boolean isDel;

    @Generated(hash = 586692638)
    public User() {
    }

    public User(String name, int age, int sex, String info) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.info = info;
    }

    public User(String name, int age, int sex, String info, boolean isDel) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.info = info;
        this.isDel = isDel;
    }

    @Generated(hash = 79701715)
    public User(Long id, String name, int age, int sex, String info,
            boolean isDel) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.info = info;
        this.isDel = isDel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }

    public boolean getIsDel() {
        return this.isDel;
    }

    public void setIsDel(boolean isDel) {
        this.isDel = isDel;
    }
}
