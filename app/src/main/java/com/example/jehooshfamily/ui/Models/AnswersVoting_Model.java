package com.example.jehooshfamily.ui.Models;

public class AnswersVoting_Model {

    String id, answer_employee, from, question_sent, sent_qn_id, options_a, options_b, options_c, options_d, options_e, boss_id, boss_name, date_submission;

    public AnswersVoting_Model() {
    }

    public AnswersVoting_Model(String id,
                               String answer_employee,
                               String from,
                               String question_sent,
                               String sent_qn_id,
                               String options_a,
                               String options_b,
                               String options_c,
                               String options_d,
                               String options_e,
                               String boss_id,
                               String boss_name,
                               String date_submission) {
        this.id = id;
        this.answer_employee = answer_employee;
        this.from = from;
        this.question_sent = question_sent;
        this.sent_qn_id = sent_qn_id;
        this.options_a = options_a;
        this.options_b = options_b;
        this.options_c = options_c;
        this.options_d = options_d;
        this.options_e = options_e;
        this.boss_id = boss_id;
        this.boss_name = boss_name;
        this.date_submission = date_submission;
    }

    public String getSent_qn_id() {
        return sent_qn_id;
    }

    public void setSent_qn_id(String sent_qn_id) {
        this.sent_qn_id = sent_qn_id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer_employee() {
        return answer_employee;
    }

    public void setAnswer_employee(String answer_employee) {
        this.answer_employee = answer_employee;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getQuestion_sent() {
        return question_sent;
    }

    public void setQuestion_sent(String question_sent) {
        this.question_sent = question_sent;
    }

    public String getBoss_id() {
        return boss_id;
    }

    public void setBoss_id(String boss_id) {
        this.boss_id = boss_id;
    }

    public String getBoss_name() {
        return boss_name;
    }

    public void setBoss_name(String boss_name) {
        this.boss_name = boss_name;
    }

    public String getDate_submission() {
        return date_submission;
    }

    public void setDate_submission(String date_submission) {
        this.date_submission = date_submission;
    }
}

