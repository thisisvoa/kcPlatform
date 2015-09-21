package com.casic27.platform.bpm.dao;


import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IBpmMapper {
	
	Map<String,Object> getDefXmlByDeployId(@Param("deployId")String deployId);
	
	void wirteDefXml(@Param("deployId")String deployId, @Param("defXml")byte[] defXml);
}
