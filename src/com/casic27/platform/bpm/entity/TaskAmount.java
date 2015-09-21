package com.casic27.platform.bpm.entity;

public class TaskAmount {
	private String typeId;

	private int read = 0;

	private int total = 0;

	private int notRead = 0;

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public int getRead() {
		return this.read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getTotal() {
		return this.total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getNotRead() {
		return this.notRead;
	}

	public void setNotRead(int notRead) {
		this.notRead = notRead;
	}
}
