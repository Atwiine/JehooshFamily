package com.example.jehooshfamily.ui.Models;

public class Feedback_Model {

    String id;
    String question;
    String feedback;
    String options_a;
    String options_b;
    String options_c;
    String options_d;
    String options_e;
    String answer_user;
    String user_name;
    String user_id;
    String date;


    public Feedback_Model() {
    }

    public Feedback_Model(String id,
                          String question,
                          String feedback,
                          String options_a,
                          String options_b,
                          String options_c,
                          String options_d,
                          String options_e,
                          String answer_user,
                          String user_name,
                          String user_id,
                          String date) {
        this.id = id;
        this.question = question;
        this.feedback = feedback;
        this.options_a = options_a;
        this.options_b = options_b;
        this.options_c = options_c;
        this.options_d = options_d;
        this.options_e = options_e;
        this.answer_user = answer_user;
        this.user_name = user_name;
        this.user_id = user_id;
        this.date = date;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptions_a() {
        return options_a;
    }

    public void setOptions_a(String options_a) {
        this.options_a = options_a;
    }

    public String getOptions_b() {
        return options_b;
    }

    public void setOptions_b(String options_b) {
        this.options_b = options_b;
    }

    public String getOptions_c() {
        return options_c;
    }

    public void setOptions_c(String options_c) {
        this.options_c = options_c;
    }

    public String getOptions_d() {
        return options_d;
    }

    public void setOptions_d(String options_d) {
        this.options_d = options_d;
    }

    public String getOptions_e() {
        return options_e;
    }

    public void setOptions_e(String options_e) {
        this.options_e = options_e;
    }

    public String getAnswer_user() {
        return answer_user;
    }

    public void setAnswer_user(String answer_user) {
        this.answer_user = answer_user;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
