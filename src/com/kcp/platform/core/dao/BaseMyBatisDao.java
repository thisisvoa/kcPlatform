package com.kcp.platform.core.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

/**
 *
 *类描述：
 * 
 *@Version：1.0
 */
public class BaseMyBatisDao {
	/**
	 * mybatis sqlSession对象
	 */
	private SqlSession sqlSession;
	
	public static final String IBATIS_SPLIT_SIGN = ".";
	/**
	 * insert object into table by IBATIS
	 * @param namespace	IBTATIS name space
	 * @param statementId	IBTATIS statement id
	 * @param object	parameter
	 * @return
	 */
	public Object insert(String namespace,String statementId,Object object){
		return this.sqlSession.insert(namespace+IBATIS_SPLIT_SIGN+statementId,object);
	}
	/**
	 * delete object from table by IBATIS
	 * @param namespace	IBATIS name space
	 * @param statementId	IBATIS statement id
	 * @param object	parameter
	 * @return
	 */
	public int delete(String namespace,String statementId,Object object){
		return this.sqlSession.delete(namespace+IBATIS_SPLIT_SIGN+statementId,object);
	}
	
	/**
	 * update object for table by IBATIS
	 * @param namespace	IBATIS name space
	 * @param statementId	IBATIS statement id
	 * @param object	parameter
	 * @return
	 */
	public int update(String namespace,String statementId,Object object){
		return this.sqlSession.update(namespace+IBATIS_SPLIT_SIGN+statementId,object);
	}
	/**
	 * query object from table by IBATIS
	 * @param namespace	IBATIS name space
	 * @param statementId	IBATIS statement id
	 * @param object  parameter
	 * @return  query result (object)
	 */
	public Object queryForObject(String namespace,String statementId,Object object){
		return this.sqlSession.selectOne(namespace+IBATIS_SPLIT_SIGN+statementId,object);
	}
	
	/**
	 * query list from table by IBATIS
	 * @param namespace	IBATIS name space
	 * @param statementId	IBATIS statement id
	 * @param object parameter
	 * @return  query result (list)
	 */
	public List<?> queryForList(String namespace,String statementId,Object object){
		return this.sqlSession.selectList(namespace+IBATIS_SPLIT_SIGN+statementId, object);
	}
	
	public SqlSession getSqlSession() {
		return sqlSession;
	}
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
}
