package com.kcp.platform.base.job.entity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.util.Assert;

import com.kcp.platform.core.entity.BaseEntity;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.util.DateUtils;
import com.kcp.platform.util.StringUtils;


/**
 * 调度任务的配置及管理对象,它对象T_JOB表，匹配关系为jobGroupName.jobName=Job的bean名称
 * 对lastActiveTime进行更新时，不会更改updateTime的值，对于其它字段值更改时，会更改updateTime的值。
 */
public class Job extends BaseEntity {

    /**
     * 表示马上开始或永不结束的时间
     */
    private static final String ZERO = "0";
    
    /**
     * 分隔符
     */
    private static final char SPLITOR = '#';
    
    /**
     * 主键ID
     */
    private String jobId;

    /**
     * 任务组名
     */
    private String jobGroupName;

    /**
     * 周知标识
     */
    private String jobName;
    
    /**
     * 任务标识
     */
    private String title;
    
    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
    
    /**
     * 任务调度触发器参数配置串,其格式如下所示：
     * <pre>
     * [{
     *  type:"simple|cron",//触发器类型，simple对应SimpleTrigger  cron对应CronTrigger
     *   repeatInterval:"10",//执行时间隔,当type=simple时有意义
     *   cronExpr:"0 0 12 * * ? "Cron格式的表达式，当type=cron时有意义
     *  },
     *  ...
     * ]
     * </pre>
     */
    private String triggerParam;
    
    /**
     * 活动状态  0：停止，1：活动 
     */
    private String activeStatus;
    
    /**
     * 发布状态  0：未发布 1：已发布
     */
    private String released;
    
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
     * 备注
     */
    private String remark;
    
    public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
	 /**
     * 获取任务对应的组名
     */
    public String getJobGroupName() {
        if (StringUtils.isEmpty(this.jobGroupName)) {
            return Scheduler.DEFAULT_GROUP;
        } else {
            return this.jobGroupName;
        }
    }
    
