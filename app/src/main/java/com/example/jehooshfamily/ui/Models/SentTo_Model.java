package com.example.jehooshfamily.ui.Models;

public class SentTo_Model {

    String id, names, phone, role, boss_name, date_register;

    public SentTo_Model() {
    }

    public SentTo_Model(String id, String names, String phone, String role, String boss_name, String date_register) {
        this.id = id;
        this.names = names;
        this.phone = phone;
        this.role = role;
        this.boss_name = boss_name;
        this.date_register = date_register;


    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBoss_name() {
        return boss_name;
    }

    public void setBoss_name(String boss_name) {
        this.boss_name = boss_name;
    }

    public String getDate_register() {
        return date_register;
    }

    public void setDate_register(String date_register) {
        this.date_register = date_register;
    }
}
