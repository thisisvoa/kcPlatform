package com.kcp.platform.base.job.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.base.job.JobSchedulerManager;
import com.kcp.platform.base.job.dao.IJobCommandMapper;
import com.kcp.platform.base.job.dao.IJobMapper;
import com.kcp.platform.base.job.entity.Job;
import com.kcp.platform.base.job.entity.JobCommand;
import com.kcp.platform.common.log.Logger;
import com.kcp.platform.common.log.annotation.Log;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.log.core.SqlContextHolder;
import com.kcp.platform.core.service.BaseService;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.util.CodeGenerator;


/**
 * 功能说明：
 * 任务服务类，包括任务的控制及任务的配置。
 *
 */
@Service("jobService")
public class JobService extends BaseService {
    @Autowired
    private IJobMapper jobMapper;
    
    @Autowired
    private IJobCommandMapper jobCommandMapper;
   

    //任务记录管理操作--------------------------------------------------------------------------------------//
    
    public List<Job> findJob(Map<String,Object> queryMap){
    	return jobMapper.findJob(queryMap);
    }
    
    @Log(type=OperateLogType.QUERY, moduleName="任务调度", operateDesc="[查询] 查询任务信息", useSpel=false)
    public List<Job> findJobPaging(Map<String,Object> queryMap){
    	return jobMapper.findJobPaging(queryMap);
    }
    
    /**
     * 获取已经发布的任务
     * @return
     */
    public List<Job> getReleasedJobs() {
    	Map<String,Object> queryMap = new HashMap<String,Object>();
    	queryMap.put("released", CommonConst.YES);
        return jobMapper.findJob(queryMap);
    }
    
    /**
     * 根据ID获取任务
     * @param id
     * @return
     */
    public Job getJobById(String id) {
        return jobMapper.getJobById(id);
    }
    
    /**
     * 根据任务分组及任务名称获取对应的Job
     *
     * @param jobGroupName
     * @param jobName
     * @return
     */
    public Job getJobByFullName(String jobGroupName, String jobName) {
    	Job job = new Job();
    	job.setJobGroupName(jobGroupName);
    	job.setJobName(jobName);
        return jobMapper.getJobByFullName(job);
    }
    
    /**
     * 保存任务
     * @param job
     */
    @Log(type=OperateLogType.INSERT, moduleName="任务调度", operateDesc="'[新增] 新增[任务名称：'+#job.title+'] [周知标识：'+#job.jobName+']的任务信息'")
    public void saveJob(Job job) {
    	jobMapper.addJob(job);
    }

    /**
     * 修改任务
     * @param job
     */
    @Log(type=OperateLogType.UPDATE, moduleName="任务调度", operateDesc="'[修改] 修改[任务名称：'+#job.title+'] [周知标识：'+#job.jobName+']的任务信息'")
    public void updateJob(Job job) {
    	jobMapper.updateJob(job);
    }

    /**
     * 批量删除任务
     * @param jobIds
     */
    public void batchDeleteJob(List<String> jobIds) {
    	if(jobIds!=null){
    		for(String jobId:jobIds){
    			jobMapper.deleteJob(jobId);
    		}
    		String jobIdStr = StringUtils.join(jobIds.toArray(),",");
    		String sql = SqlContextHolder.getSql();
    		Logger.getInstance().addSysLog(OperateLogType.DELETE.value(), sql, "任务调度", "[删除] 删除任务[任务ID："+jobIdStr+"]");
    	}
    }

