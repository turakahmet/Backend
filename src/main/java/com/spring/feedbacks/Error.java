package com.spring.feedbacks;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

/**
 * Created by egulocak on 17.04.2020.
 */
@Getter
@Setter
@Repository
public class Error {
    private String feedback;
    private int code;
}
