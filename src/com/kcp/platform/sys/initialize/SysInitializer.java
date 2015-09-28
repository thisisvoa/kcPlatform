package com.kcp.platform.sys.initialize;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.kcp.platform.util.OrderableComparator;



/**
 *
 *类描述：系统初始化器
 * 
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
