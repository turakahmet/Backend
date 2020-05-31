package com.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Town {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long townID;

    @Column
    private String townName;

    @JsonIgnoreProperties("cityID")
    @ManyToOne
    @JoinColumn(name = "cityID")
    private City cityID;

    @OneToMany(mappedBy = "townID", cascade = { CascadeType.ALL },orphanRemoval = true)
    private Set<Restaurant> rTownID;

    @OneToMany(mappedBy = "townID", cascade = { CascadeType.ALL },orphanRemoval = true)
    private Set<UserRecords> uTownID;


}
