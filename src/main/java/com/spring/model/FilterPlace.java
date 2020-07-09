package com.spring.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FilterPlace {
    private int sort;
    private List<String> category;
    private double point;
    private int distance;
}
