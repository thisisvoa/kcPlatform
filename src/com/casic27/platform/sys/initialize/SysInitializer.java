/**
 * @(#)com.casic27.platform.sys.initialize.SysInitializer.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 13, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.sys.initialize;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.casic27.platform.util.OrderableComparator;



/**
 *
 *类描述：系统初始化器
 * 
 *@Author： 林斌树(linbinshu@casic27.com)
 *@Version：1.0
 */
public class SysInitializer {
	
	@Autowired(required = false)
	private List<Initializer> initializerList = null;
	
	public SysInitializer(){
		
	}
	
	public void initialize(){
		if(initializerList == null || initializerList.size() == 0)
            return;
        
        OrderableComparator.sort(initializerList);      //对系统中的初始化器进行排序
        
        for(Initializer initializer : initializerList){
            initializer.initialize();     //执行初始化操作
        }
	}
}
