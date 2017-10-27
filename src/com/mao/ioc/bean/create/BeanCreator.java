package com.mao.ioc.bean.create;

import java.util.List;
/**
 * 实例化一个bean
 * @author Mao
 *
 */
public interface BeanCreator {

	/**
	 * 默认的构造方法
	 * @param className
	 * @return
	 */
	public Object createBeanUseDefaultConstruct(String className);
	
	/**
	 * 自定义的构造方法
	 * @param className
	 * @param args
	 * @return
	 */
	public Object createBeanUseDefineConstruct(String className,List<ConstructArgsVo> args);
}
