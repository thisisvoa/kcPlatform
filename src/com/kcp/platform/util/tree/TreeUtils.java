
package com.kcp.platform.util.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.kcp.platform.util.CodeGenerator;
import com.kcp.platform.util.JsonParser;



/**
 * <br>
 * <b>类描述:</b>
 * <ol>
 * <li>将Tree转换为客户端JS树组件需要的数据格式；
 * <li>将客户端JS格式的树组件转换为Tree对象
 * <li>将其他类型的对象转换为Tree对象
 * </ol>
 * 
 * @see
 * @since
 */

public class TreeUtils
{
    public static final String DEFAULT_TREE_ID = "0";

    public static final String EXT_TREE_START_HINT = "function(){#@@#";

    public static final String EXT_TREE_END_HINT = "#@@#}";

    /**
     * 将Tree模型转换为dhtmlx 树的数据格式
     * 
     * @param tree
     *            树模型
     * @return
     */
    public static String tree2DhtmlxTreeXml(Tree tree) throws IOException
    {
        Document doc = DocumentHelper.createDocument();
        Element rootElem = doc.addElement("tree");
        rootElem.addAttribute("id", tree.getId() == null ? DEFAULT_TREE_ID
                : tree.getId());
        if (tree.getRootNode() != null)
        {
            rootElem = rootElem.addElement("item");
            rootElem.addAttribute("id", tree.getRootNode() == null ? "root"
                    + CodeGenerator.getUUID() : tree.getRootNode().getNodeId());
            rootElem.addAttribute("text", tree.getRootNode().getLabel());
            rootElem.addAttribute("im0", tree.getRootNode().getIcon0());
        }

        List<TreeNode> rootTns = tree.getTreeRootNodes();

        setFirstTreeNodeSelected(rootTns);
        for (TreeNode treeNode : rootTns)
        {
            buildDhtmlxBranch(rootElem, treeNode);
        }

        StringWriter sw = null;
        XMLWriter writer = null;
        try
        {
            sw = new StringWriter();
            writer = new XMLWriter(sw);
            
            writer.write(doc);
            return sw.toString();

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }finally{
            if(sw != null)
                sw.close();
            if(writer != null)
                writer.close();
        }
    }

    /**
     * 默认设置首个TreeNode是被选中的。
     * 
     * @param treeNodeList
     */
    private static void setFirstTreeNodeSelected(List<TreeNode> treeNodeList)
    {
        if (treeNodeList != null && treeNodeList.size() > 0)
        {
            treeNodeList.get(0).setSelect(true);
            treeNodeList.get(0).setCall(true); // 同时调用处理方法
        }

    }


    /**
     * @param source
     *            需要转换为Tree模型的对象列表,对象必须拥有ID、父ID，名称等基本属性，
     *            无须建立父子之间的层级关系（父节点包含子节点的List）
     * @param mapping
     *            描述source中元素对象属性和TreeNode类属性的映射关系， key为TreeNode的属性名，如
     *            mapping.put("nodeId","id") -->表示Source元素对象的id属性和TreeNode的属性对应；
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Tree beans2Tree(List source, Map<String, String> mapping)
            throws RuntimeException
    {
        return constructTree(beans2TreeNodes(source, mapping), DEFAULT_TREE_ID);
    }

    @SuppressWarnings("unchecked")
    public static Tree beans2Tree(List source, Map<String, String> mapping,
            String parentId) throws RuntimeException
    {
        parentId = StringUtils.isEmpty(parentId) ? DEFAULT_TREE_ID : parentId;

        return constructTree(beans2TreeNodes(source, mapping), parentId);
    }

    /**
     * add by wenjg 
     * @param treeNodeList
     * @param parentId
     * @return
     */
    public static Tree beans2Tree(List<TreeNode> treeNodeList, String parentId) {
        return constructTree(treeNodeList, parentId);
    }

