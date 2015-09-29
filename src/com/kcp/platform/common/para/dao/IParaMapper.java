package com.kcp.platform.common.para.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcp.platform.common.log.annotation.OperateLog;
import com.kcp.platform.common.para.entity.SysParameter;
import com.kcp.platform.core.mybatis.annotation.Pageable;

@Repository
public interface IParaMapper {

	@Pageable
	@OperateLog(isRecord=false)
	public List<SysParameter> queryParaList(SysParameter para);
	
	public String queryMaxId();
	
	@OperateLog(isRecord=false)
	public int insert(SysParameter paramMap);
	
	@OperateLog(isRecord=false)
	public int update(SysParameter paramMap);
	
	@OperateLog(isRecord=false)
	public void updateSybz(SysParameter paramMap);
	
	public int statisCount(SysParameter paramMap);
	
	@OperateLog(isRecord=false)
	public void logicDelParam(SysParameter paramMap);
	
	public int delete(String id);
	
	@OperateLog(isRecord=false)
	public SysParameter getParaById(@Param("zjId")String zjId);
	
	public SysParameter getParaByCsdm(@Param("csdm")String csdm);
	
	/**
	 * 增加历史在线人数
	 */
	public void addHisOnline();
}
