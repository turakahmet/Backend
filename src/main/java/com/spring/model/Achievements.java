package com.spring.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Achievements {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long achievementID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private AppUser user;

    @ColumnDefault("0")
    private int a1;

    @ColumnDefault("0")
    private int a2;

    @ColumnDefault("0")
    private int a3;

    @ColumnDefault("0")
    private int a4;

    @ColumnDefault("0")
    private int a5;

    @ColumnDefault("0")
    private int a6;

    @ColumnDefault("0")
    private int a7;

    @ColumnDefault("0")
    private int a8;

    @ColumnDefault("0")
    private int a9;

    @ColumnDefault("0")
    private int a10;

    @ColumnDefault("0")
    private int a11;

    @ColumnDefault("0")
    private int a12;

    @ColumnDefault("0")
    private int a13;
}
