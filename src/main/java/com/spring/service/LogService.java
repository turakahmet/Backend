package com.spring.service;

import com.spring.model.Log;

import java.time.LocalDate;


public interface LogService {

    String savelog(Log log);
    Boolean getrecordcount(LocalDate localDate,String remoteIp,String action);



}
