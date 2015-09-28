package com.kcp.platform.base.job;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.kcp.platform.util.JsonParser;

/**
 *    任务执行的结果信息，在{@link com.epgis.platform.module.job.Job#execute(org.quartz.JobExecutionContext)}方法执行过程中，将执行的结果通过该
 * 对象进行记录，并以<code>JobExecuteResult.EXECUTE_RESULT</code>为键保存在<code>JobExecutionContext</code>中。即
 * 可以通过如下语句获取该对象：
 * <pre>
 *    (JobExecuteResult)jobExecutionContext.get(JobExecuteResult.EXECUTE_RESULT);
 * </pre>
 */
public class JobExecuteResult {
     /**
     * 任务执行时，其执行信息
     */
    public static final String EXECUTE_RESULT = "__EXECUTE_RESULT";

    /**
     * 执行成功
     */
    protected static final String SUCCESS = "1";

    /**
     * 执行失败
     */
    protected static final String FAIL = "2";


    /**
     * 存储任务执行的结果信息，以键值对的方式进行存储
     */
    private Map<String,Object> results = new HashMap<String,Object>();

    /**
     * 执行结果代码，参见<code>SUCCESS</code> 和  <code>FAIL</code> 的定义
     */
    private String statusCode = SUCCESS;

    /**
     * 任务开始执行时间
     */
    private Date beginTime ;


	/**
     * 任务结束执行时间
     */
    private Date endTime;

    protected JobExecuteResult(){
        this.beginTime = new Date();
    }
    /**
     * 添加一条结果信息项
     * @param key    信息项键
     * @param result  信息项内容
     */
    public void putResult(String key,Object result){
        results.put(key,result);
    }

    /**
     * 从执行结果集中通过一个结果项
     * @param key
     * @return
     */
    public Object getResult(String key){
        return results.get(key);
    }
    /**
     *    将执行结果信息转换为一个JSON串
     * @return
     */
    private String toJsonStr(){
        return JsonParser.convertObjectToJson(this.results);
    }

    /**
     * 获取简要的结果信息，最大1500个字符
     * @return
     */
    public String getSummaryInfo(){
        String result = toJsonStr();
        if(result != null && result.length() > 1000){
           return result.substring(0,1000);
        }else{
           return result;
        }
    }

    public String getStatusCode() {
        return statusCode;
    }
    
    public Date getBeginTime() {
		return beginTime;
	}
	
    public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
    /**
     * 将处理结果标识为失败,如果任务执行失败，则调用本方法填写错误信息，只要调用了本方法
     * 就无须再调用<code>setExecuteFail()</code>了。
     * @param errorInfo 失败信息
     */
   public void putErrorInfo(String errorInfo){
       this.statusCode = FAIL;
       putResult("customErrorInfo",errorInfo);
   }

    /**
     * 设置执行失败
     */
   public void setExecuteFail(){
       this.statusCode = FAIL;
   }

    /**
     * 任务是否执行成功
     * @return
     */
    public boolean isExecutedSucessful(){
        return SUCCESS.equals(this.getStatusCode());
    }
}