    @SuppressWarnings("unchecked")
    public static List<TreeNode> beans2TreeNodes(List source,
            Map<String, String> mapping) throws RuntimeException
    {

        List<TreeNode> treeNodes = new LinkedList<TreeNode>();
        try
        {
            for (Object obj : source)
            {
                TreeNode tn = new TreeNode();
                for (String key : mapping.keySet())
                {
                    Object value = BeanUtils.getProperty(obj, mapping.get(key));
                    BeanUtils.copyProperty(tn, key, value);
                }
                treeNodes.add(tn);
            }
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return treeNodes;

    }

    /**
     * 将dhtmlx Tree 的XML文档转换为TreeNode 列表
     * 
     * @param dhtmlxTreeXml
     *            dhtmlx树的XML文档字符串
     * @param hasAdditionalRootNode
     *            XML文档是否包括一个额外的父节点，这个父节点 无须保存到数据库中。
     * @return TreeNode的列表
     */
    public static List<TreeNode> dhtmlxXml2TreeNodes(String dhtmlxTreeXml,
            boolean hasAdditionalRootNode)
    {
        List<TreeNode> tns = new LinkedList<TreeNode>();
        try
        {
            Document doc = DocumentHelper.parseText(dhtmlxTreeXml);
            Element rootElem = doc.getRootElement();
            if (hasAdditionalRootNode)
            {
                rootElem = (Element) rootElem.node(0);
            }
            doc2TreeNodes(rootElem, "0", tns);

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return tns;
    }

    /**
     * @param tns
     *            需要转换为目标类对象的TreeNode
     * @param mapping
     *            描述TreeNode和TreeNode类属性的映射关系， key为的属性名，value为目标类的属性名，
     *            如mapping.put("nodeId","id") -->表示TreeNode的nodeId和目标类的id属性对应。
     * @param targetClazz
     *            目标类的类型
     * @return
     */
    public static List treeNodes2Beans(List<TreeNode> tns,
            Map<String, String> mapping, Class targetClazz)
    {
        try
        {
            List targetObjs = new LinkedList();
            for (TreeNode treeNode : tns)
            {
                Object targetObj = targetClazz.newInstance();
                for (String key : mapping.keySet())
                {
                    Object value = BeanUtils.getProperty(treeNode, key);
                    BeanUtils.copyProperty(targetObj, mapping.get(key), value);
                }
                targetObjs.add(targetObj);
            }
            return targetObjs;
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void buildDhtmlxBranch(Element parentElem, TreeNode parentTn)
    {
        if (parentTn.getChildTreeNodes() != null
                && parentTn.getChildTreeNodes().size() > 0)
        {
            Element elem = parentElem.addElement("item");
            buildDhtmlxItem(elem, parentTn);
            for (TreeNode tn : parentTn.getChildTreeNodes())
            {
                buildDhtmlxBranch(elem, tn);
            }
        } else
        {
            Element elem = parentElem.addElement("item");
            buildDhtmlxItem(elem, parentTn);
        }
    }

    private static void buildDhtmlxItem(Element elem, TreeNode treeNode)
    {
        elem.addAttribute("id", treeNode.getNodeId());
        elem.addAttribute("text", treeNode.getLabel());
        elem.addAttribute("im0", treeNode.getIcon0());
        elem.addAttribute("im1", treeNode.getIcon1());
        elem.addAttribute("im2", treeNode.getIcon2());
        elem.addAttribute("tip", treeNode.getTip());
        if (treeNode.isSelect())
            elem.addAttribute("select", "1");
        if (treeNode.isCall())
            elem.addAttribute("call", "1");
        if (treeNode.isOpen())
            elem.addAttribute("open", "1");
        if (treeNode.isChecked())
            elem.addAttribute("checked", "1");

        if (treeNode.isHasChild() || !treeNode.isLeaf())
            elem.addAttribute("child", "1");

        if (treeNode.isNocheckbox())
            elem.addAttribute("nocheckbox", "1");
        
        if (treeNode.isDisabled())
            elem.addAttribute("disabled", "1");
        Map userdatas = treeNode.getUserdatas();
        Set<String> keys = userdatas.keySet();
        for(String key:keys){
        	Object data = userdatas.get(key);
        	if(data!=null){
        		Element userdata = elem.addElement("userdata");
            	userdata.addAttribute("name", key);
            	userdata.setText(data.toString());
        	}
        }
    }


    private static Tree constructTree(List<TreeNode> treeNodes, String parentId)
    {

        // 为了防止TreeNode已经生成ChildNode的情况，在操作之前先清除TreeNode的
        // childNodes中的数据；
        for (TreeNode treeNode : treeNodes)
        {
            treeNode.clearChildeTreeNode();
        }
        Tree tree = new Tree();
        for (TreeNode tn2 : treeNodes)
        {
            if (parentId.equals(tn2.getParentNodeId().trim()))// 为每个根节点创建建立关联的TreeNode对象
            {
                buildTreeBranch(treeNodes, tn2);
                tree.addTreeNode(tn2);
            }
        }
        return tree;
    }

    private static void buildTreeBranch(List<TreeNode> treeNodes,
            TreeNode parentTreeNode)
    {
        List<TreeNode> childTns = getChildTreeNodes(treeNodes, parentTreeNode);

        if (childTns.size() > 0)
        {
            for (TreeNode tn : childTns)
                buildTreeBranch(treeNodes, tn);
        }

        parentTreeNode.setChildTreeNodes(childTns);
    }

    private static List<TreeNode> getChildTreeNodes(List<TreeNode> treeNodes,
            TreeNode parentTn)
    {
        List<TreeNode> childTreeNode = new LinkedList<TreeNode>();
        for (TreeNode treeNode : treeNodes)
        {
            if (treeNode.getParentNodeId() != null
                    && treeNode.getParentNodeId().equals(parentTn.getNodeId()))
            {
                childTreeNode.add(treeNode);
            }
        }
        return childTreeNode;
    }

    /**
     * 将XML样式的结构转换为平面样式的TreeNode 列表（通过父、子节点ID关联的TreeNode列表）
     * 
     * @param elem
     * @param parentId
     * @param tns
     */
    private static void doc2TreeNodes(Element elem, String parentId,
            List<TreeNode> tns)
    {
        if (elem.nodeCount() > 0)
        {
            List elemList = elem.elements();
            for (int i = 0, s = elemList.size(); i < s; i++)
            {
                Element itemElem = (Element) elemList.get(i);
                TreeNode tn = new TreeNode();
                String nodeId = itemElem.attributeValue("id");
                if (nodeId == null)
                {
                    nodeId = CodeGenerator.getUUID();
                }
                tn.setNodeId(nodeId);
                tn.setParentNodeId(parentId);
                tn.setLabel(itemElem.attributeValue("text"));
                tn.setOrderNo(i + 1);
                tn.setIcon0(itemElem.attributeValue("img0"));
                tn.setIcon1(itemElem.attributeValue("img1"));
                tn.setIcon2(itemElem.attributeValue("img2"));
                tns.add(tn);
                doc2TreeNodes(itemElem, nodeId, tns);
            }

        }
    }
   /**
     * 将系统Tree模型转换为dhtmlx 树的数据格式
     *
     * @param tree
     *            树模型
     * @return
     */
    public static String sysParamTree2DhtmlxTreeXml(Tree tree)
    {
        Document doc = DocumentHelper.createDocument();
        Element rootElem = doc.addElement("tree");
        rootElem.addAttribute("id", tree.getId() == null ? DEFAULT_TREE_ID
                : tree.getId());
        if (tree.getRootNode() != null)
        {
            rootElem = rootElem.addElement("item");
            rootElem.addAttribute("id", tree.getRootNode() == null ? "root"
                    + CodeGenerator.getUUID() : tree.getRootNode().getNodeId());
            rootElem.addAttribute("text", tree.getRootNode().getLabel());
            rootElem.addAttribute("im0", tree.getRootNode().getIcon0());
        }
        List<TreeNode> rootTns = tree.getTreeRootNodes();
        setFirstTreeNodeSelected(rootTns);
        for (TreeNode treeNode : rootTns)
        {
            buildDhtmlxBranch(rootElem, treeNode);
        }

        StringWriter sw = new StringWriter();
        XMLWriter writer = new XMLWriter(sw);
        try
        {
            writer.write(doc);
            return sw.toString();

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args)throws Exception{
    	List<TreeNode> treeList = new ArrayList<TreeNode>();
    	TreeNode node1 = new TreeNode();
    	node1.setNodeId("001");
    	node1.setLabel("节点1");
    	node1.setParentNodeId("0");
    	treeList.add(node1);
    	
    	TreeNode node2 = new TreeNode();
    	node2.setNodeId("001001");
    	node2.setLabel("节点2");
    	node2.setParentNodeId("001");
    	node2.addUserData("type", "0");
    	treeList.add(node2);
    	Tree tree = TreeUtils.beans2Tree(treeList, "0");
    	System.out.println(TreeUtils.tree2DhtmlxTreeXml(tree));
    	
    }
}
