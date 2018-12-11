package com.lightsnail.utils.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectUtils {


	private void getClassName() {
		 Class  demo1=null; 
         Class  demo2=null; 
         Class  demo3=null; 
         try{ 
             //一般尽量采用这种形式 
             demo1=Class.forName("Reflect.Person"); 
         }catch(Exception e){ 
             e.printStackTrace(); 
         } 
//         demo2=new Person().getClass(); 
//         demo3=Person.class; 
           
         System.out.println("类名称   "+demo1.getName()); 
//         System.out.println("类名称   "+demo2.getName()); 
//         System.out.println("类名称   "+demo3.getName()); 		
	}
    
	public static String printMethods(Class clazz) {

		String returnString = "";
		Method method[] = clazz.getMethods();

		 for(int i=0;i<method.length;++i){ 
	            Class<?> returnType=method[i].getReturnType(); 
	            Class<?> para[]=method[i].getParameterTypes(); 
	            int temp=method[i].getModifiers(); 
	            returnString += Modifier.toString(temp)+" ";
	            returnString += returnType.getName( )+" ";
	            returnString += method[i].getName( )+" ";
	            returnString += "(";
	            for(int j=0;j<para.length;++j){ 
		            returnString += para[j].getName()+" "+"arg"+j;
	                if(j<para.length-1){ 
			            returnString += ",";
	                } 
	            } 
	            Class<?> exce[]=method[i].getExceptionTypes(); 
	            if(exce.length>0){ 
		            returnString += ") throws ";
	                for(int k=0;k<exce.length;++k){ 
			            returnString +=exce[k].getName()+" ";
	                    if(k<exce.length-1){ 
				            returnString +=",";
	                    } 
	                } 
	            }else{ 
		            returnString +=")";
	            } 
	            returnString +="\n";
	        } 				
	
		 return returnString;
		 
	}
}
