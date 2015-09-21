/**
 * @(#)com.casic27.platform.core.service.BaseService.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Author： 林斌树(linbinshu@casic27.com)
 *<br> Date：Apr 11, 2012
 *<br> Version：1.0
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.casic27.platform.core.dao.BaseJdbcDao;
import com.casic27.platform.core.dao.BaseMyBatisDao;

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
