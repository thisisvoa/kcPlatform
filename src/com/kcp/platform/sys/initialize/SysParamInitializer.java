package com.kcp.platform.sys.initialize;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kcp.platform.base.event.BaseEvent;
import com.kcp.platform.base.event.BaseEventListener;
import com.kcp.platform.common.para.entity.SysParameter;
import com.kcp.platform.common.para.event.ParamChangeEvent;
import com.kcp.platform.common.para.service.ParaService;
import com.kcp.platform.sys.config.SysConfig;
import com.kcp.platform.sys.constants.CommonConst;

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
		SysParameter parameter = changeEvent.getParam();
		if(parameter == null){
			return;
		}
		if(ParamChangeEvent.CHANGE_TYPE_DEL.equals(changeEvent.getChangeType())){
			if(SysConfig.CSDM_ORG_MANAGER_SHENGJI.equals(parameter.getParmCode())){
				SysConfig.ORG_MANAGER_SHENGJI = "";
			}else if(SysConfig.CSDM_ORG_MANAGER_SHENGJI.equals(parameter.getParmCode())){
				SysConfig.ORG_MANAGER_SHIJI = "";
			}else if(SysConfig.CSDM_ORG_MANAGER_SHENGJI.equals(parameter.getParmCode())){
				SysConfig.ORG_MANAGER_XIANJI = "";
			}
		}else{
			if(parameter.getParmCode().equals(SysConfig.CSDM_ORG_MANAGER_SHENGJI)){
				initOrgManagerShJ(SysConfig.CSDM_ORG_MANAGER_SHENGJI);
			}
			if(parameter.getParmCode().equals(SysConfig.CSDM_ORG_MANAGER_SHIJI)){
				initOrgManagerShJ(SysConfig.CSDM_ORG_MANAGER_SHIJI);
			}
			if(parameter.getParmCode().equals(SysConfig.CSDM_ORG_MANAGER_XIANJI)){
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
		SysParameter param = paraService.getParaByCsdm(type);
		if(param!=null && param.getIsUsed().equals(CommonConst.YES) && param.getStatus().equals(CommonConst.YES)){
			if(SysConfig.CSDM_ORG_MANAGER_SHENGJI.equals(type)){
				SysConfig.ORG_MANAGER_SHENGJI = param.getParmValue();
			}else if(SysConfig.CSDM_ORG_MANAGER_SHIJI.equals(type)){
				SysConfig.ORG_MANAGER_SHIJI = param.getParmValue();
			}else if(SysConfig.CSDM_ORG_MANAGER_XIANJI.equals(type)){
				SysConfig.ORG_MANAGER_XIANJI = param.getParmValue();
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
