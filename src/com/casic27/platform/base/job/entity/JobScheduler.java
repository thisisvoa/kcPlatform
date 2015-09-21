package com.casic27.platform.base.job.entity;

import java.util.Date;

import com.casic27.platform.core.entity.BaseEntity;
import com.casic27.platform.sys.constants.CommonConst;

/**
 * 任务调度管理器状态记录对象，对应T_JOB_SCHEDULER表。
 *
 */
public class JobScheduler extends BaseEntity {
    /**
     * 
     */
    private String jobSchedulerId;
    
    /**
     * 任务调度服务器的地址，格式为ip:port
     */
    private String hostUrl;
    
    /**
     * 优先级 从1~10
     */
    private int priority;
    
    /**
     * 装配状态
     */
    private String mountStatus;
    
    /**
     * 活动状态  0：停止，1：活动 
     */
    private String activeStatus;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 修改时间
     */
    private Date updateTime;
    
    /**
     * 任务的最后活动时间
     */
    private Date lastActiveTime;

    /**
     * 当前调度节点 0：不是 1：是
     */
    private String currScheduler;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 版本号
     */
    private int version;

    public String getJobSchedulerId() {
        return jobSchedulerId;
    }

    public void setJobSchedulerId(String jobSchedulerId) {
        this.jobSchedulerId = jobSchedulerId;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCurrScheduler() {
        return currScheduler;
    }

    public void setCurrScheduler(String currScheduler) {
        this.currScheduler = currScheduler;
    }

    public String getMountStatus() {
        return mountStatus;
    }

    public void setMountStatus(String mountStatus) {
        this.mountStatus = mountStatus;
    }

    /**
     * 设置为解装载
     */
    public void setMounted() {
        setMountStatus(MountStatus.YES);
        setActiveStatus(ActiveStatus.ACTIVE);
    }

    public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getLastActiveTime() {
		return lastActiveTime;
	}

	public void setLastActiveTime(Date lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setUnmounted() {
        setActiveStatus(ActiveStatus.UNACTIVE);
        setMountStatus(MountStatus.NO);
        setCurrScheduler(CommonConst.NO);
    }

    /**
     * 判断本任务调度服务的地址是否和指定的相同
     *
     * @param hostUrl
     * @param hostUrlAlias
     * @return
     */
    public boolean isSameHostUrl(String hostUrl, String hostUrlAlias) {
    	boolean  flag1 = (hostUrl != null && hostUrl.equalsIgnoreCase(this.hostUrl));
    	boolean flag2 = (hostUrlAlias != null && hostUrlAlias.equalsIgnoreCase(this.hostUrl));
        return (flag1||flag2);
    }

    /**
     * 任务调度管理服务器是否已经装载
     *
     * @return
     */
    public boolean isJobSchedulerMounted() {
        return MountStatus.YES.equals(this.mountStatus);
    }
    /**
     * 任务调度管理服务器是否已经运行
     *
     * @return
     */
    public boolean isActive() {
        return ActiveStatus.ACTIVE.equals(this.activeStatus);
    }

    /**
     * 设置任务调度管理服务器为运行状态
     */
    public void setRunning() {
        this.activeStatus = ActiveStatus.ACTIVE;
    }

    /**
     * 设置任务调度服务器为停止状态
     */
    public void setStopped() {
        this.activeStatus = ActiveStatus.UNACTIVE;
    }

    /**
     * 任务装配
     */
    protected static class MountStatus {
        /**
         * 未装配
         */
        public static final String NO = "0";
        /**
         * 已装配
         */
        public static final String YES = "1";
    }
}
