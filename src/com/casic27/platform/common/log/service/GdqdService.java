/**
 * @(#)com.casic27.platform.common.log.service.GdqdService
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright: Copyright(c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.common.log.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.common.log.dao.IGdqdMapper;
import com.casic27.platform.common.log.entity.Gdqd;
import com.casic27.platform.core.service.BaseService;

@Service("gdqdService")
public class GdqdService extends BaseService {
	@Autowired
	private IGdqdMapper gdqdMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<Gdqd> findGdqd(Map<String,Object> queryMap){
		return gdqdMapper.findGdqd(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<Gdqd> findGdqdPaging(Map<String,Object> queryMap){
		return gdqdMapper.findGdqdPaging(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public Gdqd getGdqdById(String id){
		return gdqdMapper.getGdqdById(id);
	}
	/**
	 * 新增
	 */
	public void addGdqd(Gdqd gdqd){
		gdqd.setZjid(CodeGenerator.getUUID32());
		gdqdMapper.addGdqd(gdqd);
	}
	
	/**
	 * 修改
	 */
	public void updateGdqd(Gdqd gdqd){
		gdqdMapper.updateGdqd(gdqd);
	}
	
	/**
	 *删除
	 */
	public void deleteGdqd(String id){
		gdqdMapper.deleteGdqd(id);
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countGdqd(Gdqd gdqd){
		return gdqdMapper.countGdqd(gdqd);
	}
}