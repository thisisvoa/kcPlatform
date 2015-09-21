/**
 * @(#)com.casic27.platform.base.cache.itemmanager.CacheItemIntrodution.java
 * 版权声明 亿力科技, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2011
 *<br> Company： 亿力科技
 *<br> Author： 林斌树(lin_binshu@sgcc.com.cn)
 *<br> Date：Dec 5, 2011
 *<br> Version：3.0
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.base.cache.itemmanager;

/**
 * 缓存项描述信息
 * 
 * @author 林斌树 (linbinshu@casic27.com)
 * @version Revision: 1.00 Date: Dec 5, 2011
 */
public interface CacheItemIntrodution {
	/**
	 * 获取缓存段管理器的描述,如：字典表,用户,部门等
	 * @return
	 */
	String getDesc();
	
	/**
	 * 缓存项名称，主要作为缓存项管理器的标识
	 * @return
	 */
	String getName();
	
	/**
	 * 缓存项内的缓存数据使用的缓存空间名，
	 * 缓存空间名用于构造key,缓存项内的缓存数据键格式为：space+"#"+key(key为使用get接口时候穿进来的值)；
	 * 建议使用com.eic.basic.user这样的缓存空间名作为缓存项名称，便于划分各类缓存，防止命名冲突；
	 * 注：两个并列毫无相关的缓存项名称不能有层级关系
	 * @return
	 */
	String getSpace();
	
}
