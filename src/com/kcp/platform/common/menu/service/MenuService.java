package com.kcp.platform.common.menu.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kcp.platform.base.cache.annotation.CacheEvict;
import com.kcp.platform.common.func.entity.Function;
import com.kcp.platform.common.func.service.FuncService;
import com.kcp.platform.common.log.annotation.Log;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.menu.dao.IMenuMapper;
import com.kcp.platform.common.menu.entity.Menu;
import com.kcp.platform.core.service.BaseService;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.util.DateUtils;


@Service("menuService")
public class MenuService extends BaseService{
	@Autowired
	private IMenuMapper iMenuMapper;
	@Autowired
	private FuncService funcService;

	public List<Menu> queryMenuList(Menu paramMap){
		return iMenuMapper.queryMenuList(paramMap);
	}
	
	
	/**
	 * 查询某个父节点下最大的菜单序号
	 * @param sjcd
	 * @return
	 */
	public String queryMaxCdxh(String sjcd){
		return iMenuMapper.queryMaxCdxh(sjcd);
	}
	/**
	 * 新增菜单
	 * CacheEvict注解是为了刷新菜单缓存信息
	 * @param paramMap
	 * @return
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	@Log(type=OperateLogType.INSERT, moduleName="菜单信息管理", operateDesc="'[新增] 新增[菜单名称：'+#paramMap.cdmc+'] [菜单地址：'+#paramMap.cddz+'] [使用标识：'+#paramMap.sybz+']的菜单信息'")
	public int insert(Menu paramMap){
		int result = iMenuMapper.insert(paramMap);
		Function func = new Function();
		func.setGnmc("查看");//新增的菜单都添加一个"查看"的功能(用于权限分配)
		func.setGndm("VIEW_"+new Date().getTime());
		func.setCdxxZjId(paramMap.getZjId());
		func.setGnxh("1");
		func.setJlxzsj(DateUtils.getCurrentDateTime14());
		func.setJlxgsj(DateUtils.getCurrentDateTime14());
		func.setJlyxzt("1");
		func.setSybz("1");
		funcService.insert(func);
		return result;
	}
	/**
	 * 修改菜单
	 * CacheEvict注解是为了刷新菜单缓存信息
	 * @param paramMap
	 * @return
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	@Log(type=OperateLogType.UPDATE, moduleName="菜单信息管理", operateDesc="'[修改] 修改[菜单名称：'+#paramMap.cdmc+'] [菜单地址：'+#paramMap.cddz+'] [使用标识:'+#paramMap.sybz+']的菜单信息'")
	public int update(Menu paramMap){
		return iMenuMapper.update(paramMap);
	}
	
	/**
	 * 删除菜单
	 * CacheEvict注解是为了刷新菜单缓存信息
	 * @param paramMap
	 * @return
	 */
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	public int delete(String id){
		return iMenuMapper.delete(id);
	}
	
	
	public List<Object[]> createObjectListForExport(List<Map<String, Object>> resultList){
		List<Object[]> rebuildObjectList = new ArrayList<Object[]>();
		for(int i = 0; i < resultList.size(); i++){
			Map<String, Object> mapObj = (Map<String, Object>)resultList.get(i);
			Object[] obj = new Object[5];
			obj[0] = i + 1;
			obj[1] = mapObj.get("NAME");
			obj[2] = mapObj.get("LOGIN_NAME");
			obj[3] = mapObj.get("PASSWORD");
			obj[4] = mapObj.get("REGEDIT_TIME");
			rebuildObjectList.add(obj);
		}
		
		return rebuildObjectList;
	}
	
	public List<Menu> findAllEnableMenus(){
		return iMenuMapper.findAllEnableMenus();
	}
	
	/**
	 * 查找某个菜单下的所有直接下级菜单
	 * @param parentMenuId
	 * @return
	 */
	public List<Menu> queryMenusByParentMenuId(String parentMenuId){
		Menu menu = new Menu();
		menu.setSjcd(parentMenuId);
		menu.setSybz(CommonConst.ENABLE_FLAG);
		menu.setJlyxzt(CommonConst.ENABLE_FLAG);
		return iMenuMapper.findMenuList(menu);
	}
	
	/**
	 * 查询某个菜单的所有上级菜单
	 * @param menuId
	 * @return
	 */
	public List<Menu> queryParentMenuList(String menuId){
		List<Menu> menuList = iMenuMapper.queryParentMenuList(menuId);
		Collections.reverse(menuList);
		return menuList;
	}
	
	/**
	 * 修改树形所有菜单的使用标识
	 * CacheEvict注解是为了刷新菜单缓存信息
	 * @param menuId
	 * @return
	 */
	public void updateChildrenSybz(Menu paramMap){
		iMenuMapper.updateChildrenSybz(paramMap);
	}
	
	public void updateParentSybz(Menu paramMap){
		iMenuMapper.updateParentSybz(paramMap);
	}
	/**
	 * 查询权限信息
	 * @param menuIds
	 * @return
	 */
	public List<Map<String,Object>> getAuthInfo(String[] menuIds){
		return iMenuMapper.getAuthInfo(menuIds);
	}
	
	public Menu getMenuById(String id){
		return iMenuMapper.getMenuById(id);
	}
	@CacheEvict(cacheItemManager="authCacheItemManger", evitAll=true)
	@Log(type=OperateLogType.DELETE, moduleName="菜单信息管理", operateDesc="'[删除] 删除[菜单名称：'+#menu.cdmc+']的菜单信息'")
	public void logicDelMenu(Menu menu){
		funcService.logicDelByMenuId(menu.getZjId());
		menu.setJlscsj(DateUtils.getCurrentDateTime14());
		iMenuMapper.logicDelMenu(menu);
	}
}
