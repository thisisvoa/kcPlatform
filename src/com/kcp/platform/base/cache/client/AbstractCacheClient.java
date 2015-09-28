package com.kcp.platform.base.cache.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.util.Assert;

import com.kcp.platform.base.cache.client.expire.CacheLogicExpireManager;
import com.kcp.platform.base.cache.client.expire.CacheSpace;
import com.kcp.platform.base.cache.client.expire.LogicExpireCommand;
import com.kcp.platform.base.cache.entity.CacheObject;
/**
 *
 * 类描述：
 * 采用模板模式，抽象出通用的缓存客户端的实现;其余的具体实现由子类实现之。
 *@Version：1.0
 */
@SuppressWarnings("unchecked")
public abstract class AbstractCacheClient implements CacheClient {
	
	protected List<CacheInterceptor> cacheInterceptors = new ArrayList<CacheInterceptor>();
	
	protected CacheLogicExpireManager cacheLogicExpireManager;
	
	public void put(String key, CacheObject value) {
		prePut(key, value, false);
		value.setVersion(value.getVersion() + 1);
		value.setCacheUpdateTime(getCurrTime());
		putHandler(key, value);
	}
	/**
	 * 由各个缓存策略实现
	 */
	protected abstract void putHandler(String key, CacheObject value);
	
	public CacheObject get(String key) {
		return get(key,false);
	}

	public <T> CacheObject<T> get(String key, Class<T> requiredType) {
		return (CacheObject<T>) get(key);
	}
	private CacheObject get(String key, boolean ignoreIntercepted) {
        if (!preGet(key,ignoreIntercepted)) return null;
        CacheObject value = getHandler(key);
        postGet(key, value,ignoreIntercepted);
        return value;
    }
	
	protected abstract CacheObject getHandler(String key);
	
	private boolean preGet(String key, boolean ignoreIntercepted) {
        if (ignoreIntercepted) {
            return true;
        } else {
            CacheObject cacheable = get(key, true); //不执行过滤器
            for (CacheInterceptor cacheInterceptor : this.cacheInterceptors) {
                if (cacheInterceptor.isMatch(key)) {
                    if (!cacheInterceptor.preGet(key, cacheable)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
	
	private void postGet(String key, CacheObject value, boolean ignoreIntercepted) {
        if(!ignoreIntercepted){
            for (CacheInterceptor cacheInterceptor : this.cacheInterceptors) {
                if (cacheInterceptor.isMatch(key)) {
                    cacheInterceptor.postGet(key, value);
                }
            }
        }
    }
	
	public List<LogicExpireCommand> getCommands(String spaceName) {
		return getCacheLogicExpireManager().getCommands(spaceName);
	}

	public boolean remove(String key) {
		preRemove(key);
		removeHandler(key);
		return false;
	}
	
	/**
	 * 执行缓存数据删除前的拦截
	 * @param key
	 */
	private void preRemove(String key) {
        for (CacheInterceptor cacheInterceptor : cacheInterceptors) {
            if (cacheInterceptor.isMatch(key)) {
                cacheInterceptor.preRemove(key);
            }
        }
    }
	/**
	 * 各种缓存策略删除缓存数据的实现
	 * @param key
	 * @return
	 */
	protected abstract boolean removeHandler(String key);
	
	public void issueCommand(LogicExpireCommand command) {
        getCacheLogicExpireManager().issueCommand(command);
	}
	
	public void removeCommand(String spaceName) {
        getCacheLogicExpireManager().removeCommand(spaceName);
    }

    public void removeCommand(String spaceName, String scopeName) {
        getCacheLogicExpireManager().removeCommand(spaceName,scopeName);
    }
    
	public void setCacheLogicExpireManager(
			CacheLogicExpireManager cacheLogicExpireManager) {
		this.cacheLogicExpireManager = cacheLogicExpireManager;
	}
	
	public CacheLogicExpireManager getCacheLogicExpireManager() {
        Assert.notNull(this.cacheLogicExpireManager,"cacheLogicExpireManager为空，无法执行缓存逻辑过期的操作");
        return cacheLogicExpireManager;
    }
	
	public void addCacheInterceptor(CacheInterceptor cacheInterceptor) {
		Assert.isTrue(cacheInterceptor.getOrderNo() >= 0);
        this.cacheInterceptors.add(cacheInterceptor);
        Collections.sort(this.cacheInterceptors, new Comparator<CacheInterceptor>() {
            public int compare(CacheInterceptor o1, CacheInterceptor o2) {
                return o1.getOrderNo() - o2.getOrderNo();
            }
        });
	}
	
	public void addCacheSpace(CacheSpace cacheSpace){
		Assert.notNull(this.cacheLogicExpireManager,"cacheLogicExpireManager为空，无法执行缓存逻辑过期的操作");
		this.cacheLogicExpireManager.addCacheSpace(cacheSpace);
	}
	
	/**
	 * 将数据存入缓存前的回调
	 * @param key
	 * @param value
	 * @param ignoreIntercepted
	 */
	private void prePut(String key, CacheObject value,boolean ignoreIntercepted) {
        if(!ignoreIntercepted){
            for (CacheInterceptor cacheInterceptor : cacheInterceptors) {
                if (cacheInterceptor.isMatch(key)) {
                    cacheInterceptor.prePut(key, value);
                }
            }
        }
    }
	
	/**
	 * 获取当前时间，返回Long类型
	 * @return
	 */
	private long getCurrTime(){
        return System.currentTimeMillis();
    }
}