    /**
     * 判断任务周知标识是否已经存在
     * @param id
     * @param jobName
     * @return
     */
    public boolean isExistedJobName(String id,String jobName){
    	Job job = new Job();
    	job.setJobId(id);
    	job.setJobName(jobName);
    	int count = jobMapper.countJob(job);
    	if(count>0){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**
     * 判断任务标识是否已经存在
     * @param id
     * @param jobTitle
     * @return
     */
    public boolean isExistedJobTitle(String id,String jobTitle){
    	Job job = new Job();
    	job.setJobId(id);
    	job.setTitle(jobTitle);
    	int count = jobMapper.countJob(job);
    	if(count>0){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**
     * 获取当前的数据库时间
     *
     * @return
     */
    public Date getCurrDbDate() {
        return jobMapper.getCurrDbDate();
    }


    //--------------------------------------------------------------------------------------------------//

    //------任务控制操作------------------------------------------------------------------------------//

    /**
     * 更新任务的最后活动时间
     *
     * @param jobGroupName 任务组名称
     * @param jobName      任务名称
     */
    public void updateJobLastActiveTime(String jobGroupName, String jobName) {
    	Job job = new Job();
    	job.setJobGroupName(jobGroupName);
    	job.setJobName(jobName);
    	jobMapper.updateLastActiveTime(job);
    }
    
    /**
     * 启动任务
     * @param jobIdList
     */
    public void startJob(List<String> jobIdList) {
        if (jobIdList != null) {
            for (String jobId : jobIdList) {
                Job job = jobMapper.getJobById(jobId);
                job.setRunning();
                jobMapper.updateJob(job);
                //发布一条启动任务的命令
                JobCommand jobCommand = new JobCommand();
                jobCommand.setCommandId(CodeGenerator.getUUID32());
                jobCommand.setJobId(jobId);
                jobCommand.setJobGroupName(job.getJobGroupName());
                jobCommand.setJobName(job.getJobName());
                jobCommand.setCtrlCommand(JobCommand.CtrlCommand.START);
                jobCommand.setIssueTime(jobMapper.getCurrDbDate());
                jobCommandMapper.addJobCommand(jobCommand);
            }
            String jobIdStr = StringUtils.join(jobIdList.toArray(),",");
    		String sql = SqlContextHolder.getSql();
    		Logger.getInstance().addSysLog(OperateLogType.UPDATE.value(), sql, "任务调度", "[启动] 启动任务[任务ID："+jobIdStr+"]");
        }
    }

    /**
     * 停止任务
     * @param jobIdList
     */
    public void stopJob(List<String> jobIdList) {
        if (jobIdList != null) {
            for (String jobId : jobIdList) {
                Job job = jobMapper.getJobById(jobId);
                job.setStopped();
                jobMapper.updateJob(job);
                //发布一条停止任务的命令
                JobCommand jobCommand = new JobCommand();
                jobCommand.setCommandId(CodeGenerator.getUUID32());
                jobCommand.setJobId(jobId);
                jobCommand.setJobGroupName(job.getJobGroupName());
                jobCommand.setJobName(job.getJobName());
                jobCommand.setCtrlCommand(JobCommand.CtrlCommand.STOP);
                jobCommand.setIssueTime(jobMapper.getCurrDbDate());
                jobCommandMapper.addJobCommand(jobCommand);
            }
            String jobIdStr = StringUtils.join(jobIdList.toArray(),",");
    		String sql = SqlContextHolder.getSql();
    		Logger.getInstance().addSysLog(OperateLogType.UPDATE.value(), sql, "任务调度", "[停止] 停止任务[任务ID："+jobIdStr+"]");
        }
    }

    /**
     * 发布任务
     * @param jobIdList
     */
    public void releaseJob(List<String> jobIdList) {
        if (jobIdList != null) {
            for (String jobId : jobIdList) {
                Job job = jobMapper.getJobById(jobId);
                job.setReleased(CommonConst.YES);
                job.setRunning();
                jobMapper.updateJob(job);

                //发布一条启动任务的命令
                JobCommand jobCommand = new JobCommand();
                jobCommand.setCommandId(CodeGenerator.getUUID32());
                jobCommand.setJobId(jobId);
                jobCommand.setJobGroupName(job.getJobGroupName());
                jobCommand.setJobName(job.getJobName());
                jobCommand.setCtrlCommand(JobCommand.CtrlCommand.START);
                jobCommand.setIssueTime(jobMapper.getCurrDbDate());
                jobCommandMapper.addJobCommand(jobCommand);
            }
            String jobIdStr = StringUtils.join(jobIdList.toArray(),",");
    		String sql = SqlContextHolder.getSql();
    		Logger.getInstance().addSysLog(OperateLogType.UPDATE.value(), sql, "任务调度", "[发布] 发布任务[任务ID："+jobIdStr+"]");
        }
    }
    
    /**
     * 判断任务的最新命令状态，即获取{@link JobSchedulerManager#jobCommandLastTime}到当前数据库时间（包含）所产生的任务控制命令。
     * @return
     */
    public List<JobCommand> getLastJobCommands() {
        Date currTime = jobMapper.getCurrDbDate();
        Map<String,Object> queryMap = new HashMap<String,Object>();
        queryMap.put("startTime", JobSchedulerManager.jobCommandLastTime);
        queryMap.put("endTime", currTime);
        List<JobCommand> jobCommands = jobCommandMapper.findJobCommand(queryMap);
        JobSchedulerManager.jobCommandLastTime = currTime;
        return jobCommands;
    }
    
    /**
     * 删除一天前发布的任务控制命令
     */
    public void deleteJobComandIssuedBeforeOneDay(){
    	Date currTime = jobMapper.getCurrDbDate();
    	Date date = new Date(currTime.getYear(), currTime.getMonth(), currTime.getDate());
    	jobCommandMapper.deleteJobComandIssuedBeforeOneDay(date);
    }
}
