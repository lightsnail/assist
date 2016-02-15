package com.lightsnail.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

/**
 * XML文件流获取工具类
 * @author laohu
 *
 */
public class XMLStreamUtil {
	private final static String TAG = "XMLAnalysis";
	
	private static XMLStreamUtil stream = null;
	
	private XMLStreamUtil() {
		
	}
	
	/**
	 * 获取唯一实例对象
	 * @author: laohu
	 * @time:	2014-9-16下午4:46:39
	 * @return
	 */
	public static XMLStreamUtil getInstance() {
		if(stream == null) {
			stream = new XMLStreamUtil();
		}
		return stream;
	}
	
	/**
	 * 获取XML文件的输入流
	 * @author: laohu
	 * @time:	2014-9-16下午1:50:02
	 * @param context
	 * @param xmlName	XML文件的完整路径
	 * @param type		XML文件所在目录类型，分如下三种：src目录下，assets目录下，SDCard下
	 * @return
	 */
	public InputStream getXMLInputStream(Context context, String xmlName, XMLType type) {
		InputStream is = null;
		try {
			switch(type) {
				case SRC:
					is = getSrcXMLInputStream(xmlName);
					break;
				case ASSETS:
					is = getAssetsXMLInputStream(context, xmlName);
					break;
				case SDCARD:
					is = getSdcardXMLInputStream(xmlName);
					break;
			}

		} catch (IOException e) {
			Log.e(TAG, "获取XML文件流错误", e);
		}
		
		return is;
	}
	
	/**
	 * 获取src目录下的xml文件输入流
	 * @param activity
	 * @param xmlName	文件名
	 * @return
	 */
	private InputStream getSrcXMLInputStream(String xmlName) {
		return this.getClass().getClassLoader().getResourceAsStream(xmlName);
	}
	
	/**
	 * 获取XML文件的输入流
	 * @param activity
	 * @param xmlPath		xml文件路径
	 * @return
	 * @throws IOException	xml文件没有找到
	 */
	private InputStream getAssetsXMLInputStream(Context context, String xmlPath) throws IOException {
		return context.getAssets().open(xmlPath);
	}
	
	/**
	 * 获取存储卡中XML文件的输入流
	 * @param xmlPath
	 * @return
	 * @throws FileNotFoundException
	 */
	private InputStream getSdcardXMLInputStream(String xmlPath) throws FileNotFoundException {
		return new FileInputStream(new File(xmlPath));
	}
	
}
