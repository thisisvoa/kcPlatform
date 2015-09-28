package com.kcp.platform.base.job;

import org.quartz.Trigger;

import java.util.ArrayList;
import java.util.List;

/**
 *　　调度任务由两部分组成，一为“执行逻辑”；二为“调度规则”。执行逻辑可以在<code>execute()</code>方法中
 * 定义。而调度规则通过<code>getTriggers()</code>方法中定义。
 *    <p>实现类必须实现<code>execute()</code>和<code>getTriggers()</code>这两个方法。
 * 　 <p>如果{@link JobSchedulerManager}中拥有和任务名匹配的触发器（即触发器的任务分组名和任务名和Job的相等）
 * 当<code>isTopPriority()</code>返回<code>false</code>时，直接使用{@link JobSchedulerManager}中的触发器对该任务进行
 * 调度，当当<code>isTopPriority()</code>返回<code>true</code>时，则即使{@link JobSchedulerManager}中有和任务相匹配的
 * 触发器，也通过<code>getTriggers()</code>的方法返回的触发器调度任务。 这一般用在测试时期，在生产环境下应该使用
 * {@link JobSchedulerManager}中指定的匹配触发器。
 * 　
 */
public abstract class AbstractTriggerBeanJob extends AbstractBeanJob implements BindTrigger{

    /**
     * 　 如果管理器有有匹配任务的触发器，则优先采用任务管理器中定义的触发器。子类可以通过覆盖该方法，
     * 返回<code>true</code>以改变这个优先策略。
     *
     * @return  
     */
    public boolean isTopPriority() {
        return false;
    }

    /**
     * 将单个Trigger以一个List的形式返回
     * @param trigger
     * @return
     */
    protected List<Trigger> asTriggerList(Trigger trigger){
        List<Trigger> triggers = new ArrayList<Trigger>();
        triggers.add(trigger);
        return triggers;
    }
}
