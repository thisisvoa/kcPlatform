package com.kcp.platform.common.func.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.base.cache.annotation.CacheEvict;
import com.kcp.platform.common.func.dao.IFuncMapper;
import com.kcp.platform.common.func.entity.Function;
import com.kcp.platform.common.log.annotation.Log;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.menu.entity.Menu;
import com.kcp.platform.core.service.BaseService;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.util.DateUtils;


@Service("funcService")
public class FuncService extends BaseService{
	@Autowired
	private IFuncMapper iFuncMapper;
	public IFuncMapper getFuncDao() {
		return iFuncMapper;
	}
	public void setFuncDao(IFuncMapper funcDao) {
		this.iFuncMapper = funcDao;
	}

	@Log(type=OperateLogType.QUERY, moduleName="功能信息管理", operateDesc="[查询] 查询功能信息", useSpel=false)
	public List<Function> queryFuncList(Function funcMap){
		return iFuncMapper.queryFuncList(funcMap);
	}
	
	public List<Menu> queryMenuList(){
		return iFuncMapper.queryMenuList();
	}
	
	/**
	 * 新增功能
	 * @param paramMap
	 * @return
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	@Log(type=OperateLogType.INSERT, moduleName="功能信息管理", operateDesc="'[新增] 新增[功能名称：'+#paramMap.gnmc+'] [功能代码：'+#paramMap.gndm+']的功能信息'")
	public int insert(Function paramMap){
		return iFuncMapper.insert(paramMap);
	}
	
	/**
	 * 修改功能
	 * @param paramMap
	 * @return
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	@Log(type=OperateLogType.UPDATE, moduleName="功能信息管理", operateDesc="'[修改] 修改[功能名称：'+#paramMap.gnmc+'] [功能代码：'+#paramMap.gndm+']的功能信息'")
	public int update(Function paramMap){
		return iFuncMapper.update(paramMap);
	}
	
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	@Log(type=OperateLogType.DELETE, moduleName="功能信息管理", operateDesc="'[删除] 删除[功能名称：'+#paramMap.gnmc+'] [功能代码：'+#paramMap.gndm+']的功能信息'")
	public void logicDelete(Function paramMap){
		update(paramMap);
	}
	
	@Log(type=OperateLogType.UPDATE, moduleName="功能信息管理", operateDesc="'[启用/禁用] 修改[功能名称：'+#paramMap.gnmc+'] [功能代码：'+#paramMap.gndm+'] 的使用标志为['+#paramMap.sybz+']'")
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	public void updateSybz(Function paramMap){
		update(paramMap);
	}
	/**
	 * 删除功能
	 * @param paramMap
	 * @return
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	public int delete(String id){
		return iFuncMapper.delete(id);
	}
	
	/**
	 * 查询可用的方法列表
	 * @return
	 */
	public List<Function> findAllEnableFuncs(){
		Function func = new Function();
		func.setSybz(CommonConst.ENABLE_FLAG);
		func.setJlyxzt(CommonConst.ENABLE_FLAG);
		return iFuncMapper.findFuncList(func);
	}
	
	/**
	 * 查找某个菜单下的所有功能
	 * @param parentMenuId
	 * @return
	 */
	public List<Function> queryFuncByParentMenuId(String parentMenuId){
		Function func = new Function();
		func.setCdxxZjId(parentMenuId);
		func.setSybz(CommonConst.ENABLE_FLAG);
		func.setJlyxzt(CommonConst.ENABLE_FLAG);
		return iFuncMapper.findFuncList(func);
	}
	
	/**
	 * 根据功能Id查询功能
	 * @param funcId
	 * @return
	 */
	public Function getFuncById(String funcId){
		return iFuncMapper.getFuncById(funcId);
	}
	
	/**
	 * 根据菜单ID删除 
	 * @param menuId
	 */
	public void logicDelByMenuId(String menuId){
		String curTime = DateUtils.getCurrentDateTime14();
		iFuncMapper.logicDelByMenuId(menuId, curTime);
	}
}
