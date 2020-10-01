package com.spring.feedbacks;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Getter
@Setter
@Repository
public class Error {
    private String feedback;
    private int code;
}
