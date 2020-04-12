package com.spring.model;

/**
 * Created by bdurmusoglu on 6.04.2020.
 */

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
@Entity
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

    @JsonIgnoreProperties("restaurant")
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Review> restaurant;

    @Column
    private String restaurantImageUrl;


}
