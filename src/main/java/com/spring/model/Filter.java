package com.spring.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Filter {
    private double virus;
    private double score4_5;
    private int reviewPopularity;
    private FilterPlace filters;

}
