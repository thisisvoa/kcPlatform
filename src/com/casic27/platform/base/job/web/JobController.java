package com.casic27.platform.base.job.web;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.casic27.platform.base.job.entity.Job;
import com.casic27.platform.base.job.entity.JobRunLog;
import com.casic27.platform.base.job.entity.JobScheduler;
import com.casic27.platform.base.job.service.JobLogService;
import com.casic27.platform.base.job.service.JobSchedulerService;
import com.casic27.platform.base.job.service.JobService;
import com.casic27.platform.core.exception.BusinessException;
import com.casic27.platform.core.page.Page;
import com.casic27.platform.core.page.PageContextHolder;
import com.casic27.platform.core.web.BaseMultiActionController;
import com.casic27.platform.ui.model.GridData;
import com.casic27.platform.util.AjaxJsonResponse;
import com.casic27.platform.util.CodeGenerator;
import com.casic27.platform.util.DateUtils;
import com.casic27.platform.util.JsonParser;
import com.casic27.platform.util.StringUtils;


/**
 * 
 */
@Controller
@RequestMapping("job")
public class JobController extends BaseMultiActionController {


    @Autowired
    private JobService jobService;

    @Autowired
    private JobSchedulerService jobSchedulerService;

    @Autowired
    private JobLogService jobLogService;

     /**
     * 显示任务调度控制台功能界面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("toMain")
    public ModelAndView toMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("common/job/jobControlMain");
    }
    /**
     * 任务调度服务器维护界面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("toJobSchedulerMain")
    public ModelAndView toJobSchedulerMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("common/job/JobSchedulerMain");
    }
    
    /**
     * 任务维护界面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("toJobMain")
    public ModelAndView toJobMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("common/job/jobMain");
    }
    
    /**
     * 任务日志界面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("toJobRunLog")
    public ModelAndView toJobRunLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("common/job/jobRunLogMain");
    }
    
    /**
     * 调度服务器列表
     * @param request
     * @return
     */
    @RequestMapping("jobSchedulerList")
    public @ResponseBody GridData jobSchedulerList(HttpServletRequest request){
    	List<JobScheduler> jobSchedulerList = jobSchedulerService.getAllJobSchedulers();
    	return new GridData(jobSchedulerList);
    }
    /**
     * 添加任务调度服务器页面
     * @return
     */
    @RequestMapping("toJobSchedulerAdd")
    public ModelAndView toJobSchedulerAdd(HttpServletRequest request){
    	return new ModelAndView("common/job/JobSchedulerEdit")
    				.addObject("type", "add");
    }
    
