package com.kcp.platform.common.authorization.menutree;

import java.util.List;

import com.kcp.platform.util.StringUtils;

/**
 * <pre>
 * 类描述：
 * </pre>
 * <pre>
 * </pre>
 * @author 林斌树(linbinshu@kcp.com)
 */
public class DefaultMenuTreeHTMLBuilder implements MenuTreeHTMLBuilder {
	/**
	 * 应用的上下文
	 */
	private String ctx;
	private static String NODE_HTML_TEMPLATE="<li><a><span>{menuName}</span></a>";
	
	private static String LEAF_HTML_TEMPLATE="<li><a onclick=\"toTab('{menuId}','{menuName}','{menuUrl}')\"><span class=\"text_slice\">{menuName}</span></a></li>";
	
	public DefaultMenuTreeHTMLBuilder(String ctx){
		this.ctx = ctx;
	}
	@Override
	public String build(MenuTree tree) {
		List<MenuNode> menuNodeList = tree.getNodeList();
		StringBuilder sb = new StringBuilder();
		for(MenuNode menuNode:menuNodeList){
			sb.append("<div class=\"arrowlistmenu\">")
				  .append("<div class=\"menuheader expandable\"><span class=\"icon_xxzy\"></span>")
				  .append(menuNode.getMenuName())
				  .append("</div>")
				  .append("<ul class=\"categoryitems\">");
			//创建子节点HTML
			if(menuNode.isHasChildren()){
				List<MenuNode> childrens = menuNode.getChildrens();
				for(MenuNode node:childrens){
					buildMenuNode(sb, node);
				}
			}
			sb.append("</ul>").append("</div>");
		}
		return sb.toString();
	}
	/**
	 * 创建节点的List
	 * @param sb
	 * @param menuNode
	 */
	private void buildMenuNode(StringBuilder sb, MenuNode menuNode){
		if(menuNode.isHasChildren()){
			sb.append(NODE_HTML_TEMPLATE.replace("{menuName}", menuNode.getMenuName()));
			sb.append("<ul>");
			List<MenuNode> nodeList = menuNode.getChildrens();
			for(MenuNode node:nodeList){
				buildMenuNode(sb,node);
			}
			sb.append("</ul>");
			sb.append("</li>");
		}else{
			String menuId = "menu"+menuNode.getMenuId();
			String url = menuNode.getMenuUrl();
			StringBuilder urlSB = new StringBuilder();
			if(!StringUtils.isEmpty(url)){
				if(!url.startsWith("http")){
					if(!url.startsWith("/")){
						urlSB.append("/");
					}
					urlSB.append(ctx).append(url);
				}else{
					urlSB.append(url);
				}
			}
			sb.append(LEAF_HTML_TEMPLATE.replace("{menuId}", menuId)
							.replace("{menuUrl}", urlSB.toString())
							.replace("{menuName}", menuNode.getMenuName())
							.replace("{menuName}", menuNode.getMenuName()));
			
		}
	}
	
	public String getCtx() {
		return ctx;
	}

	public void setCtx(String ctx) {
		this.ctx = ctx;
	}
	
	public static void main(String[] args){
	}
	
}
