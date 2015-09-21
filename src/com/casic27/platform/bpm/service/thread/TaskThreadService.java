package com.casic27.platform.bpm.service.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.casic27.platform.bpm.entity.ProcessCmd;

/**
 * 将流程参数放置到ThreadLocal中，方便Listener等获取参数
 * @author Administrator
 *
 */
public class TaskThreadService {
	/**
	 * 分发令牌
	 */
	private static ThreadLocal<String> forkTaskTokenLocal = new ThreadLocal<String>();

	/**
	 * 新的任务
	 */
	private static ThreadLocal<List<Task>> newTasksLocal = new ThreadLocal<List<Task>>();

	/**
	 * 上一节点执行用户
	 */
	private static ThreadLocal<String> preUserLocal = new ThreadLocal<String>();

	/**
	 * ProcessCmd
	 */
	private static ThreadLocal<ProcessCmd> processCmdLocal = new ThreadLocal<ProcessCmd>();

	/**
	 * 调用的外部子流程
	 */
	private static ThreadLocal<List<String>> extSubProcess = new ThreadLocal<List<String>>();

	/**
	 * 传递其它参数
	 */
	private static ThreadLocal<Object> objLocal = new ThreadLocal<Object>();

	/**
	 * 直接跳过第一个节点
	 */
	private static ThreadLocal<String> toFirstNode = new ThreadLocal<String>();

	/**
	 * 其他传递的参数
	 */
	private static ThreadLocal<Map<String, Object>> varsLocal = new ThreadLocal<Map<String, Object>>();
	
	/**
	 * 临时传递的参数
	 */
	private static ThreadLocal<Map<String, Object>> tempLocal = new ThreadLocal<Map<String, Object>>();

	public static void addTask(Task taskEntity) {
		List<Task> taskList = newTasksLocal.get();
		if (taskList == null) {
			taskList = new ArrayList<Task>();
			newTasksLocal.set(taskList);
		}
		taskList.add(taskEntity);
	}

	public static List<Task> getNewTasks() {
		List<Task> list = newTasksLocal.get();
		return list;
	}

	public static String getToken() {
		return (String) forkTaskTokenLocal.get();
	}

	public static void setToken(String token) {
		forkTaskTokenLocal.set(token);
	}

	public static void clearToken() {
		forkTaskTokenLocal.remove();
	}

	public static void clearNewTasks() {
		newTasksLocal.remove();
	}

	public static void cleanTaskUser() {
		preUserLocal.remove();
	}

	public static ProcessCmd getProcessCmd() {
		return (ProcessCmd) processCmdLocal.get();
	}

	public static void setProcessCmd(ProcessCmd processCmd) {
		processCmdLocal.set(processCmd);
	}

	public static void cleanProcessCmd() {
		processCmdLocal.remove();
	}

	public static List<String> getExtSubProcess() {
		return extSubProcess.get();
	}

	public static void setExtSubProcess(List<String> extSubProcessList) {
		extSubProcess.set(extSubProcessList);
	}

	public static void addExtSubProcess(String instanceId) {
		List<String> list = null;
		if (extSubProcess.get() == null) {
			list = new ArrayList<String>();
			list.add(instanceId);
			extSubProcess.set(list);
		} else {
			(extSubProcess.get()).add(instanceId);
		}
	}

	public static void cleanExtSubProcess() {
		extSubProcess.remove();
	}

	public static void setObject(Object obj) {
		objLocal.set(obj);
	}

	public static Object getObject() {
		return objLocal.get();
	}

	public static void setToFirstNode(String obj) {
		toFirstNode.set(obj);
	}

	public static Object getToFirstNode() {
		return toFirstNode.get();
	}

	public static void removeToFirstNode() {
		toFirstNode.remove();
	}

	public static void removeObject() {
		objLocal.remove();
	}

	public static void setVariables(Map<String, Object> vars_) {
		varsLocal.set(vars_);
	}

	public static Map<String, Object> getVariables() {
		return varsLocal.get();
	}

	public static void clearVariables() {
		varsLocal.remove();
	}

	public static void setTempLocal(String actInstId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("forkTaskTokenLocal" + actInstId, forkTaskTokenLocal.get());
		map.put("newTasksLocal" + actInstId, newTasksLocal.get());
		map.put("preUserLocal" + actInstId, preUserLocal.get());
		map.put("processCmdLocal" + actInstId, processCmdLocal.get());
		map.put("extSubProcess" + actInstId, extSubProcess.get());
		map.put("objLocal" + actInstId, objLocal.get());
		map.put("varsLocal" + actInstId, varsLocal.get());

		tempLocal.set(map);
	}

	public static void resetTempLocal(String actInstId) {
		Map<String,Object> map = tempLocal.get();
		forkTaskTokenLocal.set((String) map.get("forkTaskTokenLocal" + actInstId));
		newTasksLocal.set((List<Task>) map.get("newTasksLocal" + actInstId));
		preUserLocal.set((String) map.get("preUserLocal" + actInstId));
		processCmdLocal.set((ProcessCmd) map.get("processCmdLocal" + actInstId));
		extSubProcess.set((List<String>) map.get("extSubProcess" + actInstId));
		objLocal.set(map.get("objLocal" + actInstId));
		varsLocal.set((Map<String,Object>) map.get("varsLocal" + actInstId));
		tempLocal.remove();
	}

	public static void clearAll() {
		forkTaskTokenLocal.remove();
		newTasksLocal.remove();
		preUserLocal.remove();
		processCmdLocal.remove();
		extSubProcess.remove();
		objLocal.remove();
		varsLocal.remove();
	}
}
