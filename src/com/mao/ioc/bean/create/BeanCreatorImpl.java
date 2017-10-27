package com.mao.ioc.bean.create;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
/**
 * 实例对象
 * @author Mao
 *
 */
public class BeanCreatorImpl implements BeanCreator {

	@Override
	public Object createBeanUseDefaultConstruct(String className) {
		try {
			Class<?> clazz = Class.forName(className);
			return clazz.newInstance();
		}catch (ClassNotFoundException e){
			throw new BeanCreateException("没有找到"+className+"这个类");
		}catch (Exception e) {
			throw new BeanCreateException(e.getMessage());
		}
	}

	@Override
	public Object createBeanUseDefineConstruct(String className, List<ConstructArgsVo> args) {
		Class<?>[] argsClass = this.getArgsClasses(args);
		Object[] argsValue = this.getArgsValues(args);
		try {
			Class<?> clazz = Class.forName(className);
			Constructor<?> constructor = getConstructor(clazz,argsClass);
			return constructor.newInstance(argsValue);
		} catch (ClassNotFoundException e){
			e.printStackTrace();
			throw new BeanCreateException(className+"类没有找到");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new BeanCreateException("没有找到"+className+"中的构造方法");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeanCreateException(e.getMessage());
		}
	}

	/**
	 * 找到匹配类型的构造方法
	 * @param clazz
	 * @param argsClass
	 * @return
	 * @throws NoSuchMethodException
	 */
	private Constructor<?> getConstructor(Class<?> clazz, 
			Class<?>[] argsClass) throws NoSuchMethodException{
		//直接这样拿，注入是子类类型的，会无法匹配到父类的
		Constructor<?> constructor = getProcessConstructor(clazz,argsClass);
		if (constructor == null) {
			//没找到就拿出所有构造，比较参数是否子类
			Constructor<?>[] constructors = clazz.getConstructors();
			for (Constructor<?> c : constructors) {
				Class<?>[] tempClass = c.getParameterTypes();
				if(tempClass.length == argsClass.length){
					if(isSameArgs(argsClass,tempClass)){
						return c;
					}
				}
			}
		}else{
			return constructor;
		}
		throw new NoSuchMethodException("找不到指定的构造器");
	}

	private boolean isSameArgs(Class<?>[] argsClass, Class<?>[] tempClass) {
		boolean isSame = true;
		for (int i = 0; i < argsClass.length; i++) {
			try {
				//转换判断是否是后者的子类，不是的话会抛出异常
				argsClass[i].asSubclass(tempClass[i]);
			} catch (Exception e) {
				isSame = false;
				break;
			}
		}
		return isSame;
	}

	private Constructor<?> getProcessConstructor(Class<?> clazz, Class<?>[] argsClass) {
		try {
			Constructor<?> constructor = clazz.getConstructor(argsClass);
			return constructor;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	private Class<?>[] getArgsClasses(List<ConstructArgsVo> args) {
		List<Class<?>> result = new ArrayList<>();
		for (ConstructArgsVo arg : args) {
			result.add(arg.getType());
		}
		Class<?>[] a = new Class[result.size()];
		return result.toArray(a);
	}

	private Object[] getArgsValues(List<ConstructArgsVo> args) {
		List<Object> result = new ArrayList<>();
		for (ConstructArgsVo arg : args) {
			result.add(arg.getValue());
		}
		Object[] a = new Object[result.size()];
		return result.toArray(a);
	}
}
