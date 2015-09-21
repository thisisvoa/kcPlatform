package com.casic27.platform.bpm.constants;

public class BpmConstants {
	
	public static final String NODE_TYPE_STARTEVENT = "1";
	public static final String NODE_TYPE_ENDEVENT = "2";
	public static final String NODE_TYPE_USERTASK = "3";
	public static final String NODE_TYPE_JAVATASK = "4";
	public static final String NODE_TYPE_SCRIPTTASK = "5";
	public static final String NODE_TYPE_MULTITASK = "6";
	public static final String NODE_TYPE_PARALLELGATEWAY = "7";
	public static final String NODE_TYPE_EXCLUSIVEGATEWAY = "8";
	public static final String NODE_TYPE_INCLUSIVEGATEWAY = "9";
	
	/**
	 * 节点人员
	 */
	public static final String CODE_PARTICIPANT_TYPE= "BPM_PARTICIPANT";
	
	/**
	 * 开始脚本
	 */
	public static final String START_SCRIPT = "1";

	/**
	 * 结束脚本
	 */
	public static final String END_SCRIPT = "2";

	public static final String SCRIPTNODE_SCRIPT = "3";
	
	public static final String ASSIGN_SCRIPT = "4";
	
	/**
	 * 驳回
	 */
	public static final Integer TASK_BACK = Integer.valueOf(1);
	
	/**
	 * 驳回到初始节点
	 */
	public static final Integer TASK_BACK_TOSTART = Integer.valueOf(2);
	
}
