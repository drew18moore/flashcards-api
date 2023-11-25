package com.drewm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrueFalseQuestion extends Question {
    private String option;
    private Boolean answer;
}
