package com.kcp.platform.common.code.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcp.platform.common.code.dao.ICodeMapper;
import com.kcp.platform.common.code.entity.Code;
import com.kcp.platform.common.log.annotation.Log;
import com.kcp.platform.common.log.annotation.OperateLogType;
import com.kcp.platform.core.service.BaseService;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.util.DateUtils;

/**
 *
 *类描述：
 * 
 *@Version：1.0
 */
@Service("codeService")
public class CodeService extends BaseService{

	@Autowired
	ICodeMapper codeMapper;
	
	/**
	 * 根据代码id获取代码信息
	 * @param codeId
	 * @return
	 */
	public Code getCodeById(String zjid){
		return codeMapper.getCodeById(zjid);
	}
	
	public List<Code> findCodeList(Map<String,Object> queryMap){
		return codeMapper.findCodeList(queryMap);
	}
	/**
	 * 根据代码实体查询代码实体列表信息
	 * @param Code code
	 * @return List<Code>
	 */
	@Log(type=OperateLogType.QUERY, moduleName="代码信息管理", operateDesc="[查询] 查询代码/代码项信息", useSpel=false)
	public List<Code> queryCodeListByCode(Code code){
		return codeMapper.queryCodeListByCode(code);
	}
	
	/**
	 * 根据参数集map查询代码实体列表信息
	 * @param Map<String, Object> paramMap
	 * @return List<Code>
	 */
	@Log(type=OperateLogType.QUERY, moduleName="代码信息管理", operateDesc="[查询] 查询代码信息", useSpel=false)
	public List<Code> queryCodeListByParamMap(Map<String, Object> paramMap){
		return codeMapper.queryCodeListByParamMap(paramMap);
	}
	
	/**
	 * 插入代码信息
	 * @param code
	 * @return
	 */
	@Log(type=OperateLogType.INSERT, moduleName="代码信息管理", operateDesc="'[新增代码/代码项] 新增[代码名称：'+#code.dmmc+'] [代码简称：'+#code.dmjc+'] [代码项名称:'+#code.dmx_mc+'] [代码项编号:'+#code.dmx_bh+']的代码信息'")
	public Code insertCode(Code code){
		codeMapper.insertCode(code);
		return code;
	}
	
	/**
	 * 修改代码信息
	 * @param code
	 * @return
	 */
	@Log(type=OperateLogType.UPDATE, moduleName="代码信息管理", operateDesc="'[修改代码/代码项] 修改[代码名称：'+#code.dmmc+'] [代码简称：'+#code.dmjc+'] [代码项名称：'+#code.dmx_mc+'] [代码项编号：'+#code.dmx_bh+']的代码信息'")
	public Code updateCode(Code code){
		codeMapper.updateCode(code);
		return code;
	}
	
	@Log(type=OperateLogType.UPDATE, moduleName="代码信息管理", operateDesc="'[启用(禁用 )代码/代码项] 修改[代码名称：'+#code.dmmc+'] [代码简称：'+#code.dmjc+'] [代码项名称：'+#code.dmx_mc+'] [代码项编号：'+#code.dmx_bh+'] 的使用标志状态为['+#sybz+']'")
	public void updateSybz(Code code, String sybz){
		code.setSybz(sybz);
		code.setGxyh(SecurityContext.getCurrentUser().getZjid());
		code.setJlxgsj(DateUtils.getCurrentDateTime14());
		code.setJlxzsj(DateUtils.getFormatString(code.getJlxzsj(),"yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
		code.setJlscsj(DateUtils.getFormatString(code.getJlscsj(),"yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
		updateCode(code);
	}
	
	@Log(type=OperateLogType.DELETE, moduleName="代码信息管理", operateDesc="'[删除代码/代码项] 删除[代码名称：'+#code.dmmc+'] [代码简称:'+#code.dmjc+'] [代码项名称:'+#code.dmx_mc+'] [代码项编号:'+#code.dmx_bh+']的代码信息'")
	public void logicDel(Code code){
		code.setJlyxzt(CommonConst.DISABLE_FLAG);
		code.setGxyh(SecurityContext.getCurrentUser().getZjid());
		code.setJlxgsj(DateUtils.getFormatString(code.getJlxgsj(),"yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
		code.setJlxzsj(DateUtils.getFormatString(code.getJlxzsj(),"yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
		code.setJlscsj(DateUtils.getCurrentDateTime14());
		updateCode(code);
	}
	/**
	 * 根据代码简称获取代码项目列表
	 */
	public List<Code> getCodeItemList(String dmjc){
		return codeMapper.getCodeItemList(dmjc);
	}
	
	public String getCodeItemListAsString(String dmjc){
		List<Code> codeList = codeMapper.getCodeItemList(dmjc);
		StringBuffer sb = new StringBuffer();
		if(codeList!=null){
			String spliter = "";
			for(Code code:codeList){
				sb.append(spliter);
				sb.append(code.getDmx_bh());
				sb.append(":");
				sb.append(code.getDmx_mc());
				spliter = ";";
			}
		}
		return sb.toString();
	}
	
	/**
	 * 根据代码简称获取最大排序号+1
	 */
	public String getCodeMaxPxh(Code code){
		return codeMapper.getMaxPxh(code);
	}
	
	/**
	 * 判断代码项是否存在
	 */
	public int staticCode(Code code){
		return codeMapper.staticCode(code);
	}
}	
