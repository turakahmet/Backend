package com.spring.service;

import com.spring.model.Log;

import java.time.LocalDate;

/**
 * Created by egulocak on 21.08.2020.
 */
public interface LogService {

    String savelog(Log log);
    Boolean getrecordcount(LocalDate localDate,String remoteIp,String action);



}
