package com.mao.ioc.bean.create;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mao.ioc.util.IocUtil;

/**
 * 处理属性域property
 * @author Mao
 *
 */
public class PropertyHandlerImpl implements PropertyHandler {

	@Override
	public Object setProperties(Object obj, Map<String, Object> properties) {
		Class<?> clazz = obj.getClass();
		try {
			for(String key : properties.keySet()){
				String setterName = this.getSetterMethodName(key);
				Class<?> argClass = IocUtil.getClass(clazz,key);
				Method setterMethod = getSetterMethod(clazz,setterName,argClass);
				setterMethod.invoke(obj, properties.get(key));
			}
			return obj;
		} catch (NoSuchMethodException e) {
			throw new PropertyException("对应的setter方法没找到"+e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new PropertyException("wrong argument "+e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new PropertyException(e.getMessage());
		}
	}

	private Method getSetterMethod(Class<?> clazz, String setterName,
			Class<?> argClass) throws NoSuchMethodException {
		Method argClassMethod = this.getMethod(clazz, setterName, argClass);
		if(argClassMethod == null){
			//这里考虑子类set进父类的问题
			List<Method> methods = this.getMethods(clazz,setterName);
			Method method = this.findMethod(argClass,methods);
			if(method == null){
				throw new NoSuchMethodException(setterName);
			}
			return method;
		}else {
			return argClassMethod;
		}
	}

	private Method findMethod(Class<?> argClass, List<Method> methods) {
		for (Method method : methods) {
			if(this.isMethodArgs(method,argClass)){
				return method;
			}
		}
		return null;
	}

	private boolean isMethodArgs(Method method, Class<?> argClass) {
		Class<?>[] types = method.getParameterTypes();
		if(types.length == 1){
			try {
				argClass.asSubclass(types[0]);
				return true;
			} catch (ClassCastException e) {
				return false;
			}
		}
		return false;
	}

	private List<Method> getMethods(Class<?> clazz, String setterName) {
		List<Method> result = new ArrayList<>();
		for(Method m : clazz.getMethods()){
			if(m.getName().equals(setterName)){
				Class<?>[] types = m.getParameterTypes();
				if(types.length == 1){
					result.add(m);
				}
			}
		}
		return result;
	}

	private Method getMethod(Class<?> clazz, String setterName, Class<?> argClass) {
		try {
			Method method = clazz.getMethod(setterName, argClass);
			return method;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	private String getSetterMethodName(String key) {
		String word = key.substring(0, 1).toUpperCase();
		return "set"+ replaceFirstword(key, word);
	}
	
	private String getMethodNameWithOutSet(String name) {
		String key = name.substring(3);
		String word = key.substring(0, 1).toLowerCase();
		return replaceFirstword(key, word);
	}
	
	private String replaceFirstword(String key,String word){
		return word + key.substring(1);
	}

	@Override
	public Map<String, Method> getSetterMethodsMap(Object object) {
		List<Method> methods = this.getSetterMethodsList(object);
		Map<String, Method> result = new HashMap<>();
		for (Method method : methods) {
			String propertyName = this.getMethodNameWithOutSet(method.getName());
			result.put(propertyName, method);
		}
		return result;
	}

	private List<Method> getSetterMethodsList(Object object) {
		Class<?> clazz = object.getClass();
		Method[] methods = clazz.getMethods();
		List<Method> result = new ArrayList<>();
		for (Method method : methods) {
			if(method.getName().startsWith("set")){
				result.add(method);
			}
		}
		return result;
	}

	@Override
	public void executeMethod(Object object, Object argBean, Method method) {
		try {
			Class<?>[] parameterTypes = method.getParameterTypes();
			
			if(parameterTypes.length == 1){
				if(isMethodArgs(method, parameterTypes[0])){
					method.invoke(object, argBean);
				}
			}
		} catch (Exception e) {
			throw new BeanCreateException("自动装配异常 "+e.getMessage());
		}
		
	}

}
