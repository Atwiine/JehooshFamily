package com.example.jehooshfamily.ui.Models;

public class FeedbackEssay_Model {

    String id;
    String question;
    String feedback;
    String answer_user;
    String user_name;
    String user_id;
    String date;

    public FeedbackEssay_Model() {
    }

    public FeedbackEssay_Model(String id,
                               String question,
                               String feedback,
                               String answer_user,
                               String user_name,
                               String user_id,
                               String date) {
        this.id = id;
        this.question = question;
        this.feedback = feedback;
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
