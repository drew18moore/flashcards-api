package com.drewm.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultipleChoiceQuestion extends Question {
    private List<String> options;
    private Integer answer;
}
