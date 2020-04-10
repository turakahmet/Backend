package com.spring.model;

/**
 * Created by bdurmusoglu on 6.04.2020.
 */

import javax.persistence.*;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
@Entity
public class Review {


    @Id
    @GeneratedValue
    private int reviewID;


    @ManyToOne
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "userID")
    private AppUser user;

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
    @Column
    private ReviewScore reviewRestaurantScore;


}