package com.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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


 //   @JsonIgnoreProperties("townID")
    @ManyToOne
    @JoinColumn(name = "townID")
    private Town townID;

  //  @JsonIgnoreProperties("cityID")
    @ManyToOne
    @JoinColumn(name = "cityID")
    private City cityID;

    @Column
    private String phone_number;

    @Column
    private String timings;

    @Column
    private String category;

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
