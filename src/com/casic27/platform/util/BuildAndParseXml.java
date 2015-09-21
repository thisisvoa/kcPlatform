/**
 * @(#)com.casic27.platform.util.BuildAndParseXml.java
 * 版权声明 航天光达科技有限公司, 版权所有 违者必究
 *
 *<br> Copyright:： Copyright (c) 2012
 *<br> Company： 航天光达科技有限公司
 *<br> Date：Apr 12, 2012
 *————————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *—————————————————————————————————————
 */
package com.casic27.platform.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
@SuppressWarnings("unchecked")
public class BuildAndParseXml {
	/**
	 * list中的第一个为总返回 码
	 * 其余各项为各行的ldxxbh,和返回码
	 * @param xml
	 * @return
	 */
	public static List<Map<String,Object>> parseDqbRetXml(String xml){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			Document document = DocumentHelper.parseText(xml);
			// Document document = reader.read(new File("d:/1.xml"));
			Element root = document.getRootElement();
			Element ResultCode = root.element("ResultCode");
			String retcode=(String)ResultCode.getData();
//			System.out.println(retcode);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ResultCode", retcode);
			list.add(map);
			Element ResultRows = root.element("ResultRows");
			List<Element> elelist =ResultRows.elements("Row");
			for(int i=0;i<elelist.size();i++){
				Map<String, Object> map2 = new HashMap<String, Object>();
				Element row = elelist.get(i);
				String ldxxbh = row.attributeValue("KeyValue");
				String rowcode = (String)row.getData();
//				System.out.println(ldxxbh);
//				System.out.println(rowcode);
				map2.put("ldxxbh", ldxxbh);
				map2.put("rowcode", rowcode);
				list.add(map2);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args) {
		parseDqbRetXml("<ResultSet><ResultCode>000</ResultCode><ResultRows><Row KeyValue='1'>001</Row>" +
				"<Row KeyValue='2'>002</Row><Row KeyValue='4'>004</Row><Row KeyValue='3'>003</Row>" +
						"<Row KeyValue='5'>005</Row></ResultRows></ResultSet>");
	}
}
