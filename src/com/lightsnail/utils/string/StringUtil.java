package com.lightsnail.utils.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.lightsnail.utils.AppLog;

public class StringUtil {
	final static  String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";   
    public   static   String stringFilter(String   str)   throws   PatternSyntaxException   {      
        // 只允许字母和数字        
        // String   regEx  =  "[^a-zA-Z0-9]";                      
           // 清除掉所有特殊字符   
	  Pattern   p   =   Pattern.compile(regEx);      
	  Matcher   m   =   p.matcher(str);     
	  return   m.replaceAll("").trim();      
  }      
}
