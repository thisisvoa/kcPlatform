package com.kcp.platform.base.job.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.base.job.dao.IJobRunLogMapper;
import com.kcp.platform.base.job.entity.JobRunLog;
import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.Log;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.core.service.BaseService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("jobLogService")
public class JobLogService extends BaseService{

    @Autowired
    private IJobRunLogMapper jobRunLogMapper;

    public List<JobRunLog> findJobRunLog(Map<String,Object> queryMap){
    	return jobRunLogMapper.findJobRunLog(queryMap);
    }
    
    @Log(type=OperateLogType.QUERY, moduleName="任务调度", operateDesc="[查询] 查询调度日志信息", useSpel=false)
    public List<JobRunLog> findJobRunLogPaging(Map<String,Object> queryMap){
    	return jobRunLogMapper.findJobRunLogPaging(queryMap);
    }
    /**
     * 保存任务运行日志
     *
     * @param jobRunLog
     */
    public void saveJobRunLog(JobRunLog jobRunLog) {
    	jobRunLogMapper.addJobRunLog(jobRunLog);
    }

    /**
     * 根据主键获取运行日志
     *
     * @param id
     * @return
     */
    public JobRunLog getJobRunLogById(String id) {
        return jobRunLogMapper.getJobRunLogById(id);
    }

    /**
     * 根据主键ID删除运行日志
     *
     * @param logIdList
     */
    public void batchDeleteJobRunLog(List<String> logIdList) {
    	if(logIdList!=null){
    		SqlContextHolder.clear();
    		for(String id:logIdList){
    			jobRunLogMapper.deleteJobRunLog(id);
    		}
    		String jobIdStr = StringUtils.join(logIdList.toArray(),",");
    		String sql = SqlContextHolder.getSql();
    		Logger.getInstance().addSysLog(OperateLogType.DELETE.value(), sql, "任务调度", "[删除] 删除任务日志 [任务ID："+jobIdStr+"]");
    	}
    }

    /**
     * 根据时间段删除运行日志
     *
     * @param beginTime
     * @param endTime
     */
    public void batchDeleteJobRunLog(String jobName,Date beginTime, Date endTime) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("jobName", jobName);
    	map.put("beginTime", beginTime);
    	map.put("endTime", endTime);
    	jobRunLogMapper.batchDeleteJobRunLog(map);
    }

    /**
     * 删除所有运行日志
     */
    public void deleteAllJobRunLog(String jobName) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("jobName", jobName);
    	jobRunLogMapper.batchDeleteJobRunLog(map);
    }

}
