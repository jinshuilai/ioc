package com.mao.ioc.bean.create;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 处理属性域
 * @author Mao
 *
 */
public interface PropertyHandler {

	/**
	 * 给obj设置properties的值
	 * @param obj
	 * @param properties map的key为obj的field
	 * @return
	 */
	public Object setProperties(Object obj,Map<String, Object> properties);
	
	/**
	 * 获得所有的set方法
	 * @param object
	 * @return
	 */
	public Map<String, Method> getSetterMethodsMap(Object object);
	
	/**
	 * 反射执行一个方法
	 * @param object
	 * @param argBean
	 * @param method
	 */
	public void executeMethod(Object object,Object argBean,Method method);
}
