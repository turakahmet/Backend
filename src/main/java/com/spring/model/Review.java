package com.spring.model;

/**
 * Created by bdurmusoglu on 6.04.2020.
 */

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table
public class Review {


    @Id
    @GeneratedValue
    private int reviewID;


    @ManyToOne
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurantID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private AppUser userID;

    @Column
    private int hygiene1;

    @Column
    private int hygiene2;

    @Column
    private int hygiene3;

    @Column
    private int child_friendly_1;

    @Column
    private int child_friendly_2;

    @Column
    private int child_friendly_3;

    @Column
    private int disabled_friendly1;

    @Column
    private int disabled_friendly2;

    @Column
    private int disabled_friendly3;

    @Embedded
    @Column(nullable = true)
    private ReviewScore reviewRestaurantScore;

    public Review() {
        super();
    }

    public Review(Restaurant restaurantID, AppUser userID, int hygiene1, int hygiene2, int hygiene3, int child_friendly_1, int child_friendly_2, int child_friendly_3, int disabled_friendly1, int disabled_friendly2, int disabled_friendly3, ReviewScore reviewRestaurantScore) {
        super();
        this.restaurantID = restaurantID;
        this.userID = userID;
        this.hygiene1 = hygiene1;
        this.hygiene2 = hygiene2;
        this.hygiene3 = hygiene3;
        this.child_friendly_1 = child_friendly_1;
        this.child_friendly_2 = child_friendly_2;
        this.child_friendly_3 = child_friendly_3;
        this.disabled_friendly1 = disabled_friendly1;
        this.disabled_friendly2 = disabled_friendly2;
        this.disabled_friendly3 = disabled_friendly3;
        this.reviewRestaurantScore = reviewRestaurantScore;
    }
}