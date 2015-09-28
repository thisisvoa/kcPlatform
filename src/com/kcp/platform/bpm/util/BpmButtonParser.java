package com.kcp.platform.bpm.util;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.Element;

import com.kcp.platform.util.Dom4jUtil;
import com.kcp.platform.util.FileUtil;

public class BpmButtonParser {
	
	private static BpmNodeButtonXml bpmNodeButtonXml;
	
	public static synchronized BpmNodeButtonXml parse()throws Exception{
		if(bpmNodeButtonXml==null){
			String xmlPath = FileUtil.getClassesPath() + "com/kcp/platform/bpm/util/button.xml".replace("/", File.separator);
			String xml = FileUtil.readFile(xmlPath);
			Document document = Dom4jUtil.loadXml(xml);
			Element root = document.getRootElement();
			String xmlStr = root.asXML();
			bpmNodeButtonXml = (BpmNodeButtonXml)XmlBeanUtil.unmarshall(xmlStr, BpmNodeButtonXml.class);
			bpmNodeButtonXml.init();
		}
		return bpmNodeButtonXml;
	}
}
