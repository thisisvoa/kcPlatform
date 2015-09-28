package com.kcp.platform.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kcp.platform.core.dao.BaseJdbcDao;
import com.kcp.platform.core.dao.BaseMyBatisDao;

public class BaseService {
	protected Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	@Qualifier("baseJdbcDao")
	protected BaseJdbcDao baseJdbcDao;
	
	@Autowired
	@Qualifier("baseMyBatisDao")
	protected BaseMyBatisDao baseMyBatisDao;

	public BaseJdbcDao getBaseJdbcDao() {
		return baseJdbcDao;
	}

	public void setBaseJdbcDao(BaseJdbcDao baseJdbcDao) {
		this.baseJdbcDao = baseJdbcDao;
	}

	public BaseMyBatisDao getBaseMyBatisDao() {
		return baseMyBatisDao;
	}

	public void setBaseMyBatisDao(BaseMyBatisDao baseMyBatisDao) {
		this.baseMyBatisDao = baseMyBatisDao;
	}

}
