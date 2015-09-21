package com.casic27.platform.base.job.core;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.casic27.platform.base.job.AbstractTriggerBeanJob;
import com.casic27.platform.base.job.JobSchedulerManager;
import com.casic27.platform.base.job.entity.JobScheduler;
import com.casic27.platform.base.job.service.JobSchedulerService;


/**
 * 任务调度服务识别器。JobSchedulerRecognizer定时运行，获取{@link com.casic27.platform.base.job.entity.JobScheduler}列表，
 * 然后向{@link com.casic27.platform.base.job.entity.JobScheduler}列表所指定的服务发送请求，请求受理者为目标服务器上的
 * {@link com.casic27.platform.base.job.web.ServerHostRecognizer}。当接收到请求后，即可分析目标服务器的服务的地址，并设置
 * 本任务调度服务器的地址，并根据条件设置当前任务调度服务。
 */
@Component("jobSchedulerRecognizer")
public class JobSchedulerRecognizer extends AbstractTriggerBeanJob {
    
    /**
     * 监控任务执行状态周期，单位为秒
     */
    private static final int PERIOD_IN_SECONDS = 60;

    /**
     * 本任务调度识别器的延迟启动时间，单位为秒
     */
    private static final long DELAY_IN_SECONDS = 30;

    /**
     * 最大识别次数
     */
    private static final int MAX_RECOGNIZER_NUM = 40;

    /**
     * 正确返回的响应词
     */
    public static final String RESPONSE_WORD = "OK";

    /**
     * 请求接收的地址
     */
    public static final String JOB_SCHEDULER_RECOGINZER_URI = "/job/serverHostRecoginzer.html";

    @Autowired
    private JobSchedulerService jobSchedulerService;

    @Autowired
    @Qualifier("taskExecutor")
    private TaskExecutor taskExecutor;

    private HttpClient HTTP_CLIENT = new DefaultHttpClient();

    /**
     * 运行次数
     */
    private int runTimes = 0;

    /**
     * 延迟{@link #DELAY_IN_SECONDS}秒执行，每{@link # PERIOD_IN_SECONDS }秒执行一次
     *
     * @return
     */
    public List<Trigger> getTriggers() {
        Trigger trigger = new SimpleTrigger(this.getJobName(), this.getJobGroupName(),
                MAX_RECOGNIZER_NUM, (long) PERIOD_IN_SECONDS * 1000);
        trigger.setStartTime(new Date(System.currentTimeMillis() + (DELAY_IN_SECONDS * 1000)));
        return asTriggerList(trigger);
    }

    /**
     * 任务调度服务识别器定期重新识别，在控制台上删除或更改任务调度服务器后，
     *
     * @param jobExecutionContext
     */
    public void execute(JobExecutionContext jobExecutionContext) {
        if (!JobSchedulerManager.isMounted()) {
            String info = "第[" + (++runTimes) + "]次运行任务调度服务地址识别器...";
            if (logger.isInfoEnabled()) {
                logger.info(info);
            }
            this.taskExecutor.execute(new RecoginzerRequestSender(this));
            if (logger.isInfoEnabled()) {
                logger.info("任务调度服务识别器执行完成.");
            }
        }
    }

    /**
     * 发送识别请求
     */
    private void sendRecoginzerRequest() {
        List<JobScheduler> jobSchedulers = jobSchedulerService.getAllJobSchedulers();
        for (JobScheduler jobScheduler : jobSchedulers) {
            String jobSchedulerHostUrl = getJobSchedulerHostUrl(jobScheduler);
            HttpGet getMethod = null;
            try {
                getMethod = new HttpGet(jobSchedulerHostUrl);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String responseStr = HTTP_CLIENT.execute(getMethod, responseHandler);  
                if (RESPONSE_WORD.equals(responseStr)) {
                    logger.info(jobSchedulerHostUrl + "返回响应.");
                } else {
                    logger.info(jobSchedulerHostUrl + "未返回响应.");
                }
            } catch (IOException e) {
                logger.warn("任务调度服务识别器向" + jobSchedulerHostUrl + "发送请求时发生错误。");
            } finally {
                getMethod.releaseConnection();
            }
        }
    }


    /**
     * 组装目标任务调度管理服务器的URL地址
     *
     * @param jobScheduler
     * @return
     */
    private String getJobSchedulerHostUrl(JobScheduler jobScheduler) {
        StringBuilder sb = new StringBuilder();
        sb.append(jobScheduler.getHostUrl());
        sb.append(JOB_SCHEDULER_RECOGINZER_URI);
        return sb.toString();
    }

    @Override
    public boolean isTopPriority() {
        return true;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

    @Override
    public boolean isNeedLog() {
        return false;
    }

    /**
     * 以异步的方式执行识别请求
     */
    private class RecoginzerRequestSender implements Runnable {
        private JobSchedulerRecognizer jobSchedulerRecognizer;

        public RecoginzerRequestSender(JobSchedulerRecognizer jobSchedulerRecognizer) {
            this.jobSchedulerRecognizer = jobSchedulerRecognizer;
        }

        public void run() {
            this.jobSchedulerRecognizer.sendRecoginzerRequest();
        }
    }
}
