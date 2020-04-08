package com.spring.model;

/**
 * Created by bdurmusoglu on 6.04.2020.
 */

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "RESTAURANT")
public class Restaurant  {


    @GeneratedValue
    @Id
    @Column
    private long restaurantID;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String locality;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private int latitude;

    @Column(nullable = false)
    private int longitude;

    @Column
    private String locality_verbose;

    @Column
    private String cuisines;

    @Column
    private String timings;

    @Column
    private int phone_number;

    @Column
    private int review_count;

    @Embedded
    @Column
    private ReviewScore reviewScore;

    @Column
    private String restaurantImageUrl;

    public Restaurant() {
        super();
    }

    public Restaurant(String restaurantName, String address, String locality, String city, int latitude, int longitude, String locality_verbose, String cuisines, String timings, int phone_number, String restaurantImageUrl) {
        super();
        this.restaurantName = restaurantName;
        this.address = address;
        this.locality = locality;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locality_verbose = locality_verbose;
        this.cuisines = cuisines;
        this.timings = timings;
        this.phone_number = phone_number;
        this.restaurantImageUrl = restaurantImageUrl;
    }
}
