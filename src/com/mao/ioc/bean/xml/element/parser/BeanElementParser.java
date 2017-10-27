package com.mao.ioc.bean.xml.element.parser;

import java.util.List;

import org.dom4j.Element;

import com.mao.ioc.bean.xml.autowire.Autowire;
import com.mao.ioc.bean.xml.element.LeafElement;
import com.mao.ioc.bean.xml.element.PropertyElement;
/**
 * 针对element进行解析
 * 获得各种属性的值
 * @author Mao
 *
 */
public interface BeanElementParser {

	/**
	 * 判断是否延迟加载
	 * @param beanElement
	 * @return
	 */
	public boolean isLazy(Element beanElement);
	
	/**
	 * 获得构造函数的element描述信息
	 * @param bean
	 * @return
	 */
	public List<Element> getConstructorArgsElements(Element bean);
	
	/**
	 * 获得属性名字为name的值
	 * @param element
	 * @param name
	 * @return
	 */
	public String getAttribute(Element element,String name);
	
	/**
	 * 判断bean是否单例
	 * @param bean
	 * @return
	 */
	public boolean isSingleton(Element bean);
	
	/**
	 * 获得bean下的所有property元素
	 * @param bean
	 * @return
	 */
	public List<Element> getPropertyElements(Element bean);
	
	/**
	 * 获得bean的装载方式
	 * @param bean
	 * @return
	 */
	public Autowire getAutowire(Element bean);
	
	/**
	 * 获得构造器的值，包括value类型和ref引用类型的
	 * @param bean
	 * @return
	 */
	public List<LeafElement> getConstructorValue(Element bean);
	
	/**
	 * 获得所有property的value和ref
	 * @param bean
	 * @return
	 */
	List<PropertyElement> getPropertyValue(Element bean);
}
