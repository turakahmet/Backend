package com.spring.model;

import lombok.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by egulocak on 21.08.2020.
 */
@Entity
@Data
@Table
@Setter
@Getter
@NoArgsConstructor
public class Log {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    public Log(String requestDesc, String requestIp) {
        this.requestDesc = requestDesc;
        this.requestIp = requestIp;
    }

    private String requestDesc;


    private String requestIp;

    private LocalDate requestDate = LocalDate.now();


}
