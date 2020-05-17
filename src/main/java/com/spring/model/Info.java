package com.spring.model;

import lombok.*;

import javax.persistence.*;
@Setter
@Getter
@ToString
public class Info {
    private long lastRestaurantId;
    private long totalUser;
    private long totalRestaurant;
    private long totalRecord;
}
