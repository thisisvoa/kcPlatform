package com.kcp.platform.bpm.graph;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerFactoryConfigurationError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import com.kcp.platform.util.Dom4jUtil;
import com.kcp.platform.util.FileUtil;
import com.kcp.platform.util.StringUtils;

public class GraphUtil
{
  private static final Log logger = LogFactory.getLog(GraphUtil.class);

  /**
   * 将临时xml(保留工作流设计器信息)文件转化为bpmn xml语言标准工作流xml文件
   * @param id
   * @param name
   * @param xml
   * @return
   * @throws TransformerFactoryConfigurationError
   * @throws Exception
   */
  public static String transform(String id, String name, String xml)
    throws TransformerFactoryConfigurationError, Exception
  {
    Map<String, String> map = new HashMap<String, String>();
    map.put("id", id);
    map.put("name", name);

    String xlstPath = FileUtil.getClassesPath() + "com/kcp/platform/bpm/graph/transform.xsl".replace("/", File.separator);

    logger.debug("xlslPath:" + xlstPath);

    xml = xml.trim();
    String str = Dom4jUtil.transXmlByXslt(xml, xlstPath, map);
    str = str.replace("&lt;", "<").replace("&gt;", ">").replace("xmlns=\"\"", "").replace("&amp;", "&");

    Pattern regex = Pattern.compile("name=\".*?\"");
    Matcher regexMatcher = regex.matcher(str);
    while (regexMatcher.find()) {
      String strReplace = regexMatcher.group(0);
      String strReplaceWith = strReplace.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
      str = str.replace(strReplace, strReplaceWith);
    }
    return str;
  }

  public static ShapeMeta transGraph(String xml)throws Exception{
    xml = xml.replace("xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "");
    Document doc = Dom4jUtil.loadXml(xml);
    Element root = doc.getRootElement();
    List list = root.selectNodes("//bpmndi:BPMNShape");
    int minx = 100000;
    int miny = 100000;
    int maxw = 0;
    int maxh = 0;

    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < list.size(); i++) {
      Element el = (Element)list.get(i);
      Element tmp = (Element)el.selectSingleNode("omgdc:Bounds");
      int x = (int)Float.parseFloat(tmp.attributeValue("x"));
      int y = (int)Float.parseFloat(tmp.attributeValue("y"));

      int w = x + (int)Float.parseFloat(tmp.attributeValue("width"));
      int h = y + (int)Float.parseFloat(tmp.attributeValue("height"));

      minx = Math.min(x, minx);
      miny = Math.min(y, miny);

      maxw = Math.max(w, maxw);
      maxh = Math.max(h, maxh);
    }

    List pointList = root.selectNodes("//omgdi:waypoint");
    for (int i = 0; i < pointList.size(); i++) {
      Element tmp = (Element)pointList.get(i);
      int x = (int)Float.parseFloat(tmp.attributeValue("x"));
      int y = (int)Float.parseFloat(tmp.attributeValue("y"));
      minx = Math.min(x, minx);
      miny = Math.min(y, miny);
    }

    for (int i = 0; i < list.size(); i++) {
      Element el = (Element)list.get(i);
      Element tmp = (Element)el.selectSingleNode("omgdc:Bounds");
      int x = (int)Float.parseFloat(tmp.attributeValue("x"));
      int y = (int)Float.parseFloat(tmp.attributeValue("y"));

      int w = (int)Float.parseFloat(tmp.attributeValue("width"));
      int h = (int)Float.parseFloat(tmp.attributeValue("height"));
      x = x - minx + 5;
      y = y - miny + 5;

      String id = el.attributeValue("bpmnElement");

      Element procEl = (Element)root.selectSingleNode("//process/descendant::*[@id='" + id + "']");
      String type = procEl.getName();
      if (type.equals("serviceTask")) {
        String attribute = procEl.attributeValue("class");

        if (attribute != null) {
          if (attribute.equals("com.hotent.platform.service.bpm.MessageTask")) {
            type = "email";
          }
          else if (attribute.equals("com.hotent.platform.service.bpm.ScriptTask")) {
            type = "script";
          }
        }
      }
      Element multiObj = procEl.element("multiInstanceLoopCharacteristics");
      if (multiObj != null)
        type = "multiUserTask";
      Element parent = procEl.getParent();

      String name = procEl.attributeValue("name");

      int zIndex = 10;

      String parentName = parent.getName();
      if (parentName.equals("subProcess")) {
        zIndex = 11;
      }

      DivShape shape = new DivShape(name, x, y, w, h, zIndex, id, type);
      sb.append(shape);
    }
    ShapeMeta shapeMeta = new ShapeMeta(maxw, maxh, sb.toString());
    return shapeMeta;
  }


  public static Map<String, String> getDecisionConditions(String processXml, String decisionNodeId)
  {
    Map<String, String> map = new HashMap<String, String>();
    processXml = processXml.replace("xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "xmlns:bpm='hotent'");
    Document doc = Dom4jUtil.loadXml(processXml);
    Element root = doc.getRootElement();

    List<Element> nodes = root.selectNodes("//sequenceFlow[@sourceRef='" + decisionNodeId + "']");
    for (Element el : nodes) {
      String id = el.attributeValue("targetRef");
      String condition = "";
      Element conditionNode = el.element("conditionExpression");
      if (conditionNode != null) {
        condition = conditionNode.getText().trim();
        condition = StringUtils.trimPrefix(condition, "${");
        condition = StringUtils.trimSufffix(condition, "}");
      }
      map.put(id, condition);
    }
    return map;
  }

  public static String setGraphXml(String sourceNode, Map<String, String> map, String xml)
    throws IOException
  {
    Document doc = Dom4jUtil.loadXml(xml);
    Element root = doc.getRootElement();

    Element node = (Element)root.selectSingleNode("//bg:Gateway[@id='" + sourceNode + "']");
    Element portsEl = node.element("ports");
    List portList = portsEl.elements();

    for (int i = 0; i < portList.size(); i++) {
      Element portEl = (Element)portList.get(i);
      if ((portEl.attribute("x") != null) || (portEl.attribute("y") != null))
      {
        String id = portEl.attributeValue("id");
        Element outNode = (Element)root.selectSingleNode("//bg:SequenceFlow[@startPort='" + id + "']");
        if (outNode != null) {
          String outPort = outNode.attributeValue("endPort");
          Element tmpNode = (Element)root.selectSingleNode("//ciied:Port[@id='" + outPort + "']");
          Element taskNode = tmpNode.getParent().getParent();
          String taskId = taskNode.attributeValue("id");

          Element conditionEl = outNode.element("condition");
          if (conditionEl != null) {
            outNode.remove(conditionEl);
          }
          if (map.containsKey(taskId)) {
            String condition = (String)map.get(taskId);
            Element elAdd = outNode.addElement("condition");
            elAdd.addText(condition);
          }
        }
      }
    }
    return doc.asXML();
  }
}