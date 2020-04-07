package com.spring.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by bdurmusoglu on 7.04.2020.
 */
@Getter
@Setter
@Embeddable
public class ReviewScore {
    @ColumnDefault("0")
    private double average_review;

    @ColumnDefault("0")
    private double hygiene_review;
    @ColumnDefault("0")
    private double child_friendly_review;
    @ColumnDefault("0")
    private double disabled_friendly_review;

    public ReviewScore() {
        super();
    }

    public ReviewScore(double average_review, double hygiene_review, double child_friendly_review, double disabled_friendly_review) {
        super();
        this.average_review = average_review;
        this.hygiene_review = hygiene_review;
        this.child_friendly_review = child_friendly_review;
        this.disabled_friendly_review = disabled_friendly_review;
    }
}
