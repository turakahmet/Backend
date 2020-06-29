package com.spring.model;

/**
 * Created by bdurmusoglu on 6.04.2020.
 */

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
@Entity
public class Review {


    @Id
    @GeneratedValue
    private long reviewID;

    //@JsonIgnoreProperties("restaurantID")
    @ManyToOne
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurant;

   // @JsonIgnoreProperties("userID")
    @ManyToOne
    @JoinColumn(name = "userID")
    private AppUser user;

    @ColumnDefault("0")
    private double question1;

    @ColumnDefault("0")
    private double question2;

    @ColumnDefault("0")
    private double question3;

    @ColumnDefault("0")
    private double question4;

    @ColumnDefault("0")
    private double question5;

    @ColumnDefault("0")
    private double question6;

    @ColumnDefault("0")
    private double question7;

    @ColumnDefault("0")
    private double question8;

    @ColumnDefault("0")
    private double question9;

    @ColumnDefault("0")
    private double average;

    @ColumnDefault("0")
    private double hygieneAverage;

    @ColumnDefault("0")
    private double friendlyAverage;

    @JsonFormat(pattern="dd-MM-yyyy")
    @UpdateTimestamp
    @Temporal(TemporalType.DATE)
    private Date reviewDate;

}