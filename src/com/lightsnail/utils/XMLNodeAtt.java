package com.lightsnail.utils;

import java.util.HashMap;
import java.util.Set;

/**
 * XML节点属性对象
 * @author: laohu
 * @time:	2014-9-15下午9:01:19
 */
public class XMLNodeAtt {
	
	/**
	 * 属性键值对
	 */
	private HashMap<String, String> attMap;
	
	public XMLNodeAtt() {
		attMap = new HashMap<String, String>();
	}
	
	public XMLNodeAtt(HashMap<String, String> attMap) {
		super();
		this.attMap = attMap;
	}

	public void addAttribute(String name, String value) {
		attMap.put(name, value);
	}
	
	public void deleteAttribute(String name) {
		if(attMap.containsKey(name)) {
			attMap.remove(name);
		}
	}
	
	/**
	 * 判断属性是否为空
	 * @author: laohu
	 * @time:	2014-9-15下午9:12:10
	 * @return
	 */
	public boolean isEmpty() {
		if(attMap != null) {
			return attMap.size() > 0 ? false : true;
		}
		return true;
	}
	
	/**
	 * 得到属性名称的Set集合
	 * @author: laohu
	 * @time:	2014-9-15下午9:02:02
	 * @return
	 */
	public Set<String> getAttributeNames() {
		return attMap.keySet();
	}
	
	/**
	 * 根据属性名称得到对应的属性值
	 * @author: laohu
	 * @time:	2014-9-15下午9:02:16
	 * @param name	属性名称
	 * @return		没有该属性则返回null
	 */
	public String getAttributeValue(String name) {
		if(attMap.containsKey(name)) {
			return attMap.get(name);
		}
		
		return null;
	}
	
	/**
	 * 判断该属性列表中是否含有某个指定属性
	 * @author: laohu
	 * @time:	2014-9-17上午10:38:23
	 * @param name		属性名称
	 * @param value		属性值
	 * @return			如果属性名称和其对应的属性值都相等则返回true，否则返回false
	 */
	public boolean isHasAttribute(String name, String value) {
		if(attMap.containsKey(name)) {
			if(attMap.get(name).equals(value)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		if(!(o instanceof XMLNodeAtt)) {
			return false;
		}
		XMLNodeAtt tAtt = (XMLNodeAtt)o;
		
		if(this.isEmpty() && tAtt.isEmpty()) {
			return true;
		}
		
		if(!(!this.isEmpty() && !tAtt.isEmpty())) {
			return false;
		}
		
		boolean isEquals = true;
		
		Set<String> keys = tAtt.getAttributeNames();

		for(String key : keys) {
			if(!isHasAttribute(key, tAtt.getAttributeValue(key))) {
				isEquals = false;
				break;
			}
		}
		
		return isEquals;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("{");
		Set<String> keys = this.getAttributeNames();
		for(String key : keys) {
			if(sb.length() > 1) {
				sb.append(", ");
			}
			sb.append("'" + key + "'");
			sb.append(":");
			sb.append("'" + this.getAttributeValue(key) + "'");
		}
		sb.append("}");
		return sb.toString();
	}
}
