
package com.casic27.platform.ui.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  类描述：
 * </pre>
 * <pre>
 * </pre>
 */
public class GridData implements Serializable {
	/**
	 * 总页数
	 */
	private int total;
	/**
	 * 当前页
	 */
	private int page;
	/**
	 * 总记录数
	 */
	private int records;
	/**
	 * 当前页数据
	 */
	private List rows;
	
	/**
	 * 自定义数据
	 */
	private Map customData;
	

	public GridData(){
		
	}
	
	public GridData(List rows){
		this.rows = rows;
	}
	
	public GridData(List rows,int page, int total, int records){
		this.rows = rows;
		this.page = page;
		this.total = total;
		this.records = records;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRecords() {
		return records;
	}
	public void setRecords(int records) {
		this.records = records;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
	public Map getCustomData() {
		return customData;
	}

	public void setCustomData(Map customData) {
		this.customData = customData;
	}
}
