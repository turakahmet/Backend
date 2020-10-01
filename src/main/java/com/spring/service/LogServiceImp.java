package com.spring.service;

import com.spring.dao.LogDao;
import com.spring.dao.UserDAO;
import com.spring.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;




@Service
@Transactional
public class LogServiceImp implements LogService {

    @Autowired
    LogDao logDao;

    @Override
    public String savelog(Log log) {
        return logDao.savelog(log);
    }

    @Override
    public Boolean getrecordcount(LocalDate localDate,String remoteIp, String action) {
        return logDao.getrecordcount(localDate,remoteIp,action);
    }


}
