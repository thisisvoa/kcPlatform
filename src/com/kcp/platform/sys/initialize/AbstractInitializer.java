package com.kcp.platform.sys.initialize;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kcp.platform.core.exception.InitializeException;
import com.kcp.platform.util.Orderable;


/**
 *
 *类描述：
 *  系统初始化类适配器
 *@Version：1.0
 */
public abstract class AbstractInitializer implements Initializer, Orderable {
	protected Log logger = (Log) LogFactory.getLog(this.getClass());
    
    /**
     * 标识是否已经初始化
     */
    protected boolean initialized = false;
    
    /**
     * 需要子类实现的初始化操作
     */
    public abstract void init();
    
    /**
     * 执行初始化操作
     */
    public void initialize() throws InitializeException {
        // 如果已初始化，就不在进行初始化操作
        if(isInitialized())
            return;
        
        String desc = getDesc();  //获取初始化器描述
        
        logger.info(" [ " + desc + " ] 初始化开始...");
        
        try {
            init();   //执行初始化操作
            
            initialized = true;    //设置初始化标识
            logger.info(" [ " + desc + " ] 初始化结束...\n");
        } catch (Exception e) {
        	logger.error(e);
            throw new InitializeException(" [ " + desc + " ] 初始化失败");
        }
    }

    /**
     * 判断是否已经初始化
     * 
     * @return
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 排序号，越小越在前面。当初始化器需要优先加载(存在依赖)的时候，就需要重写该方法
     * 
     * @return 默认返回1000
     */
    public int getOrderNo(){
        return Orderable.HIGHEST_PRECEDENCE + 1000;
    }
}
