package com.mao.ioc.bean.xml.element.loader;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * 根据上层提供的document
 * 为下层提供bean的element
 * @author Mao
 *
 */
public class ElementLoaderImpl implements ElementLoader {

	/**
	 * 存储所有bean对应的element描述
	 * key对应的是bean的id
	 */
	Map<String, Element> beanElements = new HashMap<>();
	
	@Override
	public void addBeanElements(Document document) {
		List<Element> elementList = document.getRootElement().elements();
		for (Element e : elementList) {
			String id = e.attributeValue("id");
			this.beanElements.put(id, e);
		}
	}

	@Override
	public Element getBeanElement(String id) {
		return beanElements.get(id);
	}

	@Override
	public Collection<Element> getBeanElements() {
		return beanElements.values();
	}

}
