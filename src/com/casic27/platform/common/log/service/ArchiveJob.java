package com.casic27.platform.common.log.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casic27.platform.base.job.AbstractBeanJob;
import com.casic27.platform.common.log.entity.Gdpz;
import com.casic27.platform.util.DateUtils;

/**
 * 改调度任务在每天00:00:00运行
 * @author Administrator
 *
 */
@Component("archiveJob")
public class ArchiveJob extends AbstractBeanJob {
	protected static transient final Log log = LogFactory.getLog(ArchiveJob.class);
	private static long DATE_MILLISECOND =  24*60*60*1000;
	@Autowired
	private GdpzService gdpzService;
	
	@Autowired
	private ArchiveService archiveService;
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) {
		List<Gdpz> gdpzList = gdpzService.getEnableGdpz();
		List<Gdpz> archiveList = new ArrayList<Gdpz>();//今天需进行归档的配置
		for(Gdpz gdpz:gdpzList){
			boolean needArchive = needArchive(gdpz);
			if(needArchive){
				archiveList.add(gdpz);
			}
		}
		if(archiveList.size()>0){
			long now = System.currentTimeMillis();
			ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
			for(Gdpz gdpz: archiveList){
				String zxsj = gdpz.getZxsj();
				Calendar seheduleCal = getCurrentCal();
				seheduleCal.set(Calendar.HOUR, Integer.parseInt(zxsj.split(":")[0]));
				seheduleCal.set(Calendar.MINUTE, Integer.parseInt(zxsj.split(":")[1]));
				long delay = seheduleCal.getTimeInMillis()-now;
				service.schedule(new ArchiveTable(gdpz), delay, TimeUnit.MILLISECONDS);
			}
		}
	}
	
	/**
	 * 今天需要进行归档的归档配置
	 * @param gdpz
	 * @return
	 */
	public boolean needArchive(Gdpz gdpz){
		String sjclSjlx = gdpz.getSjclSjlx();
		Date zhzxsj = gdpz.getZhzxsj();
		if(null==zhzxsj){//如果最后执行时间为空，则取待归档表中的数据的最小时间戳
			if("1".equals(sjclSjlx)){
				zhzxsj = archiveService.getMinDate(gdpz.getGdbmc(), gdpz.getSjclm());
			}else if("2".equals(sjclSjlx)){
				String minDateStr = archiveService.getMinDateStr(gdpz.getGdbmc(), gdpz.getSjclm());
				zhzxsj = DateUtils.parseStringformatDate((String)minDateStr, gdpz.getSjclGs());
			}
			if(zhzxsj == null) return false;
			gdpz.setZhzxsj(zhzxsj);
		}
		Calendar currentDate = getCurrentCal();
		Calendar minCal = parseDateToCal(zhzxsj);
		String gdzqDw = gdpz.getGdzqDw();
		if("1".equals(gdzqDw)){
			if(((currentDate.getTimeInMillis()-minCal.getTimeInMillis())/DATE_MILLISECOND)>=(gdpz.getGdzq()+gdpz.getYcsj())){
				return true;
			}
		}else if("2".equals(gdzqDw)){
			Calendar currentMonth = getCurrentMonth();
			if(minCal.getTimeInMillis()<currentMonth.getTimeInMillis()){
				if(getMonthDiff(minCal, currentMonth)>=gdpz.getGdzq()){
					if(((currentDate.getTimeInMillis()-currentMonth.getTimeInMillis())/DATE_MILLISECOND)>=(gdpz.getYcsj())){
						return true;
					}
				}
			}
		}else if("3".equals(gdzqDw)){
			Calendar currentYear = getCurrentYear();
			if(minCal.getTimeInMillis()<currentYear.getTimeInMillis()){
				if(getYearDiff(minCal, currentYear)>=gdpz.getGdzq()){
					if(((currentDate.getTimeInMillis()-currentYear.getTimeInMillis())/DATE_MILLISECOND)>=(gdpz.getYcsj())){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private Calendar getCurrentCal(){
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE), 0 , 0, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		return currentDate;
	}
	
	private Calendar getCurrentMonth(){
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), 1, 0 , 0, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		return currentDate;
	}
	
	private Calendar getCurrentYear(){
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(currentDate.get(Calendar.YEAR), 0, 1, 0 , 0, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		return currentDate;
	}
	private Calendar parseDateToCal(Date date){
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear()+1900, date.getMonth(), date.getDate(), 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	
	private int getMonthDiff(Calendar startCal, Calendar endCal){
		int startY = startCal.get(Calendar.YEAR);
		int startM = startCal.get(Calendar.MONTH);
		int endY = endCal.get(Calendar.YEAR);
		int endM = endCal.get(Calendar.MONTH);
        int monthday = ((endY - startY) * 12 + (endM - startM));
        return monthday;
    }
	
	private int getYearDiff(Calendar startCal, Calendar endCal){
		int startY = startCal.get(Calendar.YEAR);
		int endY = endCal.get(Calendar.YEAR);
		return endY-startY;
	}
	
	private Calendar getCalMonth(Calendar cal){
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	private Calendar getCalYear(Calendar cal){
		cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	class ArchiveTable implements Runnable{
		private Gdpz gdpz;
		public ArchiveTable(Gdpz gdpz){
			this.gdpz = gdpz;
		}
		
		public void run() {
			try {
				String gdzqDw = gdpz.getGdzqDw();
				if("1".equals(gdzqDw)){
					Calendar currentCal = getCurrentCal();
					Calendar minCal = parseDateToCal(gdpz.getZhzxsj());
					while((minCal.getTimeInMillis()+gdpz.getGdzq()*DATE_MILLISECOND+gdpz.getYcsj()*DATE_MILLISECOND)<=currentCal.getTimeInMillis()){
						Date endDate = new Date(minCal.getTimeInMillis()+gdpz.getGdzq()*DATE_MILLISECOND);
						archiveService.archive(gdpz, minCal.getTime(), endDate);
						minCal = parseDateToCal(endDate);
					}
				}else if("2".equals(gdzqDw)){
					Calendar currentMonth = getCurrentMonth();
					Calendar minCal = getCalMonth(parseDateToCal(gdpz.getZhzxsj()));
					while(getMonthDiff(minCal, currentMonth)>=gdpz.getGdzq()){
						Date startTime = minCal.getTime();
						minCal.add(Calendar.MONTH, gdpz.getGdzq());
						Date endDate = minCal.getTime();
						archiveService.archive(gdpz, startTime, endDate);
					}
				}else if("3".equals(gdzqDw)){
					Calendar currentYear = getCurrentYear();
					Calendar minCal = getCalYear(parseDateToCal(gdpz.getZhzxsj()));
					while(getYearDiff(minCal, currentYear)>=gdpz.getGdzq()){
						Date startTime = minCal.getTime();
						minCal.add(Calendar.YEAR, gdpz.getGdzq());
						Date endDate = minCal.getTime();
						archiveService.archive(gdpz, startTime, endDate);
					}
				}
				log.info("表："+gdpz.getGdbmc()+"，归档成功("+DateUtils.parseDate2String(new Date(), "yyyy-MM-dd hh:mm:ss")+")");
			} catch (Exception e) {
				log.error("表："+gdpz.getGdbmc()+"，归档失败("+DateUtils.parseDate2String(new Date(), "yyyy-MM-dd hh:mm:ss")+")",e);
				e.printStackTrace();
			}
	    }
	}
}