    /**
     * 添加任务调度服务器页面
     * @return
     */
    @RequestMapping("addJobScheduler")
    public @ResponseBody void addJobScheduler(HttpServletRequest request){
    	String hostUrl = StringUtils.getNullBlankStr(request.getParameter("hostUrl"));
    	String priorityStr = StringUtils.getNullBlankStr(request.getParameter("priority"));
    	int priority = Integer.valueOf(priorityStr);
    	String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));
    	JobScheduler jobScheduler = new JobScheduler();
    	jobScheduler.setHostUrl(hostUrl);
    	jobScheduler.setPriority(priority);
    	jobScheduler.setJobSchedulerId(CodeGenerator.getUUID32());
        jobScheduler.setCreateTime(new Date());
        jobScheduler.setRemark(remark);
        jobScheduler.setUnmounted();
        jobSchedulerService.saveJobScheduler(jobScheduler);
    }
    
    @RequestMapping("toJobSchedulerUpdate")
    public ModelAndView toJobSchedulerUpdate(HttpServletRequest request){
    	String jobSchedulerId = StringUtils.getNullBlankStr(request.getParameter("jobSchedulerId"));
    	JobScheduler jobScheduler = jobSchedulerService.getJobSchedulerById(jobSchedulerId);
    	if(jobScheduler==null){
    		throw new BusinessException("任务调度服务器不存在！");
    	}
    	return new ModelAndView("common/job/JobSchedulerEdit")
    				.addObject("type", "update")
    				.addObject("jobScheduler", jobScheduler);
    }
    
    @RequestMapping("updateJobScheduler")
    public @ResponseBody void updateJobScheduler(HttpServletRequest request){
    	String jobSchedulerId = StringUtils.getNullBlankStr(request.getParameter("jobSchedulerId"));
    	String hostUrl = StringUtils.getNullBlankStr(request.getParameter("hostUrl"));
    	String priorityStr = StringUtils.getNullBlankStr(request.getParameter("priority"));
    	int priority = Integer.valueOf(priorityStr);
    	String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));
    	JobScheduler jobScheduler = jobSchedulerService.getJobSchedulerById(jobSchedulerId);
    	//如果更改了URL地址，则必须将其状态重置为初始态，即装配状态为“未装配”，活动状态为“停止”，当前调度器为“否”。
        if(!jobScheduler.getHostUrl().equals(hostUrl)) {
        	jobScheduler.setUnmounted();
        }
    	jobScheduler.setHostUrl(hostUrl);
    	jobScheduler.setPriority(priority);
    	jobScheduler.setRemark(remark);
    	jobSchedulerService.updateJobScheduler(jobScheduler);
    }
    
     /**
     * 删除任务调度器<br>
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("deleteJobScheduler")
    public @ResponseBody void deleteJobScheduler(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String ids = request.getParameter("ids");
        List<String> idList = StringUtils.arrayToList(ids.split(","));
        jobSchedulerService.batchDeleteJobScheduler(idList);
    }
    
    /**
     * 调度服务器切换
     * @param request
     * @return
     */
    @RequestMapping("toJobSchedulerSwitch")
    public ModelAndView toJobSchedulerSwitch(HttpServletRequest request){
    	JobScheduler curJobScheduler = jobSchedulerService.getCurrentJobScheduler();
    	return new ModelAndView("common/job/JobSchedulerSwitch")
    					.addObject("curJobScheduler", curJobScheduler);
    }
    
    
    /**
     * 调度服务器下拉列表
     * @param request
     * @return
     */
    @RequestMapping("jobSchedulerComboList")
    public @ResponseBody List<JobScheduler> jobSchedulerComboList(HttpServletRequest request){
    	List<JobScheduler> jobSchedulerList = jobSchedulerService.getAllJobSchedulers();
    	jobSchedulerList = jobSchedulerList==null?new ArrayList<JobScheduler>():jobSchedulerList;
    	JobScheduler curJobScheduler = jobSchedulerService.getCurrentJobScheduler();
		Iterator<JobScheduler> it = jobSchedulerList.iterator();
    	while(it.hasNext()){
    		JobScheduler jobScheduler = it.next();
    		if(!jobScheduler.isActive()){
    			it.remove();
    		}else{
				if (curJobScheduler != null
						&& curJobScheduler.getJobSchedulerId().equals(
								jobScheduler.getJobSchedulerId())) {
					it.remove();
				}
    		}
    	}
    	return jobSchedulerList;
    }
    
    /**
     * 切换任务调度器
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("switchJobScheduler")
    public void switchJobScheduler(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String toHostUrl = request.getParameter("toHostUrl");
        jobSchedulerService. makeCurrentJobScheduler(toHostUrl, null);
    }
    /**
     * 验证调度服务器地址是否唯一
     * @param request
     * @return
     */
    @RequestMapping("validateHostUrl")
    public @ResponseBody Object[] validateHostUrl(HttpServletRequest request){
    	String jobSchedulerId = StringUtils.getNullBlankStr(request.getParameter("jobSchedulerId"));
		String hostUrl = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String filedId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		boolean isExist = jobSchedulerService.isExistedHostUrl(jobSchedulerId, hostUrl);
		Object[] result = new Object[2];
		result[0] = filedId;
		result[1] = !isExist;
		return result;
    }
    /**
     * 验证优先级是否唯一
     * @param request
     * @return
     */
    @RequestMapping("validatePriority")
    public @ResponseBody Object[] validatePriority(HttpServletRequest request){
    	String jobSchedulerId = StringUtils.getNullBlankStr(request.getParameter("jobSchedulerId"));
		String priorityStr = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		String filedId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		boolean isExist = jobSchedulerService.isExistedPriority(jobSchedulerId, Integer.valueOf(priorityStr));
		Object[] result = new Object[2];
		result[0] = filedId;
		result[1] = !isExist;
		return result;
    }
    
    @RequestMapping("jobList")
    public @ResponseBody GridData jobList(HttpServletRequest request){
    	String jobGroupName = StringUtils.getNullBlankStr(request.getParameter("jobGroupName"));
    	String jobName = StringUtils.getNullBlankStr(request.getParameter("jobName"));
    	String title = StringUtils.getNullBlankStr(request.getParameter("title"));
    	Map<String,Object> queryMap = new HashMap<String,Object>();
    	queryMap.put("jobGroupName", jobGroupName);
    	queryMap.put("jobName", jobName);
    	queryMap.put("title", title);
    	List<Job> jobList = jobService.findJobPaging(queryMap);
    	Page page = PageContextHolder.getPage();
    	return new GridData(jobList, page.getPage(),page.getTotalPages(), page.getTotalItems());
    }
    /**
     * 新增任务页面
     * @param request
     * @return
     */
    @RequestMapping("toJobAdd")
    public ModelAndView toJobAdd(HttpServletRequest request){
    	return new ModelAndView("common/job/jobEdit")
    				.addObject("type", "add");
    }
    
    @RequestMapping("addJob")
    public @ResponseBody void addJob(HttpServletRequest request){
    	Job job = assembleJob(request);
    	job.setJobId(CodeGenerator.getUUID32());
        job.setCreateTime(new Date());
        job.setUnReleased();
        jobService.saveJob(job);
    }
    
    
    /**
     * 新增任务页面
     * @param request
     * @return
     */
    @RequestMapping("toJobUpdate")
    public ModelAndView toJobUpdate(HttpServletRequest request){
    	String jobId = StringUtils.getNullBlankStr(request.getParameter("jobId"));
    	Job job = jobService.getJobById(jobId);
    	if(job==null){
    		throw new BusinessException("任务不存在!");
    	}
    	return new ModelAndView("common/job/jobEdit")
    				.addObject("type", "update")
    				.addObject("job", job);
    }
    
    @RequestMapping("updateJob")
    public @ResponseBody void updateJob(HttpServletRequest request){
    	Job job = assembleJob(request);
    	Job tempJob = jobService.getJobById(job.getJobId());
    	job.setCreateTime(tempJob.getCreateTime());
        job.setReleased(tempJob.getReleased());
        job.setActiveStatus(tempJob.getActiveStatus());
        jobService.updateJob(job);
    }
     /***
     * 显示Cron表达式说明页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("showCronInfoForm")
    public ModelAndView showCronInfoForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("common/job/cronInfo");
    }
    
    /**
     * 删除任务<br>
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("deleteJob")
    public @ResponseBody void deleteJob(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String ids = request.getParameter("ids");
        List<String> idList = StringUtils.arrayToList(ids.split(","));
        jobService.batchDeleteJob(idList);

    }
    /**
     * 启动任务
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("startJob")
    public @ResponseBody void startJob(HttpServletRequest request,HttpServletResponse response) throws Exception {
         String ids = request.getParameter("ids");
         List<String> idList = StringUtils.arrayToList(ids.split(","));
         jobService.startJob(idList);
    }
    /**
     * 发布任务
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("releaseJob")
    public @ResponseBody void releaseJob(HttpServletRequest request,HttpServletResponse response) throws Exception {
         String ids = request.getParameter("ids");
         List<String> idList = StringUtils.arrayToList(ids.split(","));
         jobService.releaseJob(idList);
    }
    /**
     * 停止任务
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("stopJob")
    public @ResponseBody void stopJob(HttpServletRequest request,HttpServletResponse response) throws Exception {
         String ids = request.getParameter("ids");
         List<String> idList = StringUtils.arrayToList(ids.split(","));
         jobService.stopJob(idList);
    }

    private Job assembleJob(HttpServletRequest request){
    	String jobId = StringUtils.getNullBlankStr(request.getParameter("jobId"));
    	String jobGroupName = StringUtils.getNullBlankStr(request.getParameter("jobGroupName"));
    	String jobName = StringUtils.getNullBlankStr(request.getParameter("jobName"));
    	String title = StringUtils.getNullBlankStr(request.getParameter("title"));
    	String startTime = StringUtils.getNullBlankStr(request.getParameter("startTime"));
    	String endTime = StringUtils.getNullBlankStr(request.getParameter("endTime"));
    	String triggerParam = StringUtils.getNullBlankStr(request.getParameter("triggerParam"));
    	String remark = StringUtils.getNullBlankStr(request.getParameter("remark"));
    	Job job = new Job();
    	job.setJobId(jobId);
    	job.setJobGroupName(jobGroupName);
    	job.setJobName(jobName);
    	job.setTitle(title);
    	job.setStartTime(startTime);
    	job.setEndTime(endTime);
    	job.setTriggerParam(triggerParam);
    	job.setRemark(remark);
    	return job;
    }
    
    /**
     * 运行日志列表
     * @param request
     * @return
     */
    @RequestMapping("jobRunLogList")
    public @ResponseBody GridData jobRunLogList(HttpServletRequest request){
    	String jobName = StringUtils.getNullBlankStr(request.getParameter("jobName"));
    	String scheduler = StringUtils.getNullBlankStr(request.getParameter("scheduler"));
    	String resultType = StringUtils.getNullBlankStr(request.getParameter("resultType"));
    	Date beginTime = DateUtils.parseStrint2Date(request.getParameter("beginTime"), "yyyy-MM-dd hh:mm:ss");
    	Date endTime = DateUtils.parseStrint2Date(request.getParameter("endTime"), "yyyy-MM-dd hh:mm:ss");
    	Map<String,Object> queryMap = new HashMap<String,Object>();
    	queryMap.put("jobName", jobName);
    	queryMap.put("scheduler", scheduler);
    	queryMap.put("resultType", resultType);
    	queryMap.put("beginTime", beginTime);
    	queryMap.put("endTime", endTime);
    	List<JobRunLog> result = jobLogService.findJobRunLogPaging(queryMap);
    	Page page = PageContextHolder.getPage();
    	return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
    }
    
    /***
     * 显示运行日志详细信息页面
     * @param request
     * @param response
     * @return                                     
     * @throws Exception
     */
    @RequestMapping("showJobRunLog")
    public ModelAndView showJobRunLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        JobRunLog jobRunLog = jobLogService.getJobRunLogById(id);
        return new ModelAndView("common/job/jobRunLogView").addObject("jobRunLog", jobRunLog);
    }                                                    


    /**
     * 删除运行日志 <br>
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("deleteJobRunLog")
    public void deleteJobRunLog(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String deleteType = request.getParameter("deleteType");
        String jobName = request.getParameter("jobName");
        if(null!=jobName){
        	jobName = jobName.trim();
        }
        String jobId = request.getParameter("jobId");
        if ("DeleteByPrimaryKey".equals(deleteType)) {
            String ids = request.getParameter("ids");
            List<String> idList = StringUtils.arrayToList(ids.split(","));
            jobLogService.batchDeleteJobRunLog(idList);
        } else if ("DeleteAll".equals(deleteType)) {
            jobLogService.deleteAllJobRunLog(StringUtils.isNotEmpty(jobId) ? jobName : null);
        } else if ("DeleteByTime".equals(deleteType)) {
            String fromTime = request.getParameter("fromTime");
            String toTime = request.getParameter("toTime");
            Date beginTime = DateUtils.parseStringformatDate(fromTime, "yyyy-MM-dd hh:mm:ss");
            Date endTime = DateUtils.parseStringformatDate(toTime, "yyyy-MM-dd hh:mm:ss");
            jobLogService.batchDeleteJobRunLog(StringUtils.isNotEmpty(jobId) ? jobName : null,beginTime, endTime);
        }
    }

    /***
     * 显示时间段选择页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView showSelectTime(HttpServletRequest request, HttpServletResponse response) throws Exception {         
        return new ModelAndView("job/select_time");
    }
}
