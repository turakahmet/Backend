package com.spring.model;

import lombok.*;
import oracle.sql.BLOB;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Set;

@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long recordID;

    @Column(nullable = false)
    private long userID;

    @Column
    private String restaurantName;

    @Column
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    @Column
    private String locality_verbose;

    @Column
    private String phone_number;

    @Column
    private String timings;

    @Column
    private String place_type;

    @Column
    private String cuisines;

    @Column
    private String sticker;

    @Column
    private String latitude;

    @Column
    private String longitude;

    @Column
    private String restaurantImageUrl;

}