	public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }
	
	 /**
     * 获取任务对应的名称
     */
    public String getJobName() {
        return this.jobName;
    }
    
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public String getTriggerParam() {
        return triggerParam;
    }

    public void setTriggerParam(String triggerParam) {
        this.triggerParam = triggerParam;
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

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }
    
    /**
     * 设置为为发布
     */
    public void setUnReleased() {
        this.setReleased(CommonConst.NO);
        setActiveStatus(ActiveStatus.ACTIVE);
    }
   

    /**
     * 获取任务的全限定名
     *
     * @return
     */
    public String getJobFullName() {
        return this.getJobGroupName() + "." + this.getJobName();
    }

    /**
     * 获取任务所对应的Trigger列表
     *
     * @return
     */
    public List<Trigger> getTriggers() {
        List<TriggerConfig> triggerConfigs = TriggerConfig.fromJsonStr(this.getTriggerParam());
        List<Trigger> triggers = new ArrayList<Trigger>();
        int index = 1;
        for (TriggerConfig triggerConfig : triggerConfigs) {
            Trigger trigger = triggerConfig.toTrigger();

            trigger.setJobGroup(getJobGroupName());
            trigger.setJobName(getJobName());
            
            trigger.setGroup(getJobGroupName());
            trigger.setName(getJobName()+SPLITOR+index);

            if (isStartTimeSetting()) {
            	Date date = getStartTimeAsDate();
            	Date now = new Date();
            	if(date.getTime()<now.getTime()){
            		trigger.setStartTime(now);
            	}else{
            		trigger.setStartTime(getStartTimeAsDate());
            	}
            }else{
                trigger.setStartTime(new Date());
            }
            if (isEndTimeSetting()) {
                if( getEndTimeAsDate().compareTo(trigger.getStartTime()) > 0) {
                    trigger.setEndTime(getEndTimeAsDate());
                }else{
                    trigger.setEndTime(new Date());
                }
            }
            triggers.add(trigger);
            index++;
        }
        return triggers;
    }

    /**
     * 返回以Date表示的开始时间
     *
     * @return
     */
    private Date getStartTimeAsDate() {
        return DateUtils.parseStrint2Date(this.getStartTime(), "yyyyMMddhhmmss");
    }

    /**
     * 是否设置了开始时间
     *
     * @return
     */
    private boolean isStartTimeSetting() {
        return StringUtils.isNotEmpty(this.startTime) && !ZERO.equals(this.startTime);
    }

    /**
     * 是否设置了结束时间
     *
     * @return
     */
    private boolean isEndTimeSetting() {
        return StringUtils.isNotEmpty(this.endTime) && !ZERO.equals(this.endTime);
    }

    /**
     * 返回以Date表示的结束时间
     *
     * @return
     */
    private Date getEndTimeAsDate() {
        return DateUtils.parseStrint2Date(this.endTime, "yyyyMMddhhmmss");
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
     * 　　触发器配置参数，对于Simple类型的Trigger，只配置间隔时间（单位为秒），对于Cron类型的Trigger，
     * 只配置Cron表达式。
     */
    public static class TriggerConfig {

        //SimpleTrigger的配置类型名称
        private static final String SIMPLE_TRIGGER = "simple";

        //CronTrigger的配置类型名称
        private static final String CRON_TRIGGER = "cron";


        /**
         * 触发器类型的名称
         */
        private String type;

        /**
         * 执行间隔，单位为秒（当type=simple时有意义）
         */
        private int repeatInterval;

        /**
         * 表达式（当type=cronExpr时有意义）
         */
        private String cronExpr;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getRepeatInterval() {
            return repeatInterval;
        }

        public void setRepeatInterval(int repeatInterval) {
            this.repeatInterval = repeatInterval;
        }


        public String getCronExpr() {
            return cronExpr;
        }

        public void setCronExpr(String cronExpr) {
            this.cronExpr = cronExpr;
        }

        public Trigger toTrigger() {
            if (isSimpleTrigger()) {
                return toSimpleTrigger();
            } else {
                return toCronTrigger();
            }

        }

        /**
         * 根据JSON表达式直接返回TriggerConfig列表，正确的JSON表达式形如：
         * <pre>
         * [{
         *  type:"simple|cron",//触发器类型，simple对应SimpleTrigger  cron对应CronTrigger
         *   repeatInterval:"10",//执行时间隔,当type=simple时有意义
         *   cronExpr:"0 0 12 * * ? "Cron格式的表达式，当type=cron时有意义
         *  },
         *  ...
         * ]
         * </pre>
         *
         * @param jsonStr
         * @return
         */
        public static List<TriggerConfig> fromJsonStr(String jsonStr) {
            Assert.notNull(jsonStr, "jsonStr不能为空");
            JSONArray jsonArray = JSONArray.fromObject(jsonStr);
            return (List<TriggerConfig>) JSONArray.toCollection(jsonArray, TriggerConfig.class);
        }


        /**
         * 是否是Simple类型的Trigger
         *
         * @return
         */
        private boolean isSimpleTrigger() {
            return SIMPLE_TRIGGER.equalsIgnoreCase(this.type);
        }


        /**
         * 是否是Cron类型的Trigger
         *
         * @return
         */
        private boolean isCronTrigger() {
            return CRON_TRIGGER.equalsIgnoreCase(this.type);
        }

        /**
         * 创建类型为cronTrigger的触发器
         *
         */
        private CronTrigger toCronTrigger() {
            CronTrigger cronTrigger = new CronTrigger();
            try {
                cronTrigger.setCronExpression(getCronExpr());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return cronTrigger;
        }

        /**
         * 创建类型为simpleTrigger的触发器
         *
         */
        private SimpleTrigger toSimpleTrigger() {
            SimpleTrigger simpleTrigger = new SimpleTrigger();
            simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
            simpleTrigger.setRepeatInterval(1000 * getRepeatInterval());
            return simpleTrigger;
        }

    }
}
