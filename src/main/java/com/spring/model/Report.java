package com.spring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * Created by bdurmusoglu on 24.06.2020.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table
public class Report {

    @Id
    @Column
    private long reportID;

    @ManyToOne
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurantID;

    @ColumnDefault("0")
    private int report_count;

    @ColumnDefault("0")
    private int closedPlace;

    @ColumnDefault("0")
    private int fakePlace;

    @ColumnDefault("0")
    private int wrongLocation;

    @ColumnDefault("0")
    private int wrongInfo;

    @ColumnDefault("0")
    private int wrongScore;
}
