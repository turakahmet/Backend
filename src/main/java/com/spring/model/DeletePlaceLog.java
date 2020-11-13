package com.spring.model;

import lombok.*;

import javax.persistence.*;
@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeletePlaceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long DeletePlaceID;

    @Column
    private String placeName;

    @Column
    private String placeAddress;

    @Column
    private String category;
}
