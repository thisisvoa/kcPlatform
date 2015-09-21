package com.casic27.platform.base.job.service;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.casic27.platform.base.job.JobSchedulerManager;
import com.casic27.platform.base.job.dao.IJobSchedulerMapper;
import com.casic27.platform.base.job.entity.JobScheduler;
import com.casic27.platform.common.log.annotation.Log;
import com.casic27.platform.common.log.annotation.OperateLogType;
import com.casic27.platform.core.service.BaseService;
import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.util.AssertUtils;
import com.casic27.platform.util.StringUtils;

import javax.annotation.PreDestroy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 任务调度服务器，包括任务调度的配置及装载和监控等操作。
 *
 */
@Service("jobSchedulerService")
public class JobSchedulerService extends BaseService{
	private static org.apache.commons.logging.Log logger = LogFactory.getLog(JobSchedulerService.class) ;
    /**
     * 判断为不活动的任务调度服务器
     */
    private static final int UNACTIVE_JOB_SCHEDULER_IN_SECONDS = 3 * 60;
    
    /**
     * 等待高低优先级任务调度服务装载的时间，单位为分
     */
    private static final int WAIT_HIGH_PRIO_MOUNT_IN_MINUTES = 5;

    @Autowired
    private IJobSchedulerMapper jobSchedulerMapper;
    /**
     * 获取所有的任务调度管理服务器
     * @return
     */
    @Log(type=OperateLogType.QUERY, moduleName="任务调度", operateDesc="[查询] 查询调度服务器信息", useSpel=false)
    public List<JobScheduler> getAllJobSchedulers() {
    	Map<String,Object> queryMap = new HashMap<String,Object>();
        return jobSchedulerMapper.findJobScheduler(queryMap);
    }

    /**
     * 保存任务调度器
     * @param jobScheduler
     */
    @Log(type=OperateLogType.INSERT, moduleName="任务调度", operateDesc="'[新增] 新增[地址：'+#jobScheduler.hostUrl+'] [优先级：'+#jobScheduler.priority+']的调度服务器信息'")
    public void saveJobScheduler(JobScheduler jobScheduler){
    	jobSchedulerMapper.addJobScheduler(jobScheduler);
    }

    /**
     * 修改任务调度器
     * @param jobScheduler
     */
    @Log(type=OperateLogType.UPDATE, moduleName="任务调度", operateDesc="'[修改] 修改[地址：'+#jobScheduler.hostUrl+'] [优先级：'+#jobScheduler.priority+']的调度服务器信息'")
    public void updateJobScheduler(JobScheduler jobScheduler) {
    	jobSchedulerMapper.updateJobScheduler(jobScheduler);
    }

    /**
     * 根据主键Id任务调度器
     * @param jobSchedulerId
     */
    public JobScheduler getJobSchedulerById(String jobSchedulerId) {
        return jobSchedulerMapper.getJobSchedulerById(jobSchedulerId);
    }

    /**
     * 删除调度器
     * @param schedulerIdList
     */
    @Log(type=OperateLogType.DELETE, moduleName="任务调度", operateDesc="[删除] 删除调度服务器信息", useSpel=false)
    public void batchDeleteJobScheduler(List<String> schedulerIdList) {
       if(schedulerIdList!=null){
    	   for(String id:schedulerIdList){
    		   jobSchedulerMapper.deleteJobScheduler(id);
    	   }
       }
    }
    
    /**
     * 判断调度服务器是否已经存在
     */
    public boolean isExistedHostUrl(String id,String hostUrl){
    	JobScheduler jobScheduler = new JobScheduler();
    	jobScheduler.setJobSchedulerId(id);
    	jobScheduler.setHostUrl(hostUrl);
    	int count  = jobSchedulerMapper.countJobScheduler(jobScheduler);
    	if(count>0){
    		return true;
    	}else{
    		return false;
    	}
    }
    /**
     * 判断调度服务器优先级是否已经存在
     */
    public boolean isExistedPriority(String id,int priority){
    	JobScheduler jobScheduler = new JobScheduler();
    	jobScheduler.setJobSchedulerId(id);
    	jobScheduler.setPriority(priority);
    	int count  = jobSchedulerMapper.countJobScheduler(jobScheduler);
    	if(count>0){
    		return true;
    	}else{
    		return false;
    	}
    }
    /**
     * 获取当前任务调度服务器
     * @return
     */
    public JobScheduler getCurrentJobScheduler(){
    	Map<String,Object> queryMap = new HashMap<String,Object>();
    	queryMap.put("currScheduler", "1");
    	List<JobScheduler> jobSchedulerList = jobSchedulerMapper.findJobScheduler(queryMap);
    	 if (jobSchedulerList!=null && jobSchedulerList.size() > 0) {
             return jobSchedulerList.get(0);
         } else {
             return null;
         }
    }
    
