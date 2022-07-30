package com.example.jehooshfamily.ui.Models;

public class DrawQns_Model {

    String id, question, date;

    public DrawQns_Model() {
    }

    public DrawQns_Model(String id, String question, String date) {
        this.id = id;
        this.question = question;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
