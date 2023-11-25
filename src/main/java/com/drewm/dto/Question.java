package com.drewm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Question {
    private String questionText;
    private QuestionType questionType;
}
