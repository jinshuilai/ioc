package com.mao.ioc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class IocUtil {

	public static Class<?> getClass(String txt) throws ClassNotFoundException {
		if (txt.equals("int")) {
            return Integer.TYPE;
        } else if (txt.equals("boolean")) {
            return Boolean.TYPE;
        } else if (txt.equals("long")) {
            return Long.TYPE;
        } else if (txt.equals("short")) {
            return Short.TYPE;
        } else if (txt.equals("double")) {
            return Double.TYPE;
        } else if (txt.equals("float")) {
            return Float.TYPE;
        } else if (txt.equals("char")) {
            return Character.TYPE;
        } else if (txt.equals("byte")) {
            return Byte.TYPE;
        }
		return Class.forName(txt);
	}
	
	public static Class<?> getClass(Class<?> clazz,String key) throws Exception {
		try{
			Field f = clazz.getDeclaredField(key);
			return f.getType();
		}catch(NoSuchFieldException e){
			String name = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
			Method method = clazz.getDeclaredMethod(name);
			return method.getReturnType();
		}
	}
	
	/**
	 * 只对一些常用的object进行了类型转换
	 * date BigDecimal这些没有将string转换
	 * @param className
	 * @param data
	 * @return
	 */
	public static Object getValue(String className,String data){
		if (isType(className, "integer")) {
            return Integer.parseInt(data);
        } else if (isType(className, "boolean")) {
            return Boolean.valueOf(data);
        } else if (isType(className, "long")) {
            return Long.valueOf(data);
        } else if (isType(className, "short")) {
            return Short.valueOf(data);
        } else if (isType(className, "double")) {
            return Double.valueOf(data);
        } else if (isType(className, "float")) {
            return Float.valueOf(data);
        } else if (isType(className, "Character")) {
            /**
             * 如果是Character类型则取第一个字符
             */
            return data.charAt(0);
        } else if (isType(className, "byte")) {
            return Byte.valueOf(data);
        } else {
        	System.out.println(className+":目前为string没转换");
        	return data;
        }
	}
	
	private static boolean isType(String className,String type){
		className = className.toLowerCase();
		String ld = className.length() > type.length() ? className : type;
		String sd = className.length() > type.length() ? type : className;
		if(ld.contains(sd)){
			return true;
		}
		return false;
	}
}
