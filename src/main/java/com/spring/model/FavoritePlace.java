package com.spring.model;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long favoriteID;

    @ManyToOne
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "userID")
    private AppUser user;
}
