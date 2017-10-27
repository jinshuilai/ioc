package com.mao.ioc.bean.xml.element.parser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.mao.ioc.bean.create.ConstructArgsVo;
import com.mao.ioc.bean.xml.autowire.Autowire;
import com.mao.ioc.bean.xml.autowire.ByNameAutowire;
import com.mao.ioc.bean.xml.autowire.ByTypeAutowire;
import com.mao.ioc.bean.xml.autowire.NoAutowire;
import com.mao.ioc.bean.xml.element.CollectionElement;
import com.mao.ioc.bean.xml.element.LeafElement;
import com.mao.ioc.bean.xml.element.PropertyElement;
import com.mao.ioc.bean.xml.element.RefElement;
import com.mao.ioc.bean.xml.element.ValueElement;
import com.mao.ioc.util.IocUtil;

/**
 * 针对bean的描述信息进行解构
 * @author Mao
 *
 */
public class BeanElementParserImpl implements BeanElementParser {

	@Override
	public boolean isLazy(Element beanElement) {
		String elementLazy = this.getAttribute(beanElement, "lazy-init");
		Element parentElement = beanElement.getParent();
		//beans是否懒加载
		Boolean parentElementLazy = new Boolean(this.getAttribute(parentElement, "default-lazy-init"));
		if(parentElementLazy){
			if("false".equals(elementLazy)){
				return false;
			}
			//两者为懒加载时，该bean为懒加载
			return true;
		}else{
			if("true".equals(elementLazy)){
				return true;
			}
			//两者不为懒加载时，该bean不为懒加载
			return false;
		}
	}

	@Override
	public List<Element> getConstructorArgsElements(Element element) {
		List<Element> children = element.elements();
		List<Element> result = new ArrayList<>();
		for(Element e : children){
			if("constructor-arg".equals(e.getName())){
				result.add(e);
			}
		}
		return result;
	}

	@Override
	public String getAttribute(Element element, String name) {
		String value = element.attributeValue(name);
		return value;
	}

	@Override
	public boolean isSingleton(Element bean) {
		Boolean singleton = new Boolean(this.getAttribute(bean, "singleton"));
		return singleton;
	}

	@Override
	public List<Element> getPropertyElements(Element element) {
		List<Element> children = element.elements();
		List<Element> result = new ArrayList<>();
		for(Element e : children){
			if("property".equals(e.getName())){
				result.add(e);
			}
		}
		return result;
	}

	@Override
	public Autowire getAutowire(Element element) {
		String type = this.getAttribute(element, "autowire");
		String parentType = this.getAttribute(element.getParent(), "default-autowire");
		if("no".equals(parentType)){
			//beans不自动装配，下面的bean配置是什么就是什么
			if("byName".equals(type)){
				return new ByNameAutowire(type);
			}else if("byType".equals(type)){
				return new ByTypeAutowire(type);
			}
			return new NoAutowire(type);
		}else if("byName".equals(parentType)){
			//beans按名字装配时。下面的装配受覆盖
			if("no".equals(type)){
				return new NoAutowire(type);
			}
			return new ByNameAutowire(type);
		}else if("byType".equals(parentType)){
			//beans按类型装配时。下面的装配受覆盖
			if ("no".equals(type)) {
				return new NoAutowire(type);
			}
			return new ByTypeAutowire(type);
		}
		return new NoAutowire(type);
	}

	@Override
	public List<LeafElement> getConstructorValue(Element element) {
		List<Element> cons = this.getConstructorArgsElements(element);
		List<LeafElement> result = new ArrayList<>();
		try{
			for(Element e : cons){
				List<Element> eles = e.elements();
			//基于下面的结构解析
			//<constructor-arg>
            //<value type="java.lang.Integer">1</value> 只有一个。get'0
            //</constructor-arg>
				LeafElement leafElement = this.getLeafElement(eles.get(0));
				result.add(leafElement);
			}
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<PropertyElement> getPropertyValue(Element element) {
		List<Element> properties = this.getPropertyElements(element);
		List<PropertyElement> result = new ArrayList<>();
		try{
			for(Element e : properties){
				List<Element> eles = e.elements();
				LeafElement leafElement = getLeafElement(eles.get(0));
				String propertyNameAtt = this.getAttribute(e, "name");
				PropertyElement pe = new PropertyElement(propertyNameAtt, leafElement);
				result.add(pe);
			}
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return result;
	}
	
	private LeafElement getLeafElement(Element leafElement) throws ClassNotFoundException {
		String name = leafElement.getName();
		if("value".equals(name)){
			return new ValueElement(this.getValueOfValueElement(leafElement));
		}else if("ref".equals(name)){
			return new RefElement(this.getAttribute(leafElement, "bean"));
		}else if("collection".equals(name)){
			return this.getCollectionElement(leafElement);
		}
		return null;
	}

	/**
	 * 注入了譬如list，set的type
	 * @param leafElement
	 * @return
	 * @throws ClassNotFoundException
	 */
	private LeafElement getCollectionElement(Element leafElement) throws ClassNotFoundException {
		List<LeafElement> temp = new ArrayList<>();
		String typeName = leafElement.attributeValue("type");
		CollectionElement ce = new CollectionElement(typeName);
		List<Element> elements = leafElement.elements();
		for (Element e : elements) {
			String tempName = e.getName();
			if("value".equals(tempName)){
				temp.add(new ValueElement(this.getValueOfValueElement(e)));
			}else if("ref".equals(tempName)){
				temp.add(new RefElement(this.getAttribute(e, "bean")));
			}
		}
		ce.setList(temp);
		return ce;
	}

	/**
	 * value类型的，保留字段类型和值
	 * @param leafElement
	 * @return
	 * @throws ClassNotFoundException
	 */
	private ConstructArgsVo getValueOfValueElement(Element leafElement) throws ClassNotFoundException {
		String typeName = leafElement.attributeValue("type");
		String data = leafElement.getText();
		ConstructArgsVo vo = new ConstructArgsVo();
		vo.setValue(IocUtil.getValue(typeName,data));
		vo.setType(IocUtil.getClass(typeName));
		return vo;
	}

}
