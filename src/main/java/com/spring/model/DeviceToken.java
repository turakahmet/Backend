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
public class DeviceToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int deviceID;

    @Column(unique = true)
    private String deviceToken;

    @ColumnDefault("0")
    private int deviceStatus;

}
