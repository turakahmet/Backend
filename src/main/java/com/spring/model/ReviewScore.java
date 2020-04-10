package com.spring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by bdurmusoglu on 7.04.2020.
 */

@AllArgsConstructor
@NoArgsConstructor
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


}
