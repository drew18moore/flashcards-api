package com.drewm.dto;

import java.util.List;

// Question.java (a simple class to represent different question types)
public class TestQuestionDTO {

    private String question;

    // For multiple-choice questions
    private String[] choices;

    // For true/false questions
    private Boolean answerTF;

    // For written questions
    private String answerWritten;

    // Constructors, getters, and setters (you can generate these using your IDE)

    // Example constructors:
    public TestQuestionDTO(String question, String[] choices, String answer) {
        this.question = question;
        this.choices = choices;
        this.answerWritten = answer;
    }

    public TestQuestionDTO(String question, Boolean answer) {
        this.question = question;
        this.answerTF = answer;
    }

    public TestQuestionDTO(String question, String answer) {
        this.question = question;
        this.answerWritten = answer;
    }
}

