/**
 * @(#)com.casic27.platform.common.log.service.GdpzService
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.common.log.dao.IGdpzMapper;
import com.casic27.platform.common.log.entity.Gdpz;
import com.casic27.platform.core.service.BaseService;

@Service("gdpzService")
public class GdpzService extends BaseService {
	@Autowired
	private IGdpzMapper gdpzMapper;
	
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<Gdpz> findGdpz(Map<String,Object> queryMap){
		return gdpzMapper.findGdpz(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<Gdpz> findGdpzPaging(Map<String,Object> queryMap){
		return gdpzMapper.findGdpzPaging(queryMap);
	}
	
	public List<Gdpz> getEnableGdpz(){
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("sybz", "1");
		return findGdpz(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public Gdpz getGdpzById(String id){
		return gdpzMapper.getGdpzById(id);
	}
	/**
	 * 新增
	 */
	public void addGdpz(Gdpz gdpz){
		gdpz.setZjid(CodeGenerator.getUUID32());
		gdpzMapper.addGdpz(gdpz);
	}
	
	/**
	 * 修改
	 */
	public void updateGdpz(Gdpz gdpz){
		gdpzMapper.updateGdpz(gdpz);
	}
	
	/**
	 *删除
	 */
	public void deleteGdpz(String id){
		gdpzMapper.deleteGdpz(id);
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countGdpz(Gdpz gdpz){
		return gdpzMapper.countGdpz(gdpz);
	}
}