package com.mao.ioc.bean.xml.element.loader;
/**
 * 载入一个document下的所有bean
 */
import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.Element;

public interface ElementLoader {

	/**
	 * 将document下的所有bean解析出来存储
	 * @param document
	 */
	public void addBeanElements(Document document);
	
	/**
	 * 根据bean的id获取element描述
	 * @param id
	 * @return
	 */
	public Element getBeanElement(String id);
	
	/**
	 * 取出所有的bean
	 * @return
	 */
	public Collection<Element> getBeanElements();
}
