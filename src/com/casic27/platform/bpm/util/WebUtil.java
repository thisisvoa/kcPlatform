package com.casic27.platform.bpm.util;

import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

import com.casic27.platform.bpm.entity.TaskExecutor;
import com.casic27.platform.bpm.service.TaskUserService;
import com.casic27.platform.common.user.entity.User;
import com.casic27.platform.common.user.service.UserService;
import com.casic27.platform.sys.context.SpringContextHolder;

public class WebUtil {
	public static String getAssignee(String ctx, String userId, String taskId, boolean isIcon) {
		String noData = "暂无";
		StringBuffer sb = new StringBuffer();
		String imgUser = "<img src='" + ctx + "/ui/css/icon/images/user.gif' style='margin:-5px 3px'>";
		String imgOrg = "<img src='" + ctx + "/ui/css/icon/images/user_group.gif' style='margin:-5px 3px'>";
		String imgRole = "<img src='" + ctx + "/ui/css/icon/images/user_female.gif' style='margin:-5px 3px'>";
		if (StringUtils.isNotEmpty(userId) && !("0".equals(userId))) {
			UserService userService = SpringContextHolder.getBean(UserService.class);
			User user = userService.getUserById(userId);
			if (user == null)
				return noData;
			String userName = user.getYhmc();
			if (isIcon){
				sb.append(imgUser);
			}
			sb.append("<a class='userlink' style='color:blue' userId='"+user.getZjid()+"'>").append(userName).append("</a>&nbsp;");
		} else if (StringUtils.isNotEmpty(taskId)) {
			TaskUserService taskUserService = SpringContextHolder.getBean(TaskUserService.class);
			Set<TaskExecutor> candidateUsers = taskUserService.getCandidateExecutors(taskId);
			if (candidateUsers.size() > 4) {
				sb.append("<img src='" + ctx + "/ui/css/icon/images/detail.gif' style='margin:-5px 3px'>");
				sb.append("<a class='showMore' style='color:blue' taskId='" + taskId + "' >执行人...</a>&nbsp;" +
							"<input type='hidden' name='candidateUsersJson' value='" + JSONArray.fromObject(candidateUsers) + "'>");
			} else {
				for (TaskExecutor te : candidateUsers) {
					if ("org".equals(te.getType())){
						if (isIcon){
							sb.append(imgOrg);
						}
						sb.append("<a class='orglink' style='color:blue' orgId='"+te.getExecuteId()+"'>" + te.getExecutor() + "</a>&nbsp;");
					}else if ("role".equals(te.getType())){
						if (isIcon){
							sb.append(imgRole);
						}
						sb.append("<a class='rolelink' style='color:blue' roleId='"+te.getExecuteId()+"'>" + te.getExecutor() + "</a>&nbsp;");
					}else if ("user".equals(te.getType())) {
						if (isIcon){
							sb.append(imgUser);
						}
						sb.append("<a class='userlink' style='color:blue' userId='"+te.getExecuteId()+"'>" + te.getExecutor() + "</a>&nbsp;");
					}
				}
			}
		}
		return StringUtils.isEmpty(sb.toString()) ? noData : sb.toString();
	}
}
