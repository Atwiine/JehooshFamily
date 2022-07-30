package com.example.jehooshfamily.ui.Models;

public class AnswersEssay_Model {

    String id, answer, from_who, user_id, sent_question, sent_qn_id, boss_name, boss_id, date_submission;

    public AnswersEssay_Model() {
    }


    public AnswersEssay_Model(String id, String answer, String from_who, String user_id, String sent_question, String sent_qn_id, String boss_name, String boss_id, String date_submission) {
        this.id = id;
        this.answer = answer;
        this.user_id = user_id;
        this.from_who = from_who;
        this.sent_question = sent_question;
        this.sent_qn_id = sent_qn_id;
        this.boss_name = boss_name;
        this.boss_id = boss_id;
        this.date_submission = date_submission;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getFrom_who() {
        return from_who;
    }

    public void setFrom_who(String from_who) {
        this.from_who = from_who;
    }

    public String getSent_question() {
        return sent_question;
    }

    public void setSent_question(String sent_question) {
        this.sent_question = sent_question;
    }

    public String getSent_qn_id() {
        return sent_qn_id;
    }

    public void setSent_qn_id(String sent_qn_id) {
        this.sent_qn_id = sent_qn_id;
    }

    public String getBoss_name() {
        return boss_name;
    }

    public void setBoss_name(String boss_name) {
        this.boss_name = boss_name;
    }

    public String getBoss_id() {
        return boss_id;
    }

    public void setBoss_id(String boss_id) {
        this.boss_id = boss_id;
    }

    public String getDate_submission() {
        return date_submission;
    }

    public void setDate_submission(String date_submission) {
        this.date_submission = date_submission;
    }
}
