package com.spring.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table
public class AdminTK {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int adminID;

    @Column
    private int adminStatus;

    @Column
    private String adminIDName;

    @Column
    private String adminPW;

    @Column
    private String uniqueID;
}
