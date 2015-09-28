package com.kcp.platform.util.tree;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>类描述:</b> <br>
 *   树节点
 * @see
 * @since
 */

public class TreeNode
{
    private String nodeId;

    private String parentNodeId;

    private String label;

    /**
     * 叶子图标
     */
    private String icon0;

    /**
     * 分支打开图标
     */
    private String icon1;

    /**
     * 分支关闭图标
     */
    private String icon2;

    private int orderNo;

    private String url;

    private String tip;

    private String cls;
    
    private List<TreeNode> childTreeNodes ;
    
    private boolean isSelect=false; //是否选中
    private boolean isOpen=false; //是否打开
    private boolean isCall=false; //是否要调用单击事件
    private boolean isChecked=false;//如果是checked类型，默认是否选中
    private boolean hasChild = false; //判断该节点是否有子节点    
    private boolean nocheckbox = false; //在整棵树都设置成checkbox时，设置该节点是否带有复选框
    private boolean disabled = false; //是否可以对复选框进行操作：1为不行，0为行
    
    private Map<String,Object> userdatas = new HashMap<String,Object>();//自定义属性
    
    /**
     * 添加自定义属性
     * @param name
     * @param data
     */
    public void addUserData(String name, Object data){
    	userdatas.put(name, data);
    }
    
    public Map<String, Object> getUserdatas() {
		return userdatas;
	}

	public void setUserdatas(Map<String, Object> userdatas) {
		this.userdatas = userdatas;
	}

	private String tabIcon;

    public String getNodeId()
    {
        return nodeId;
    }

    public void setNodeId(String nodeId)
    {
        this.nodeId = nodeId;
    }

    public String getParentNodeId()
    {
        return parentNodeId;
    }

    public void setParentNodeId(String parentNodeId)
    {
        this.parentNodeId = parentNodeId;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getIcon0()
    {
        return icon0;
    }

    public void setIcon0(String icon0)
    {
        this.icon0 = icon0;
    }

    public String getIcon1()
    {
        return icon1;
    }

    public void setIcon1(String icon1)
    {
        this.icon1 = icon1;
    }

    public String getIcon2()
    {
        return icon2;
    }

    public void setIcon2(String icon2)
    {
        this.icon2 = icon2;
    }

    public int getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(int orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getTip()
    {
        return tip;
    }

    public void setTip(String tip)
    {
        this.tip = tip;
    }

    public String getCls()
    {
        return cls;
    }

    public void setCls(String cls)
    {
        this.cls = cls;
    }
    
    public int compareTo(TreeNode treeNode){
       Assert.notNull(treeNode);
       if(this.orderNo == treeNode.getOrderNo()){
           return 0;
       }else{
           return this.orderNo > treeNode.getOrderNo()?1:-1;
       } 
    }

    public void setChildTreeNodes(List<TreeNode> childTreeNodes)
    {
        this.childTreeNodes = childTreeNodes;
    }

    public List<TreeNode> getChildTreeNodes()
    {
        return childTreeNodes;
    }
    public void clearChildeTreeNode(){
        if(childTreeNodes != null) childTreeNodes.clear();
    }

    public boolean isOpen()
    {
        return isOpen;
    }

    public void setOpen(boolean isOpen)
    {
        this.isOpen = isOpen;
    }

    public boolean isSelect()
    {
        return isSelect;
    }

    public void setSelect(boolean isSelect)
    {
        this.isSelect = isSelect;
    }
    
    /**
     * @return the isCall
     */
    public boolean isCall()
    {
        return isCall;
    }

    /**
     * @param isCall the isCall to set
     */
    public void setCall(boolean isCall)
    {
        this.isCall = isCall;
    }

    /**
     * @return the isChecked
     */
    public boolean isChecked()
    {
        return isChecked;
    }

    /**
     * @param isChecked the isChecked to set
     */
    public void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }

    /**
     * @return the hasChild
     */
    public boolean isHasChild()
    {
        return hasChild;
    }

    /**
     * @param hasChild the hasChild to set
     */
    public void setHasChild(boolean hasChild)
    {
        this.hasChild = hasChild;
    }

    /**
     * @return the nocheckbox
     */
    public boolean isNocheckbox()
    {
        return nocheckbox;
    }

    /**
     * @param nocheckbox the nocheckbox to set
     */
    public void setNocheckbox(boolean nocheckbox)
    {
        this.nocheckbox = nocheckbox;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled()
    {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    /**
     * 判断是否是叶子节点
     * add by chenmk 2007.10.19
     * 
     * @return
     */
    public boolean isLeaf(){
        return this.childTreeNodes == null || this.childTreeNodes.size() == 0;
    }
    
    /**
     * 添加子节点列表
     * add by chenmk 2007.10.19
     * 
     * @param treeNodeList
     */
    public void addChildNodeList(List<TreeNode> treeNodeList){
        
        if(treeNodeList == null || treeNodeList.size() == 0)
            return;
        
        for(TreeNode tn : treeNodeList)
            addChildNode(tn);
    }
    
    /**
     * 添加一个子节点
     * add by chenmk 2007.10.19
     * 
     * @param treeNode
     */
    public void addChildNode(TreeNode treeNode){
        
        /*
         * 如果要添加的节点为空，则返回 
         */
        if(treeNode == null)
            return;
        
       /*
        * 如果当前列表的节点为空的话，则直接添加子节点
        */ 
        if(this.childTreeNodes == null){
            this.childTreeNodes = new ArrayList<TreeNode>();
            this.childTreeNodes.add(treeNode);
            treeNode.setParentNodeId(this.nodeId);
            return;
        }
        
        /*
         * 判断要添加的节点是否已经存在于该节点的子节点列表中，如果存在则返回
         */
        for(TreeNode tn : this.childTreeNodes){
            if(tn.getNodeId().equals(treeNode.getNodeId()))
                return;
        }
        
        /*
         * 在子节点列表中加入该节点，并且设置要添加的子节点的父节点ID
         */
        this.childTreeNodes.add(treeNode);
        treeNode.setParentNodeId(this.nodeId);
    }
    
    /**
     * 根据给定的节点ID递归判断是否存在于该节点(包括其子节点)下
     * add by chenmk 2007.10.19
     * 
     * @param nodeId
     * @return
     */    
    public TreeNode isExsitedTreeNode(String nodeId){
        
        if(this.nodeId.equals(nodeId))
            return this;
        
        if(this.isLeaf())
            return null;
        
        for(TreeNode treeNode : this.childTreeNodes){
            
            TreeNode tn = treeNode.isExsitedTreeNode(nodeId);
            
            if(tn != null)
                return tn;
        }
        
        return null;
    }

    public String getTabIcon()
    {
        return this.tabIcon;
    }

    public void setTabIcon(String tabIcon)
    {
        this.tabIcon = tabIcon;
    }
}
