package com.casic27.platform.common.para.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.casic27.platform.common.log.annotation.OperateLog;
import com.casic27.platform.common.para.entity.Parameter;
import com.casic27.platform.core.mybatis.annotation.Pageable;

@Repository
public interface IParaMapper {

	@Pageable
	@OperateLog(isRecord=false)
	public List<Parameter> queryParaList(Parameter para);
	
	public String queryMaxId();
	
	@OperateLog(isRecord=false)
	public int insert(Parameter paramMap);
	
	@OperateLog(isRecord=false)
	public int update(Parameter paramMap);
	
	@OperateLog(isRecord=false)
	public void updateSybz(Parameter paramMap);
	
	public int statisCount(Parameter paramMap);
	
	@OperateLog(isRecord=false)
	public void logicDelParam(Parameter paramMap);
	
	public int delete(String id);
	
	@OperateLog(isRecord=false)
	public Parameter getParaById(@Param("zjId")String zjId);
	
	public Parameter getParaByCsdm(@Param("csdm")String csdm);
	
	/**
	 * 增加历史在线人数
	 */
	public void addHisOnline();
}