    /**
     * 在容器卸载前执行，将本任务调度服务{@link com.epgis.platform.module.job.JobSchedulerManager}从控制台解装载掉
     */
    @PreDestroy
    public void unmountJobScheduler() {
        if (JobSchedulerManager.isMounted()) {
        	 JobScheduler jobScheduler = getJobScheduler(JobSchedulerManager.getHostUrl(), JobSchedulerManager.getHostUrlAlias());
	    	 if (jobScheduler!=null) {
	    		 jobScheduler.setUnmounted();
	    		 jobSchedulerMapper.updateJobScheduler(jobScheduler);
	         }
        }
    }

    /**
     * 将ip:port的服务设置为当前的任务调度服务,同时将其它的任务调度设置为非当前调度服务
     */
    public void makeCurrentJobScheduler(String hostUrl, String hostUrlAlias) {
        List<String> hostUrlList = toHostUrlList(hostUrl, hostUrlAlias);
        jobSchedulerMapper.makeNotCurrentWhenOther(hostUrlList);
    }

    /**
     * 根据ip及port获取任务调度服务器
     * @return
     */
	public JobScheduler getJobScheduler(String hostUrl, String hostUrlAlias) {
		List<String> hostUrlList = toHostUrlList(hostUrl, hostUrlAlias);
		List<JobScheduler> jobSchedulerList = jobSchedulerMapper.getJobSchedulerByHostUrl(hostUrlList);
		if (jobSchedulerList != null && jobSchedulerList.size() > 0) {
			return jobSchedulerList.get(0);
		} else {
			return null;
		}
	}
    public int getCountOfCurrentJobScheduler(){
    	JobScheduler jobScheduler = new JobScheduler();
    	jobScheduler.setCurrScheduler(CommonConst.YES);
    	int count  = jobSchedulerMapper.countJobScheduler(jobScheduler);
    	return count;
    }
    
    public JobScheduler getHighestPriorityJobScheduler(){
    	List<JobScheduler> jobSchedulerList = jobSchedulerMapper.getHighestPriorityJobScheduler();
    	AssertUtils.isTrue(jobSchedulerList!=null || jobSchedulerList.size()==0, "任务调度服务器不存在");
    	AssertUtils.isTrue(jobSchedulerList.size() == 1, "任务调度服务器的优先级不能一样.");
    	return jobSchedulerList.get(0);
    }
    
    /**
     * 获取优先级最高的活动的任务调度管理器。
     *
     * @return
     */
    public JobScheduler getHighestPriorityActiveJobScheduler() {
        List<JobScheduler> jobSchedulers = jobSchedulerMapper.getHighestPriorityActiveJobScheduler();
        if (jobSchedulers != null && jobSchedulers.size() > 0) {
            return jobSchedulers.get(0);
        } else {
            return null;
        }
    }
    
    public int getCountOfActiveJobScheduler(){
    	JobScheduler jobScheduler = new JobScheduler();
    	jobScheduler.setActiveStatus(CommonConst.YES);
    	int count  = jobSchedulerMapper.countJobScheduler(jobScheduler);
    	return count;
    }
    
