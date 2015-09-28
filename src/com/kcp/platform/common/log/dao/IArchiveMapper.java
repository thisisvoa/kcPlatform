package com.kcp.platform.common.log.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IArchiveMapper {
	/**
	 * 复制数据到新表中
	 * @param oldTableName
	 * @param newTableName
	 * @param startTime
	 * @param endTime
	 */
	void copyOldData(@Param("oldTableName")String oldTableName, @Param("newTableName")String newTableName, @Param("sjclm")String sjclm, @Param("startTime")String startTime, @Param("endTime")String endTime);
	
	/**
	 * 删除旧的数据
	 * @param oldTableName
	 * @param startTime
	 * @param endTime
	 */
	int removeOldData(@Param("tableName")String tableName, @Param("sjclm")String sjclm, @Param("startTime")Object startTime, @Param("endTime")Object endTime);
	
	/**
	 * 获取数据的最小时间
	 * @param tableName
	 * @param sjclm
	 * @return
	 */
	Date getMinDate(@Param("tableName")String tableName, @Param("sjclm")String sjclm);
	
	String getMinDateStr(@Param("tableName")String tableName, @Param("sjclm")String sjclm);
}
