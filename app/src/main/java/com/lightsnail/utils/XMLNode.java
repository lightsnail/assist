package com.lightsnail.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * XML标签节点，包括标签节点名称，该节点拥有的属性，节点文本内容，子节点集合
 * @author: laohu
 * @time:	2014-9-15下午9:10:22
 */
public class XMLNode {
	/**
	 * 节点名称
	 */
	private String nodeName;
	/**
	 * 节点文本值
	 */
	private String nodeValue = "";
	/**
	 * 该节点的属性
	 */
	private XMLNodeAtt attribute;
	/**
	 * 子节点
	 */
	private List<XMLNode> childList;
	
	
	public XMLNode() {
		
	}
	
	public XMLNode(String nodeName) {
		super();
		this.nodeName = nodeName;
		this.childList = new ArrayList<XMLNode>();
	}

	public XMLNode(String nodeName, String tagValue) {
		super();
		this.nodeName = nodeName;
		this.childList = new ArrayList<XMLNode>();
	}

	public XMLNode(String nodeName, XMLNodeAtt attribute) {
		super();
		this.nodeName = nodeName;
		this.attribute = attribute;
		this.childList = new ArrayList<XMLNode>();
	}

	public XMLNode(String nodeName, String nodeValue, XMLNodeAtt attribute) {
		super();
		this.nodeName = nodeName;
		this.nodeValue = nodeValue;
		this.attribute = attribute;
		this.childList = new ArrayList<XMLNode>();
	}
	
	public XMLNode(String nodeName, String nodeValue, XMLNodeAtt attribute,
			List<XMLNode> childList) {
		super();
		this.nodeName = nodeName;
		this.nodeValue = nodeValue;
		this.attribute = attribute;
		if(childList != null) {
			this.childList = childList;
		} else {
			childList = new ArrayList<XMLNode>();
		}
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public XMLNodeAtt getAttribute() {
		return attribute;
	}

	public void setAttribute(XMLNodeAtt attribute) {
		this.attribute = attribute;
	}
	
	public String getNodeValue() {
		return nodeValue;
	}

	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}

	public List<XMLNode> getChildList() {
		return childList;
	}

	public void setChildList(List<XMLNode> childList) {
		this.childList = childList;
	}

	/**
	 * 判断该节点是否有属性
	 * @author: laohu
	 * @time:	2014-9-15下午9:18:28
	 * @return
	 */
	public boolean isHasAttribute() {
		if(this.attribute == null) {
			return false;
		}
		return !this.attribute.isEmpty();
	}
	
