package com.kcp.platform.bpm.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.bpm.dao.IBpmFormFieldMapper;
import com.kcp.platform.bpm.dao.IBpmFormTableMapper;
import com.kcp.platform.bpm.entity.BpmFormField;
import com.kcp.platform.bpm.entity.BpmFormTable;
import com.kcp.platform.core.service.BaseService;

@Service("bpmFormTableService")
public class BpmFormTableService extends BaseService{
	@Autowired
	private IBpmFormTableMapper bpmFormTableMapper;
	
	@Autowired
	private IBpmFormFieldMapper bpmFormFieldMapper;
	/**
	 * 查询列表(不支持分页)
	 * @param queryMap
	 */
	public List<BpmFormTable> findBpmFormTable(Map<String,Object> queryMap){
		return bpmFormTableMapper.findBpmFormTable(queryMap);
	}
	
	/**
	 * 查询列表(支持分页)
	 * @param queryMap
	 */
	public List<BpmFormTable> findBpmFormTablePaging(Map<String,Object> queryMap){
		return bpmFormTableMapper.findBpmFormTablePaging(queryMap);
	}
	
	/**
	 * 根据ID查询
	 */
	public BpmFormTable getBpmFormTableById(String id){
		return bpmFormTableMapper.getBpmFormTableById(id);
	}
	/**
	 * 新增
	 */
	public void addBpmFormTable(BpmFormTable bpmFormTable, List<BpmFormField> fields){
		bpmFormTable.setTableId(CodeGenerator.getUUID32());
		bpmFormTable.setIsDefault(CommonConst.YES);
		bpmFormTable.setIsPublished(CommonConst.NO);
		bpmFormTable.setIsExternal(CommonConst.NO);
		bpmFormTable.setVersionNo(1);
		bpmFormTableMapper.addBpmFormTable(bpmFormTable);
		
		if (StringUtils.isEmpty(bpmFormTable.getTableDesc())) {
			bpmFormTable.setTableDesc(bpmFormTable.getTableName());
		}
		int i = 1;
		for(BpmFormField field:fields){
			field.setTableId(bpmFormTable.getTableId());
			field.setFieldId(CodeGenerator.getUUID32());
			field.setSn(i);
			bpmFormFieldMapper.addBpmFormField(field);
			i++;
		}
	}
	
	/**
	 * 修改
	 */
	public void updateBpmFormTable(BpmFormTable bpmFormTable, List<BpmFormField> fields){
		BpmFormTable table = getBpmFormTableById(bpmFormTable.getTableId());
		table.setTableDesc(bpmFormTable.getTableDesc());
		table.setTableName(bpmFormTable.getTableName());
		table.setIsMain(bpmFormTable.getIsMain());
		table.setMainTableId(bpmFormTable.getMainTableId());
		table.setPkField(bpmFormTable.getPkField());
		bpmFormTableMapper.updateBpmFormTable(table);
		bpmFormFieldMapper.deleteBpmFormFieldByTableId(table.getTableId());
		int i = 1;
		for(BpmFormField field:fields){
			field.setTableId(bpmFormTable.getTableId());
			field.setFieldId(CodeGenerator.getUUID32());
			field.setSn(i);
			bpmFormFieldMapper.addBpmFormField(field);
			i++;
		}
	}
	
	/**
	 * 发布
	 * @param bpmFormTable
	 */
	public void publishBpmFormTable(BpmFormTable bpmFormTable){
		bpmFormTable.setIsPublished(CommonConst.YES);
		bpmFormTable.setPublishedBy(SecurityContext.getCurrentUser().getZjid());
		bpmFormTableMapper.publishBpmFormTable(bpmFormTable);
		if(CommonConst.YES.equals(bpmFormTable.getIsMain())){
			bpmFormTableMapper.publishSubBpmFormTable(bpmFormTable);
		}
	}
	
	/**
	 *删除
	 */
	public void deleteBpmFormTable(String id){
		bpmFormTableMapper.deleteBpmFormTable(id);
		bpmFormFieldMapper.deleteBpmFormFieldByTableId(id);
	}
	
	/**
	 * 根据条件统计记录数
	 */
	public int countBpmFormTable(BpmFormTable bpmFormTable){
		return bpmFormTableMapper.countBpmFormTable(bpmFormTable);
	}
	

	public List<Map<String,Object>> getAllTables(){
		return bpmFormTableMapper.getAllTables();
	}
	
	public List<Map<String,Object>> getTableColumns(String tableName){
		return bpmFormTableMapper.getTableColumns(tableName);
	}
}