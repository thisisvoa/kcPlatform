/**
 * 
 */
package com.kcp.platform.sys.security.filter;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.kcp.platform.common.menu.service.MenuService;


/**
 *
 */
public class FilterChainDefinitionFactoryBean implements FactoryBean<Map<String, String>> {
	private final static Logger log = LoggerFactory.getLogger(FilterChainDefinitionFactoryBean.class);
	
	@Autowired
	private MenuService menuService;
	
	@Override
	public Map<String, String> getObject() throws Exception {
		log.debug("=====开始构造shiro的url映射=====");
		Map<String, String> urls = new LinkedHashMap<String, String>();	//必须是有序的，代表着chainFilter的顺序
		urls.put("/tologin.html", "anon");
		urls.put("/login.html", "anon");
		urls.put("/pkilogin.html", "anon");
		urls.put("/unauthorized.html", "anon");
		urls.put("/ui/**", "anon");
		urls.put("/job/serverHostRecoginzer.html", "anon");
		
		/*List<Menu> menuList = this.menuService.findAllEnableMenus();
		for(Menu menu:menuList){
			if("1".equals(menu.getSfzhyicd())){
				urls.put(menu.getCddz()+ "*", "perms["+"MENU_"+menu.getZjId()+"]");
			}
		}*/
		urls.put("/**", "authc");
		return urls;
	}

	@Override
	public Class<?> getObjectType() {
		return Map.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @return the permissionMapper
	 */
	/*public IPermissionMapper getPermissionMapper() {
		return permissionMapper;
	}*/

	/**
	 * @param permissionMapper the permissionMapper to set
	 */
	/*public void setPermissionMapper(IPermissionMapper permissionMapper) {
		this.permissionMapper = permissionMapper;
	}*/
}
