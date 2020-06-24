package com.spring.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class Compare {
    private double newValueHygiene;
    private double newValueCleaning;

    public Compare(double newValueHygiene, double newValueCleaning) {
        this.newValueHygiene = newValueHygiene;
        this.newValueCleaning = newValueCleaning;
    }

    public Compare(ArrayList<Compare> body) {

    }

    public Compare() {

    }
}
