package com.spring.model;

/**
 * Created by bdurmusoglu on 6.04.2020.
 */

import javax.persistence.*;

import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "userID")
    private AppUser user;

    @Column
    private double hygiene1;

    @Column
    private double hygiene2;

    @Column
    private double hygiene3;

    @Column
    private double child_friendly_1;

    @Column
    private double child_friendly_2;

    @Column
    private double child_friendly_3;

    @Column
    private double disabled_friendly1;

    @Column
    private double disabled_friendly2;

    @Column
    private double disabled_friendly3;

    @Temporal(TemporalType.DATE)
    private Date reviewDate;

}