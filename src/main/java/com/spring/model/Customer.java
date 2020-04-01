package com.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

/**
 * Entity bean with JPA annotations
 * Hibernate provides JPA implementation
 * @author aturak
 * my model
 */
@Getter
@Setter
@Entity
@Table(name = "CUSTOMER")
public class Customer{
    @Id
    @GeneratedValue
    @Column
    private long id;
    @Column
    private String name;
    @Column
    private String email;


    public Customer (String name, String email, long id){
        super();
        this.name=name;
        this.email=email;
        this.id=id;
    }
    public Customer(){super();}

}
