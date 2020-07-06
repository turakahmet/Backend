package com.spring.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FilterPlace {
    private String sort;
    private String covid;
    private double point;
    private int distance;
}
