/**
 * @(#)com.kcp.platform.common.code.dao.ICdodeMapper.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：2012-5-15
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.kcp.platform.common.code.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcp.platform.common.code.entity.Code;
import com.kcp.platform.common.log.annotation.OperateLog;
import com.kcp.platform.core.mybatis.annotation.Pageable;

/**
 *
 *类描述：
 * 
 *@Author： 宗斌(zongbin@kcp.com)
 *@Version：1.0
 */
@Repository
public interface ICodeMapper {
	/**
	 * 根据代码ID查询获取代码
	 * @param codeId
	 * @return
	 */
	
	Code getCodeById(@Param("codeId")String codeId);
	
	/**
	 * 查询代码列表
	 * @param queryMap
	 * @return
	 */
	List<Code> findCodeList(Map<String,Object> queryMap);
	
	/**
	 * 根据代码实体查询代码实体列表信息(精确查询)
	 * @param Code code
	 * @return List<Code>
	 */
	@Pageable
	@OperateLog(isRecord=false)
	List<Code> queryCodeListByCode(Code code);
	
	/**
	 * 根据参数集map查询代码实体列表信息(代码名称、代码简称、代码项编号、代码项名称、代码排序号、备注模糊查询，其它为精确查询)
	 * @param Map<String, Object> paramMap
	 * @return List<Code>
	 */
	@Pageable
	@OperateLog(isRecord=false)
	List<Code> queryCodeListByParamMap(Map<String, Object> paramMap);
	
	/**
	 * 插入代码信息
	 * @param code
	 * @return
	 */
	@OperateLog(isRecord=false)
	int insertCode(Code code);
	
	/**
	 * 修改代码信息
	 * @param code
	 * @return
	 */
	@OperateLog(isRecord=false)
	int updateCode(Code code);
	
	/**
	 * 根据代码简称获取代码项目列表
	 */
	List<Code> getCodeItemList(@Param("dmjc")String dmjc);
	/**
	 * 根据代码简称获取当前代码的最大排序号
	 * @param dmjc
	 * @return
	 */
	String getMaxPxh(Code code);
	
	/**
	 * 判断代码项是否存在
	 */
	int staticCode(Code code);
}
