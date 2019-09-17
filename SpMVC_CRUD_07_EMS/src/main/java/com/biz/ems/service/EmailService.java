package com.biz.ems.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biz.ems.mapper.EmailDao;
import com.biz.ems.model.EmailVO;

@Service
public class EmailService {
 @Autowired
 EmailDao eDao;
 
	public int insert(EmailVO emailVO) {
	int ret= eDao.insert(emailVO); 
		return ret;
	}

}
