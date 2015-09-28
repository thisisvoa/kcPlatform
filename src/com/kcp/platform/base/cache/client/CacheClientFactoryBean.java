
package com.kcp.platform.base.cache.client;

import java.util.List;

import net.sf.ehcache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.kcp.platform.base.cache.client.ehcache.EhCacheClient;
import com.kcp.platform.base.cache.client.expire.CacheLogicExpireInterceptor;
import com.kcp.platform.base.cache.client.expire.CacheLogicExpireManager;
import com.kcp.platform.base.cache.client.expire.SimpleCacheLogicExpireManager;

/**
 *    可通过如下方式在Spring中配置远程缓存客户端：
 * @version Revision: 1.00 Date: Nov 10, 2011
 */
public class CacheClientFactoryBean extends AbstractFactoryBean<CacheClient> implements BeanNameAware {
	/**
	 * 日志记录器
	 */
    protected Log logger = LogFactory.getLog(this.getClass());
    
    /**
     * 缓存客户端拦截器
     */
    private List<CacheInterceptor> cacheInterceptors;
    
    /**
     * 缓存逻辑过滤器
     */
    private CacheLogicExpireManager cacheLogicExpireManager;
    
    private CacheManager cacheManager;
    
    /**
     * Bean名称
     */
    private String beanName;
    /**
     * Ehcache缓存段名称 
     */
    private String cacheName;
    
    /**
     * 根据配置信息,实例化缓存客户端
     */
	protected CacheClient createInstance() throws Exception {
		CacheClient client = new EhCacheClient(cacheManager.getCache(getCacheName()));
		
		//若无配置逻辑过期管理器，则采用默认的逻辑过期管理器
        if(this.cacheLogicExpireManager == null){
        	cacheLogicExpireManager = new SimpleCacheLogicExpireManager();
        }
        cacheLogicExpireManager.setCacheClient(client);
    	
        CacheLogicExpireInterceptor cacheLogicExpireInterceptor = new CacheLogicExpireInterceptor();
        cacheLogicExpireInterceptor.setCacheLogicExpireManager(this.cacheLogicExpireManager);

        //装配逻辑缓存过期管理器
        client.setCacheLogicExpireManager(cacheLogicExpireManager);
        client.addCacheInterceptor(cacheLogicExpireInterceptor);
        logger.info("缓存客户端：["+beanName+"],装配了逻辑过期管理器");
		return client;
	}

	public Class<?> getObjectType() {
		return CacheClient.class;
	}

	public List<CacheInterceptor> getCacheInterceptors() {
		return cacheInterceptors;
	}

	public void setCacheInterceptors(List<CacheInterceptor> cacheInterceptors) {
		this.cacheInterceptors = cacheInterceptors;
	}

	public CacheLogicExpireManager getCacheLogicExpireManager() {
		return cacheLogicExpireManager;
	}

	public void setCacheLogicExpireManager(
			CacheLogicExpireManager cacheLogicExpireManager) {
		this.cacheLogicExpireManager = cacheLogicExpireManager;
	}
    
	/**
	 * 使用EhCache时,需配置缓存段名称
	 * @param cacheName
	 */
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public String getCacheName() {
		return cacheName;
	}
	
	
	public void setBeanName(String beanName){
		this.beanName = beanName;
	}
}
