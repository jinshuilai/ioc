package com.mao.ioc.context;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;

import com.mao.ioc.bean.create.BeanCreateException;
import com.mao.ioc.bean.create.BeanCreator;
import com.mao.ioc.bean.create.BeanCreatorImpl;
import com.mao.ioc.bean.create.ConstructArgsVo;
import com.mao.ioc.bean.create.PropertyHandler;
import com.mao.ioc.bean.create.PropertyHandlerImpl;
import com.mao.ioc.bean.xml.autowire.Autowire;
import com.mao.ioc.bean.xml.autowire.ByNameAutowire;
import com.mao.ioc.bean.xml.autowire.ByTypeAutowire;
import com.mao.ioc.bean.xml.autowire.NoAutowire;
import com.mao.ioc.bean.xml.document.DocumentHolder;
import com.mao.ioc.bean.xml.document.XmlDocumentHolder;
import com.mao.ioc.bean.xml.element.CollectionElement;
import com.mao.ioc.bean.xml.element.LeafElement;
import com.mao.ioc.bean.xml.element.PropertyElement;
import com.mao.ioc.bean.xml.element.RefElement;
import com.mao.ioc.bean.xml.element.ValueElement;
import com.mao.ioc.bean.xml.element.loader.ElementLoader;
import com.mao.ioc.bean.xml.element.loader.ElementLoaderImpl;
import com.mao.ioc.bean.xml.element.parser.BeanElementParser;
import com.mao.ioc.bean.xml.element.parser.BeanElementParserImpl;
/**
 * ioc容器，提供扩展
 * @author Mao
 *
 */
public abstract class AbstractApplicationContext implements ApplicationContext {
	//文档持有对象
	protected DocumentHolder documentHolder = new XmlDocumentHolder();
	//元素加载对象
	protected ElementLoader elementLoader = new ElementLoaderImpl();
	//元素解析对象
	protected BeanElementParser elementParser = new BeanElementParserImpl();
	//实例类
	protected BeanCreator beanCreator = new BeanCreatorImpl();
	//处理属性注入
	protected PropertyHandler propertyHandler = new PropertyHandlerImpl();
	//将单例的bean实例化，全部保存在这，key为id
	protected Map<String, Object> beanInstances = new HashMap<>();
	
