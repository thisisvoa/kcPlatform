/**
 * @(#)com.casic27.platform.util.GB2Alpha.java
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

package com.casic27.platform.util.tree;

import java.util.LinkedList;
import java.util.List;

/**
 * <br>
 * <b>类描述:</b> <br>
 *   树模型
 * @see
 * @since
 */
public class Tree
{
    private List<TreeNode> rootTreeNodes = new LinkedList<TreeNode>();

    private String defaultCloseFloderIcon;

    private String defaultOpenFloderIcon;

    private String defaultFileIcon;
    
    private String id;
    
    private TreeNode rootNode;
    
    public void setId(String id)
    {
        this.id = id;
    }
    /**
     * 添加根节点，根节点必须是已经建立树节点关联对象
     * @param tn
     */
    public void addTreeNode(TreeNode tn){
        rootTreeNodes.add(tn);
    }
    public List<TreeNode> getTreeRootNodes()
    {
        return rootTreeNodes;
    }

    /**
     *
     * @return 
     */
    public List<TreeNode> getRootTreeNodes() {
        return rootTreeNodes;
    }
    /**
     *
     * @param rootTreeNodes 
     */
    public void setRootTreeNodes(List<TreeNode> rootTreeNodes) {
        if(rootTreeNodes == null || rootTreeNodes.size() == 0)
            return;
        
        this.rootTreeNodes = rootTreeNodes;
    }
    public String getDefaultOpenFloderIcon()
    {
        return defaultOpenFloderIcon;
    }

    public void setDefaultOpenFloderIcon(String defaultOpenFloderIcon)
    {
        this.defaultOpenFloderIcon = defaultOpenFloderIcon;
    }

    public String getDefaultFileIcon()
    {
        return defaultFileIcon;
    }

    public void setDefaultFileIcon(String defaultFileIcon)
    {
        this.defaultFileIcon = defaultFileIcon;
    }

    public String getDefaultCloseFloderIcon()
    {
        return defaultCloseFloderIcon;
    }
    public String getId()
    {
        return id;
    }
    public TreeNode getRootNode()
    {
        return rootNode;
    }
    public void setRootNode(TreeNode rootNode)
    {
        this.rootNode = rootNode;
    }
}
