package com.mao.ioc.context;

/**
 * ioc应用容器接口
 * @author Mao
 *
 */
public interface ApplicationContext {

	/**
	 * 根据id找到bean对应的实例
	 * @param id
	 * @return
	 */
	public Object getBeanInstance(String id);
	
	/**
	 * 查看容器中是否存在该id的bean
	 * @param id
	 * @return
	 */
	public boolean beanIsExist(String id);
	
	/**
	 * 判断该bean是否单例
	 * @param id
	 * @return
	 */
	public boolean isSingleton(String id);
	
	/**
	 * 从容器中获取实例，找不到返回null
	 * @param id
	 * @return
	 */
	public Object getBeanWithoutCreate(String id);
}
