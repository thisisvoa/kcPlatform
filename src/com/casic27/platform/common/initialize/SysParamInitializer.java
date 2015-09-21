package com.casic27.platform.common.initialize;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casic27.platform.base.event.BaseEvent;
import com.casic27.platform.base.event.BaseEventListener;
import com.casic27.platform.common.para.entity.Parameter;
import com.casic27.platform.common.para.event.ParamChangeEvent;
import com.casic27.platform.common.para.service.ParaService;
import com.casic27.platform.sys.config.SysConfig;
import com.casic27.platform.sys.constants.CommonConst;
import com.casic27.platform.sys.initialize.AbstractInitializer;

@Component
public class SysParamInitializer extends AbstractInitializer implements BaseEventListener {
	@Autowired
	private ParaService paraService;
	
	@Override
	public String getDesc() {
		return "系统参数";
	}

	@Override
	public void init() {
		initOrgManagerShJ(SysConfig.CSDM_ORG_MANAGER_SHENGJI);
		initOrgManagerShJ(SysConfig.CSDM_ORG_MANAGER_SHIJI);
		initOrgManagerShJ(SysConfig.CSDM_ORG_MANAGER_XIANJI);
	}
	
	
	/**
	 * 事件处理的方法
	 * @param event
	 */
	public void onEvent(BaseEvent event){
		ParamChangeEvent changeEvent = (ParamChangeEvent)event;
		Parameter parameter = changeEvent.getParam();
		if(parameter == null){
			return;
		}
		if(ParamChangeEvent.CHANGE_TYPE_DEL.equals(changeEvent.getChangeType())){
			if(SysConfig.CSDM_ORG_MANAGER_SHENGJI.equals(parameter.getCsdm())){
				SysConfig.ORG_MANAGER_SHENGJI = "";
			}else if(SysConfig.CSDM_ORG_MANAGER_SHENGJI.equals(parameter.getCsdm())){
				SysConfig.ORG_MANAGER_SHIJI = "";
			}else if(SysConfig.CSDM_ORG_MANAGER_SHENGJI.equals(parameter.getCsdm())){
				SysConfig.ORG_MANAGER_XIANJI = "";
			}
		}else{
			if(parameter.getCsdm().equals(SysConfig.CSDM_ORG_MANAGER_SHENGJI)){
				initOrgManagerShJ(SysConfig.CSDM_ORG_MANAGER_SHENGJI);
			}
			if(parameter.getCsdm().equals(SysConfig.CSDM_ORG_MANAGER_SHIJI)){
				initOrgManagerShJ(SysConfig.CSDM_ORG_MANAGER_SHIJI);
			}
			if(parameter.getCsdm().equals(SysConfig.CSDM_ORG_MANAGER_XIANJI)){
				initOrgManagerShJ(SysConfig.CSDM_ORG_MANAGER_XIANJI);
			}
		}
	}
	
	/**
	 * 所要处理的事件类型
	 * @return
	 */
	public List<Class<? extends BaseEvent>> getEventClasses(){
		List<Class<? extends BaseEvent>> eventCls = new ArrayList<Class<? extends BaseEvent>>();
		eventCls.add(ParamChangeEvent.class);
		return eventCls;
	};
	
	private void initOrgManagerShJ(String type){
		Parameter param = paraService.getParaByCsdm(type);
		if(param!=null && param.getSybz().equals(CommonConst.YES) && param.getJlyxzt().equals(CommonConst.YES)){
			if(SysConfig.CSDM_ORG_MANAGER_SHENGJI.equals(type)){
				SysConfig.ORG_MANAGER_SHENGJI = param.getCsz();
			}else if(SysConfig.CSDM_ORG_MANAGER_SHIJI.equals(type)){
				SysConfig.ORG_MANAGER_SHIJI = param.getCsz();
			}else if(SysConfig.CSDM_ORG_MANAGER_XIANJI.equals(type)){
				SysConfig.ORG_MANAGER_XIANJI = param.getCsz();
			}
		}else{
			if(SysConfig.CSDM_ORG_MANAGER_SHENGJI.equals(type)){
				SysConfig.ORG_MANAGER_SHENGJI = "";
			}else if(SysConfig.CSDM_ORG_MANAGER_SHENGJI.equals(type)){
				SysConfig.ORG_MANAGER_SHIJI = "";
			}else if(SysConfig.CSDM_ORG_MANAGER_SHENGJI.equals(type)){
				SysConfig.ORG_MANAGER_XIANJI = "";
			}
		}
	}
}