	/**
	 * 判断两个节点是否为相同标签名称的节点
	 * @author: laohu
	 * @time:	2014-9-16上午9:35:19
	 * @param node
	 * @return
	 */
	public boolean isNodeNameEquals(XMLNode node) {
		if(this.nodeName != null && node.nodeName != null && 
		   this.nodeName.equals(node.nodeName)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否有子节点
	 * @author: laohu
	 * @time:	2014-9-15下午10:09:36
	 * @return
	 */
	public boolean isHasChild() {
		return childList.size() > 0 ? true : false;
	}
	
	/**
	 * 得到该节点指定下级节点集合(节点名称进行匹配)
	 * @author: laohu
	 * @time:	2014-9-16上午9:44:41
	 * @param tagName				指定的下级节点名称
	 * @param isFirstLevelChild		是否只查找第一级子节点，true：是，false：否
	 * @return
	 * @throws Exception
	 */
	public List<XMLNode> getNodes(String tagName, boolean isFirstLevelChild) throws Exception {
		if(ValueUtil.isEmpty(tagName)) {
			throw new Exception("标签名称不能为空");
		}
		
		List<XMLNode> list = new ArrayList<XMLNode>();
		if(childList.size() <= 0) {
			return list;
		}
		for(XMLNode node : childList) {
			if(tagName.equals(node.getNodeName())) {
				list.add(node);
			}
			if(!isFirstLevelChild) {//当要查找所有子节点时继续往下下级节点里找
				list.addAll(node.getNodes(tagName, isFirstLevelChild));
			}
		}
		
		return list;
	}
	
	/**
	 * 根据节点名称和节点属性得到对应的下级节点集合(节点名称和属性共同进行匹配)
	 * @author: laohu
	 * @time:	2014-9-17上午10:46:12
	 * @param tagName				节点名称
	 * @param nodeAtt				节点属性，属性如果为空则默认只按照节点名称进行查找匹配
	 * @param isFirstLevelChild		是否只查找第一级子节点，true：是，false：否
	 * @return
	 * @throws Exception
	 */
	public List<XMLNode> getNodes(String tagName, XMLNodeAtt nodeAtt, 
			boolean isFirstLevelChild) throws Exception {
		if(ValueUtil.isEmpty(tagName)) {
			throw new Exception("标签名称不能为空");
		}
		
		List<XMLNode> list = new ArrayList<XMLNode>();
		if(childList.size() <= 0) {
			return list;
		}
		
		if(nodeAtt == null || nodeAtt.isEmpty()) {
			return getNodes(tagName, isFirstLevelChild);
		}
		
		Set<String> keys = nodeAtt.getAttributeNames();
		for(XMLNode node : childList) {
			if(tagName.equals(node.getNodeName())) {
				boolean isExit = true;
				for(String key : keys) {//遍历判断所有要查找的属性
					if(!node.getAttribute().isHasAttribute(key, nodeAtt.getAttributeValue(key))) {
						isExit = false;
						break;
					}
				}
				if(isExit) {
					list.add(node);
				}
			}
			if(!isFirstLevelChild) {//当要查找所有子节点时继续往下下级节点里找
				list.addAll(node.getNodes(tagName, nodeAtt, isFirstLevelChild));
			}
		}
		
		return list;
	}
	
	/**
	 * 将该节点的所有指定下级节点的文本值设置为指定值，对于有下级节点的子节点，该方法设置无效
	 * @author: laohu
	 * @time:	2014-9-17下午1:55:55
	 * @param childNodeName			子节点名称
	 * @param childNodeValue		子节点文本内容值
	 */
	public void setChildNodeValue(String childNodeName, String childNodeValue) {
		for(int i = 0; i < childList.size(); i++) {
			if(childNodeName.equals(childList.get(i).getNodeName()) &&
			   !childList.get(i).isHasChild()) {
				childList.get(i).setNodeValue(childNodeValue);
			}
			childList.get(i).setChildNodeValue(childNodeName, childNodeValue);
		}
	}
	
	/**
	 * 将该节点某个位置上的子节点替换为指定节点
	 * @author: laohu
	 * @time:	2014-9-17下午2:39:55
	 * @param location	要替换的子节点的位置索引，从0开始
	 * @param node		替换后的新子节点
	 */
	public void set(int location,XMLNode node) {
		if(this.childList.size() <= location) {
			throw new IndexOutOfBoundsException("要替换的元素位置越界,size:" + this.childList.size() + ",location:" + location);
		}
		
		this.childList.set(location, node);
	}
	
	/**
	 * 将该节点的某个指定子节点替换为指定的新节点
	 * @author: laohu
	 * @time:	2014-9-17下午2:49:40
	 * @param oldNode	要替换的子节点
	 * @param newNode	替换后的新子节点
	 */
	public void set(XMLNode oldNode, XMLNode newNode) {
		int index = this.childList.indexOf(oldNode);
		if(index <= -1) {
			throw new IndexOutOfBoundsException("没有找到要替换的节点");
		}
		this.set(index, newNode);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if(!(o instanceof XMLNode)) {
			return false;
		}
		
		XMLNode tnode = (XMLNode) o;
		
		if(!tnode.getNodeName().equals(this.nodeName) ||
				!tnode.getNodeValue().equals(this.nodeValue)) {
			return false;
		}

		if(!tnode.getAttribute().equals(this.attribute)) {
			return false;
		}
		
		if(!this.isHasChild() && !tnode.isHasChild()) {
			return true;
		}
		
		if(!(this.isHasChild() && tnode.isHasChild())) {
			return false;
		}
		
		if(this.getChildList().size() != tnode.getChildList().size()) {
			return false;
		}
		
		boolean isEquals = true;
		for(int i = 0; i < tnode.getChildList().size(); i++) {
			if(!this.getChildList().contains(tnode.getChildList().get(i))) {
				isEquals = false;
				break;
			}
		}
			
		return isEquals;
	}
	
	@Override
	public String toString() {
		String attributeString = this.attribute.toString();
		
		StringBuffer sb = new StringBuffer("{");
		sb.append("'nodeName':");
		sb.append("'" +this.nodeName + "'");
		sb.append(", ");
		sb.append("'nodeValue':");
		sb.append("'" +this.nodeValue + "'");
		sb.append(", ");
		sb.append("'attribute':");
		sb.append(attributeString);
		
		sb.append(", 'children':[");
		for(int i = 0; i < childList.size(); i++) {
			if(i > 0) {
				sb.append(", ");
			}
			sb.append(childList.get(i).toString());
		}
		sb.append("]");
		
		sb.append("}");
		
		return sb.toString();
	}
}
