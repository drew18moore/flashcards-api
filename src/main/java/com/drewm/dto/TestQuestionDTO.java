package com.drewm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

// Question.java (a simple class to represent different question types)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestQuestionDTO {

    private String question;

    // For multiple-choice questions
    private String[] choices;
    private Integer answerMC;

    // For true/false questions
    private String choiceTF;
    private Boolean answerTF;

    // For written questions
    private String answerWritten;

    // Constructors, getters, and setters (you can generate these using your IDE)

    // Example constructors:
    public TestQuestionDTO(String question, String[] choices, Integer answer) {
        this.question = question;
        this.choices = choices;
        this.answerMC = answer;
    }

    public TestQuestionDTO(String question, String choiceTF, Boolean answer) {
        this.question = question;
        this.choiceTF = choiceTF;
        this.answerTF = answer;
    }

    public TestQuestionDTO(String question, String answer) {
        this.question = question;
        this.answerWritten = answer;
    }

    @Override
    public String toString() {
        return "TestQuestionDTO{" +
                "question='" + question + '\'' +
                ", choices=" + Arrays.toString(choices) +
                ", answerMC=" + answerMC +
                ", choiceTF='" + choiceTF + '\'' +
                ", answerTF=" + answerTF +
                ", answerWritten='" + answerWritten + '\'' +
                '}';
    }
}

