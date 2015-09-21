package com.casic27.platform.common.func.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.casic27.platform.common.func.entity.Function;
import com.casic27.platform.common.log.annotation.OperateLog;
import com.casic27.platform.common.menu.entity.Menu;
import com.casic27.platform.core.mybatis.annotation.Pageable;

@Repository
public interface IFuncMapper {

	@Pageable
	@OperateLog(isRecord=false)
	public List<Function> queryFuncList(Function funcMap);
	
	public List<Menu> queryMenuList();
	
	@OperateLog(isRecord=false)
	public int insert(Function paramMap);
	
	@OperateLog(isRecord=false)
	public int update(Function paramMap);
	
	@OperateLog(isRecord=false)
	public int delete(String id);
	
	/**
	 * 查找功能
	 * @param func
	 * @return
	 */
	List<Function> findFuncList(Function func);
	
	/**
	 * 根据Id查询功能
	 * @param funcId
	 * @return
	 */
	@OperateLog(isRecord=false)
	Function getFuncById(@Param("funcId")String funcId);
	
	void logicDelByMenuId(@Param("menuId")String menuId, @Param("curTime")String curTime);
}
