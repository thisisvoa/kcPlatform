package com.casic27.platform.common.log.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casic27.platform.common.log.dao.IArchiveMapper;
import com.casic27.platform.common.log.entity.Gdpz;
import com.casic27.platform.common.log.entity.Gdqd;
import com.casic27.platform.core.service.BaseService;
import com.casic27.platform.util.DateUtils;

@Service("archiveService")
public class ArchiveService extends BaseService {
	private static long DATE_MILLISECOND =  24*60*60*1000;
	@Autowired
	private IArchiveMapper archiveMapper;
	
	@Autowired
	private GdqdService gdqdService;
	
	@Autowired
	private GdpzService gdpzService;
	/**
	 * 归档数据库
	 * @param tableName
	 * @param startTime
	 * @param endTime
	 */
	public void archive(Gdpz gdpz, Date startTime, Date endTime){
		String tableName = gdpz.getGdbmc();
		String endTimeStr = "";
		String gdzqDw = gdpz.getGdzqDw();
		if("1".equals(gdzqDw)){
			endTimeStr = DateUtils.parseDate2String(new Date(endTime.getTime()-DATE_MILLISECOND), "yyMMdd");
		}else if("2".equals(gdzqDw)){
			endTimeStr = DateUtils.parseDate2String(new Date(endTime.getTime()-DATE_MILLISECOND), "yyyyMM");
		}else{
			endTimeStr = DateUtils.parseDate2String(new Date(endTime.getTime()-DATE_MILLISECOND), "yyyy");
		}
		
		String newTableName = tableName+"_"+endTimeStr;
		if(newTableName.length()>30){
			newTableName = newTableName.substring(newTableName.length()-30-1);
		}
		String sjclm = gdpz.getSjclm();
		String sjclSjlx = gdpz.getSjclSjlx();
		int count = 0;
		if("1".equals(sjclSjlx)){
			String stStr = "to_date('"+DateUtils.parseDate2String(startTime, "yyyy-MM-dd HH:mm:ss")+"','yyyy-mm-dd hh24:mi:ss')";
			String etStr = "to_date('"+DateUtils.parseDate2String(endTime, "yyyy-MM-dd HH:mm:ss")+"','yyyy-mm-dd hh24:mi:ss')";
			archiveMapper.copyOldData(tableName, newTableName, sjclm, stStr, etStr);
			count = archiveMapper.removeOldData(tableName, sjclm, startTime, endTime);
		}else{
			String sjclGs = gdpz.getSjclGs();
			String stStr = "'"+DateUtils.parseDate2String(startTime, sjclGs)+"'";
			String etStr = "'"+DateUtils.parseDate2String(endTime, sjclGs)+"'";
			archiveMapper.copyOldData(tableName, newTableName, sjclm, stStr, etStr);
			count = archiveMapper.removeOldData(tableName, sjclm, stStr, etStr);
		}
		gdpz = gdpzService.getGdpzById(gdpz.getZjid());
		gdpz.setZhzxsj(endTime);
		gdpzService.updateGdpz(gdpz);
		
		Gdqd gdqd = new Gdqd();
		gdqd.setGdpzZjid(gdpz.getZjid());
		gdqd.setGdbmc(gdpz.getGdbmc());
		gdqd.setGdhbmc(newTableName);
		gdqd.setGdsjKssj(startTime);
		gdqd.setGdsjJssj(endTime);
		gdqd.setGdjls(count);
		gdqd.setJlcjsj(new Date());
		gdqdService.addGdqd(gdqd);
	}
	
	public Date getMinDate(String tableName, String sjclm){
		return archiveMapper.getMinDate(tableName, sjclm);
	}
	
	public String getMinDateStr(String tableName, String sjclm){
		return archiveMapper.getMinDateStr(tableName, sjclm);
	}
}
