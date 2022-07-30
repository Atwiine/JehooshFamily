package com.example.jehooshfamily.ui.Models;

public class HistoryObjectives_Model {

    String id, question, options_a, options_b, options_c, options_d, options_e, answer_boss, date;

    public HistoryObjectives_Model() {
    }

    public HistoryObjectives_Model(String id, String question, String options_a, String options_b, String options_c,
                                   String options_d, String options_e, String answer_boss, String date) {
        this.id = id;
        this.question = question;
        this.options_a = options_a;
        this.options_b = options_b;
        this.options_c = options_c;
        this.options_d = options_d;
        this.options_e = options_e;
        this.answer_boss = answer_boss;
        this.date = date;
    }

    public String getAnswer_boss() {
        return answer_boss;
    }

    public void setAnswer_boss(String answer_boss) {
        this.answer_boss = answer_boss;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
