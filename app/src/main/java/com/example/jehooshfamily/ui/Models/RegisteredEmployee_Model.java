package com.example.jehooshfamily.ui.Models;

public class RegisteredEmployee_Model {

    String id, names, phone, email, role, boss_name, date_register;

    public RegisteredEmployee_Model() {
    }

    public RegisteredEmployee_Model(String id, String names, String phone, String email, String role, String boss_name, String date_register) {
        this.id = id;
        this.names = names;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.boss_name = boss_name;
        this.date_register = date_register;


    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate_register() {
        return date_register;
    }

    public void setDate_register(String date_register) {
        this.date_register = date_register;
    }
}
