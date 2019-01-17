package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by Eriq on 3/25/2018.
 */

public class FAQ {
    String question, answer;

    public FAQ() {}

    public FAQ(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
