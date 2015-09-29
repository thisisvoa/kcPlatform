package com.kcp.platform.common.para.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.base.event.EventDispatcher;
import com.kcp.platform.common.log.annotation.Log;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.common.para.dao.IParaMapper;
import com.kcp.platform.common.para.entity.SysParameter;
import com.kcp.platform.common.para.event.ParamChangeEvent;
import com.kcp.platform.core.service.BaseService;


@Service("paraService")
public class ParaService extends BaseService{
	@Autowired
	private IParaMapper iParaMapper;
	public IParaMapper getParaDao() {
		return iParaMapper;
	}
	public void setParaDao(IParaMapper paraDao) {
		this.iParaMapper = paraDao;
	}
	@Log(type=OperateLogType.QUERY, moduleName="参数信息管理", operateDesc="查询参数信息", useSpel=false)
	public List<SysParameter> queryParaList(SysParameter paraMap){
		return iParaMapper.queryParaList(paraMap);
	}
	
	public String queryMaxId(){
		return iParaMapper.queryMaxId();
	}
	@Log(type=OperateLogType.INSERT, moduleName="参数信息管理", operateDesc="'[新增] 新增[参数名称：'+#paramMap.csmc+'] [参数代码：'+#paramMap.csdm+'] [参数值：'+#paramMap.csz+']的参数信息'")
	public int insert(SysParameter paramMap){
		int result = iParaMapper.insert(paramMap);
		ParamChangeEvent event = new ParamChangeEvent(ParamChangeEvent.CHANGE_TYPE_ADD, paramMap);
		EventDispatcher.publishEvent(event);
		return result;
	}
	
	@Log(type=OperateLogType.UPDATE, moduleName="参数信息管理", operateDesc="'[修改] 修改[参数名称：'+#paramMap.csmc+'] [参数代码：'+#paramMap.csdm+'] [参数值：'+#paramMap.csz+']的参数信息'")
	public int update(SysParameter paramMap){
		int result = iParaMapper.update(paramMap);
		ParamChangeEvent event = new ParamChangeEvent(ParamChangeEvent.CHANGE_TYPE_UPDATE, paramMap);
		EventDispatcher.publishEvent(event);
		return result;
	}
	
	@Log(type=OperateLogType.UPDATE, moduleName="参数信息管理", operateDesc="'[启用/禁用参数] 修改[参数名称：'+#paramMap.csmc+'] [参数代码：'+#paramMap.csdm+'] [参数值：'+#paramMap.csz+']的使用标志为['+#sybz+']'")
	public void updateSybz(SysParameter paramMap, String sybz){
		paramMap.setIsUsed(sybz);
		iParaMapper.updateSybz(paramMap);
		ParamChangeEvent event = new ParamChangeEvent(ParamChangeEvent.CHANGE_TYPE_SYBZ, paramMap);
		EventDispatcher.publishEvent(event);
	}
	
	@Log(type=OperateLogType.DELETE, moduleName="参数信息管理", operateDesc="'[删除] 删除[参数名称：'+#paramMap.csmc+'] [参数代码：'+#paramMap.csdm+'] [参数值：'+#paramMap.csz+']的参数信息'")
	public void logicDelParam(SysParameter paramMap){
		SysParameter param = getParaById(paramMap.getId());
		iParaMapper.logicDelParam(paramMap);
		ParamChangeEvent event = new ParamChangeEvent(ParamChangeEvent.CHANGE_TYPE_DEL, param);
		EventDispatcher.publishEvent(event);
	}
	
	public int delete(String id){
		SysParameter param = getParaById(id);
		int result = iParaMapper.delete(id);
		ParamChangeEvent event = new ParamChangeEvent(ParamChangeEvent.CHANGE_TYPE_DEL, param);
		EventDispatcher.publishEvent(event);
		return result;
	}
	
	public SysParameter getParaById(String zjId){
		return iParaMapper.getParaById(zjId);
	}
	
	public SysParameter getParaByCsdm(String csdm){
		return iParaMapper.getParaByCsdm(csdm);
	}
	
	public List<Object[]> createObjectListForExport(List<Map<String, Object>> resultList){
		List<Object[]> rebuildObjectList = new ArrayList<Object[]>();
		for(int i = 0; i < resultList.size(); i++){
			Map<String, Object> mapObj = (Map<String, Object>)resultList.get(i);
			Object[] obj = new Object[5];
			obj[0] = i + 1;
			obj[1] = mapObj.get("NAME");
			obj[2] = mapObj.get("LOGIN_NAME");
			obj[3] = mapObj.get("PASSWORD");
			obj[4] = mapObj.get("REGEDIT_TIME");
			rebuildObjectList.add(obj);
		}
		
		return rebuildObjectList;
	}
	
	public boolean isCsdmExist(SysParameter paramMap){
		int count = iParaMapper.statisCount(paramMap);
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
	
	public void addHisOnline(){
		iParaMapper.addHisOnline();
	}
}
