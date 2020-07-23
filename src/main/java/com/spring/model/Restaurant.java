package com.spring.model;

/**
 * Created by bdurmusoglu on 6.04.2020.
 */

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table
public class Restaurant {

    @Id
    @Column
    private long restaurantID;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String address;


    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column
    private String cuisines;

    @Column
    private String timings;

    @Column
    private String category;

    @Column
    private String phone_number;

    @Column
    private long review_count;

    @ColumnDefault("0")
    private double average_review;

    @ColumnDefault("0")
    private double hygiene_review;

    @ColumnDefault("0")
    private double friendly_review;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private Set<Review> review;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private Set<Report> reports;

    @Column
    private String restaurantImageUrl;

    @Lob
    @Column
    private byte[] restaurantImageBlob;

    //@JsonIgnoreProperties("cityID")
    @ManyToOne
    @JoinColumn(name = "cityID")
    private City cityID;

    //@JsonIgnoreProperties("townID")
    @ManyToOne
    @JoinColumn(name = "townID")
    private Town townID;

    @ColumnDefault("1")
    private int HygieneArrow;

    @ColumnDefault("1")
    private int CleaningArrow;

}
