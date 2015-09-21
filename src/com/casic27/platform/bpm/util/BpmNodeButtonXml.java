package com.casic27.platform.bpm.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bpm")
@XmlAccessorType(XmlAccessType.FIELD)
public class BpmNodeButtonXml {

	@XmlElementWrapper(name = "buttons")
	@XmlElements({ @javax.xml.bind.annotation.XmlElement(name = "button", type = BpmButton.class) })
	private List<BpmButton> buttons;
	private List<BpmButton> startBtnList = new ArrayList<BpmButton>();
	
	private List<BpmButton> firstNodeBtnList = new ArrayList<BpmButton>();
	
    private List<BpmButton> signBtnList = new ArrayList<BpmButton>();
    
    private List<BpmButton> commonBtnList = new ArrayList<BpmButton>();
    
	public List<BpmButton> getButtons() {
		return this.buttons;
	}

	public void setButtons(List<BpmButton> buttons) {
		this.buttons = buttons;
	}
	
	public void init() {
		for (BpmButton button : buttons) {
			if (button.getStart().intValue() == 1) {
				startBtnList.add(button);
			}
			if (button.getFirst().intValue() == 1) {
				firstNodeBtnList.add(button);
			}
			if (button.getCommon().intValue() == 1) {
				commonBtnList.add(button);
				signBtnList.add(button);
			} else if (button.getSign().intValue() == 1) {
				signBtnList.add(button);
			}
		}
	}
	
	public List<BpmButton> getStartBtnList() {
		return startBtnList;
	}

	public void setStartBtnList(List<BpmButton> startBtnList) {
		this.startBtnList = startBtnList;
	}

	public List<BpmButton> getFirstNodeBtnList() {
		return firstNodeBtnList;
	}

	public void setFirstNodeBtnList(List<BpmButton> firstNodeBtnList) {
		this.firstNodeBtnList = firstNodeBtnList;
	}

	public List<BpmButton> getSignBtnList() {
		return signBtnList;
	}

	public void setSignBtnList(List<BpmButton> signBtnList) {
		this.signBtnList = signBtnList;
	}

	public List<BpmButton> getCommonBtnList() {
		return commonBtnList;
	}

	public void setCommonBtnList(List<BpmButton> commonBtnList) {
		this.commonBtnList = commonBtnList;
	}
	
	public String toEditOption(){
		StringBuffer sb = new StringBuffer();
		String spliter = "";
		for(BpmButton btn : buttons){
			sb.append(spliter).append(btn.getOperatortype()).append(":").append(btn.getText());
			spliter=";";
		}
		return sb.toString();
	}
	/**
	 * 开始节点操作按钮描述
	 * @return
	 */
	public String startBtnDesc(){
		StringBuffer sb = new StringBuffer();
		String spliter = "";
		for(BpmButton btn : startBtnList){
			sb.append(spliter).append(btn.getText());
			spliter=",";
		}
		return sb.toString();
	}
	/**
	 * 第一个节点操作按钮描述
	 * @return
	 */
	public String firstBtnDesc(){
		StringBuffer sb = new StringBuffer();
		String spliter = "";
		for(BpmButton btn : firstNodeBtnList){
			sb.append(spliter).append(btn.getText());
			spliter=",";
		}
		return sb.toString();
	}
	
	/**
	 * 普通节点操作按钮描述
	 * @return
	 */
	public String commonBtnDesc(){
		StringBuffer sb = new StringBuffer();
		String spliter = "";
		for(BpmButton btn : commonBtnList){
			sb.append(spliter).append(btn.getText());
			spliter=",";
		}
		return sb.toString();
	}
	
	/**
	 * 会签节点操作按钮描述
	 * @return
	 */
	public String signBtnDesc(){
		StringBuffer sb = new StringBuffer();
		String spliter = "";
		for(BpmButton btn : signBtnList){
			sb.append(spliter).append(btn.getText());
			spliter=",";
		}
		return sb.toString();
	}
}