	//先将所有decoment加载
	protected void initElements(String[] xmlPaths) {
		try {
			URL classPathUrl = AbstractApplicationContext.class.getClassLoader().getResource(".");
			String classPath = URLDecoder.decode(classPathUrl.getPath(),"utf-8");
			for (String path : xmlPaths) {
				Document doc = documentHolder.getDocument(classPath+path);
				elementLoader.addBeanElements(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//创建一个bean实例
	protected Object createBeanInstance(String id) {
		Element e = elementLoader.getBeanElement(id);
		if(e == null){
			throw new BeanCreateException("没有找到"+id+"对应的bean元素");
		}
		Object result = this.instanceBeanElement(e);
		System.out.println("创建bean:"+id);
		System.out.println("该bean的对象是:"+result);
		Autowire autowire = elementParser.getAutowire(e);
		if(autowire instanceof ByTypeAutowire){
			autowireByType(result);
		}else if(autowire instanceof ByNameAutowire){
			autowireByName(result);
		}else if(autowire instanceof NoAutowire){
			this.setterInject(result,e);
		}
		return result;
	}

	protected void autowireByName(Object result) {
		Map<String, Method> methods = propertyHandler.getSetterMethodsMap(result);
		for (String key : methods.keySet()) {
			Element ele = elementLoader.getBeanElement(key);
			if(ele == null){
				continue;
			}
			Object beanInstance = this.getBeanInstance(key);
			Method method = methods.get(key);
			propertyHandler.executeMethod(result, beanInstance, method);
			System.out.println("执行"+method.getName()+"方法给对象:"+result+"注入"+beanInstance);
		}
	}

	protected void setterInject(Object obj, Element beanElement) {
		List<PropertyElement> properties = elementParser.getPropertyValue(beanElement);
		Map<String, Object> propertiesMap = this.getPropertyArgs(properties);
		propertyHandler.setProperties(obj, propertiesMap);
	}

	protected Map<String, Object> getPropertyArgs(List<PropertyElement> properties) {
		Map<String, Object> result = new HashMap<>();
		for (PropertyElement p : properties) {
			LeafElement le = p.getLeafElement();
			if (le instanceof RefElement) {
				result.put(p.getName(), this.getBeanInstance((String)le.getValue()));
			}else if (le instanceof ValueElement) {
				result.put(p.getName(), le.getValue());
			}else if (le instanceof CollectionElement) {
				if(this.childIsValueElement((CollectionElement)le)){
					if ("list".equals(le.getType())) {
						result.put(p.getName(), this.arrayToArrayList((Object[])le.getValue()));
					}else{
						result.put(p.getName(), this.arrayToHashSet((Object[])le.getValue()));
					}
				}else{
					if ("list".equals(le.getType())) {
						result.put(p.getName(), this.arrayToArrayList(this.getValuesIfChildIsRefElement(le)));
					}else{
						result.put(p.getName(), this.arrayToHashSet(this.getValuesIfChildIsRefElement(le)));
					}
				}
			}
		}
		return result;
	}

	protected Object[] getValuesIfChildIsRefElement(LeafElement le) {
		List<Object> templist = new ArrayList<>();
		for (Object o : (Object[])le.getValue()) {
			templist.add(this.getBeanInstance((String)o));
		}
		return templist.toArray();
	}

	protected Object arrayToHashSet(Object[] value) {
		Set<Object> temp = new HashSet<>();
		for (Object o : value) {
			temp.add(o);
		}
		return temp;
	}

	protected Object arrayToArrayList(Object[] value) {
		List<Object> temp = new ArrayList<>();
		for (Object o : value) {
			temp.add(o);
		}
		return temp;
	}

	protected boolean childIsValueElement(CollectionElement ce) {
		if(ce.getList().get(0) instanceof ValueElement){
			return true;
		}
		return false;
	}

	protected void autowireByType(Object result) {
		Map<String, Method> methods = propertyHandler.getSetterMethodsMap(result);
		for (String key : methods.keySet()) {
			//TODO
			Element ele = elementLoader.getBeanElement(key);
			if(ele == null){
				continue;
			}
			Object beanInstance = this.getBeanInstance(key);
			Method method = methods.get(key);
			propertyHandler.executeMethod(result, beanInstance, method);
			System.out.println("执行"+method.getName()+"方法给对象:"+result+"注入"+beanInstance);
		}
	}

	protected Object instanceBeanElement(Element e) {
		String className = elementParser.getAttribute(e, "class");
		List<Element> constructorElements = elementParser.getConstructorArgsElements(e);
		if(constructorElements.size() == 0){
			return beanCreator.createBeanUseDefaultConstruct(className);
		}else {
			List<ConstructArgsVo> args = getConstructArgs(e);
			return beanCreator.createBeanUseDefineConstruct(className, args);
		}
	}

	protected List<ConstructArgsVo> getConstructArgs(Element e) {
		List<LeafElement> datas = elementParser.getConstructorValue(e);
		List<ConstructArgsVo> result = new ArrayList<>();
		for(LeafElement d : datas){
			if (d instanceof ValueElement) {
				ConstructArgsVo vo = new ConstructArgsVo();
				ValueElement temp = (ValueElement) d;
				vo.setValue(temp.getValue());
				vo.setType(temp.getClassType());
				result.add(vo);
			}else if (d instanceof RefElement) {
				ConstructArgsVo vo = new ConstructArgsVo();
				RefElement temp = (RefElement) d;
				String refId = (String) temp.getValue();
				vo.setValue(this.getBeanInstance(refId));
				result.add(vo);
			}
		}
		return result;
	}
	
	protected void createBeanInstances(){
		Collection<Element> elements = elementLoader.getBeanElements();
		for (Element e : elements) {
			boolean lazy = elementParser.isLazy(e);
			if(!lazy){
				String id = e.attributeValue("id");
				Object bean = this.getBeanInstance(id);
				if(bean == null){
					handleBean(id);
				}
			}
		}
	}

	protected Object handleBean(String id) {
		Object beanInstance = createBeanInstance(id);
		if(isSingleton(id)){
			this.beanInstances.put(id, beanInstance);
		}
		return beanInstance;
	}
	
	@Override
	public boolean isSingleton(String id) {
		Element e = elementLoader.getBeanElement(id);
		return elementParser.isSingleton(e);
	}
	
	@Override
	public Object getBeanInstance(String id) {
		Object beanInstance = this.beanInstances.get(id);
		if(beanInstance == null){
			beanInstance = handleBean(id);
		}
		return beanInstance;
	}
	
	@Override
	public boolean beanIsExist(String id) {
		Element e = elementLoader.getBeanElement(id);
		return (e==null) ? false:true;
	}
	
	@Override
	public Object getBeanWithoutCreate(String id) {
		return this.beanInstances.get(id);
	}
}
