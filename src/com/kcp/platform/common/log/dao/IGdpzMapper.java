package com.kcp.platform.common.log.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.kcp.platform.common.log.entity.Gdpz;
import com.kcp.platform.core.mybatis.annotation.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IGdpzMapper{
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 * @return
	 */
	List<Gdpz> findGdpz(Map<String,Object> queryMap);
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 * @return
	 */
	@Pageable
	List<Gdpz> findGdpzPaging(Map<String,Object> queryMap);
	
	/**
	 * 根据Id进行查询
	 */
	Gdpz getGdpzById(@Param("id")String id);
	
	/**
	 * 新增
	 */
	void addGdpz(Gdpz gdpz);
	
	/**
	 * 修改
	 */
	void updateGdpz(Gdpz gdpz);
	
	/**
	 * 物理删除
	 */
	void deleteGdpz(@Param("id")String id);
	
	/**
	 * 根据条件统计记录数
	 */
	int countGdpz(Gdpz gdpz);
	
}