package com.spring.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPlaces {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long placeID;

    @ManyToOne
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "userID")
    private AppUser user;

    @ColumnDefault("1")
    private int placeStatus;

    @JsonFormat(pattern="dd-MM-yyyy")
    @UpdateTimestamp
    @Temporal(TemporalType.DATE)
    private Date placeActiveDate;



}
