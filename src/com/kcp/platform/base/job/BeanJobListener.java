package com.kcp.platform.base.job;

import org.quartz.JobListener;

/**
 * 实现该接口可以对某个具体的任务进行监听，其匹配规则为：
 * <pre>
 *     BeanJobListener#getJobGroupName() == Job#getJobGroupName() && 
 *     BeanJobListener#getJobName() == Job#getJobName()
 * </pre>
 */
public interface BeanJobListener extends JobListener{
    /**
     * 获取匹配的任务分组名
     * @return
     */
     String getJobGroupName();

    /**
     * 获取匹配的任务名
     * @return
     */
     String getJobName();
}
