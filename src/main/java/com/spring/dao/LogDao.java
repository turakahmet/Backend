package com.spring.dao;

import com.spring.model.Log;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.bytebuddy.asm.Advice;

import java.time.LocalDate;

/**
 * Created by egulocak on 21.08.2020.
 */
public interface LogDao {

    String savelog(Log log);
    Boolean getrecordcount(LocalDate localDate,String remoteIp,String action);

}
