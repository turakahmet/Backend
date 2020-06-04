package com.spring.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by bdurmusoglu on 25.05.2020.
 */
@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cityID;

    @Column
    private String cityName;

    @OneToMany(mappedBy = "cityID", cascade = { CascadeType.ALL },orphanRemoval = true)
    private Set<Town> towns;

    @OneToMany(mappedBy = "cityID", cascade = { CascadeType.ALL },orphanRemoval = true)
    private Set<Restaurant> rCityID;

    @OneToMany(mappedBy = "cityID", cascade = { CascadeType.ALL },orphanRemoval = true)
    private Set<UserRecords> uCityID;

}