    /**
     * 装配任务调度管理服务器。
     * @param hostUrl
     * @param hostUrlAlias
     */
    public void mountJobScheduler(String hostUrl, String hostUrlAlias) {
        try {
        	jobSchedulerMapper.unmountUnactiveJobScheduler(UNACTIVE_JOB_SCHEDULER_IN_SECONDS);
            
            //将控制台的JobScheduler设置为装载状态
            JobScheduler jobScheduler = getJobScheduler(hostUrl, hostUrlAlias);

            //设置任务调度服务器的地址及装载状态
            JobSchedulerManager.setHostUrl(hostUrl);
            JobSchedulerManager.setHostUrlAlias(hostUrlAlias);
            JobSchedulerManager.setMounted(true);

            jobScheduler.setMounted();
            long currGlobalCount = getCountOfCurrentJobScheduler();
            if (currGlobalCount == 0) {
                JobScheduler highestPriJobScheduler = getHighestPriorityJobScheduler();
                //本任务调度服务是最高的优先级
                if (highestPriJobScheduler.isSameHostUrl(hostUrl, hostUrlAlias)) {
                    jobScheduler.setCurrScheduler(CommonConst.YES);
                    JobSchedulerManager.setCurrentGlobalJobScheduler(true);
                }
            }
            jobSchedulerMapper.updateJobScheduler(jobScheduler);
            //汇报本任务服务的最后活动时间
            List<String> hostUrlList = toHostUrlList(hostUrl, hostUrlAlias);
        	jobSchedulerMapper.updateLastActiveTime(hostUrlList);
        } catch (OptimisticLockingFailureException e) {
            logger.warn("任务调度服务装载状态更改时发生乐观锁异常");
            this.mountJobScheduler(hostUrl, hostUrlAlias);
        }
    }
    
    
    /**
     * 对任务调度服务器进行热切换
     */
    public void hotSwapJobScheduler() {
        try {
            if (JobSchedulerManager.isMounted()) {
                //判断本JobSchedulerManager在控制台服务列表中
                JobScheduler jobScheduler = getJobScheduler(
                        JobSchedulerManager.getHostUrl(),
                        JobSchedulerManager.getHostUrlAlias());

                if (jobScheduler != null) {//在列表中
                    //将最后活动时间超限的任务服务解装载
                	jobSchedulerMapper.unmountUnactiveJobScheduler(UNACTIVE_JOB_SCHEDULER_IN_SECONDS);
                	List<String> hostUrlList = toHostUrlList(JobSchedulerManager.getHostUrl(), JobSchedulerManager.getHostUrlAlias());
                    //汇报本任务服务的最后活动时间
                	jobSchedulerMapper.updateLastActiveTime(hostUrlList);

                    //判断是否存在当前全局任务调度服务
                    long currentCount = getCountOfCurrentJobScheduler();
                    if (currentCount > 0) {//存在
                        JobScheduler currJobScheduler = getCurrentJobScheduler();
                        if (JobSchedulerManager.isManagerOf(currJobScheduler)) {
                            JobSchedulerManager.setCurrentGlobalJobScheduler(true);
                        }else{
                            JobSchedulerManager.setCurrentGlobalJobScheduler(false);
                        }
                    } else {//不存在
                        if (System.currentTimeMillis() - JobSchedulerManager.getMountTime() > WAIT_HIGH_PRIO_MOUNT_IN_MINUTES * 60 * 1000) {
                            int activeCount = getCountOfActiveJobScheduler();
                            if (activeCount > 0) {
                                JobScheduler highestActiveJobScheduler = getHighestPriorityActiveJobScheduler();
                                if (highestActiveJobScheduler != null) {
                                    highestActiveJobScheduler.setCurrScheduler(CommonConst.YES);
                                    jobSchedulerMapper.updateJobScheduler(highestActiveJobScheduler);
                                    if (JobSchedulerManager.isManagerOf(highestActiveJobScheduler)) {
                                        JobSchedulerManager.setCurrentGlobalJobScheduler(false);
                                    }
                                }
                            } else {
                                logger.error("系统启动10分钟后，还没有发现可用的全局任务调度服务节点！");
                            }
                        }
                    }
                } else {//不存列表中
                    if (JobSchedulerManager.isCurrentGlobalJobScheduler()) {
                        JobSchedulerManager.setCurrentGlobalJobScheduler(false);
                        if (logger.isInfoEnabled()) {
                            logger.info("任务调度服务器[" + JobSchedulerManager.getHostUrl() + "]设置为非当前全局调度器。");
                        }
                    }
                    JobSchedulerManager.setMounted(false);
                }
            } else {
                if (logger.isInfoEnabled()) {
                    logger.info("本服务器还未装配成全局任务调度服务，不参于任务调度的热切换.");
                }
            }
        } catch (OptimisticLockingFailureException e) {
            logger.warn("任务调度服务装载状态更改时发生乐观锁异常");
            this.hotSwapJobScheduler();
        }
    }
    
    private List<String> toHostUrlList(String hostUrl, String hostUrlAlias) {
        List<String> hostUrls = new ArrayList<String>();
        if (StringUtils.isNotEmpty(hostUrl)) {
            hostUrls.add(hostUrl);
        }
        if (StringUtils.isNotEmpty(hostUrlAlias)) {
            hostUrls.add(hostUrlAlias);
        }
        return hostUrls;
    }
}