package com.mao.ioc.xml.element.parser;

import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mao.ioc.bean.xml.autowire.Autowire;
import com.mao.ioc.bean.xml.document.XmlDocumentHolder;
import com.mao.ioc.bean.xml.element.LeafElement;
import com.mao.ioc.bean.xml.element.PropertyElement;
import com.mao.ioc.bean.xml.element.RefElement;
import com.mao.ioc.bean.xml.element.ValueElement;
import com.mao.ioc.bean.xml.element.loader.ElementLoader;
import com.mao.ioc.bean.xml.element.loader.ElementLoaderImpl;
import com.mao.ioc.bean.xml.element.parser.BeanElementParser;
import com.mao.ioc.bean.xml.element.parser.BeanElementParserImpl;

public class ElementParserImplTest {

	private XmlDocumentHolder xmlHolder;
	private ElementLoader elementLoader;
	private BeanElementParser parser;
	
	@Before
	public void setUp() throws Exception{
		xmlHolder = new XmlDocumentHolder();
		elementLoader = new ElementLoaderImpl();
		String filePath = "test/resources/element/elementParserImpl.xml";
		Document doc = xmlHolder.getDocument(filePath);
		elementLoader.addBeanElements(doc);
		parser = new BeanElementParserImpl();
	}
	
	@After
	public void tearDown() throws Exception{
		xmlHolder = null;
		elementLoader = null;
	}
	
	@Test
	public void testIsLazy(){
		Element e = elementLoader.getBeanElement("test1-1");
		boolean result = parser.isLazy(e);
		assertTrue(result);
		
		//第二个元素
        e = elementLoader.getBeanElement("test1-2");
        result = parser.isLazy(e);
        assertFalse(result);
        //第三个元素
        e = elementLoader.getBeanElement("test1-3");
        result = parser.isLazy(e);
        assertFalse(result);
	}
	
	@Test
	public void testGetConstructorElements(){
		Element e = elementLoader.getBeanElement("test2");
		List<Element> constructorElements = parser.getConstructorArgsElements(e);
		assertEquals(constructorElements.size(), 2);
	}
	
	@Test
	public void testGetAttrubute(){
		Element e = elementLoader.getBeanElement("test3");
		String value = parser.getAttribute(e, "class");
		assertEquals(value, "com.mao.Test5");
	}
	
	@Test
	public void testIsSingleton(){
		Element e = elementLoader.getBeanElement("test4-1");
		boolean result = parser.isSingleton(e);
		assertFalse(result);
		
		e = elementLoader.getBeanElement("test4-2");
		result = parser.isSingleton(e);
		assertTrue(result);
	}
	
	@Test
	public void testGetPropertyElements(){
		Element e = elementLoader.getBeanElement("test6");
		List<Element> elements = parser.getPropertyElements(e);
		assertEquals(elements.size(), 2);
	}
	
	@Test
	public void testGetAutowire(){
		Element e = elementLoader.getBeanElement("test10-1");
		assertEquals(parser.getAttribute(e, "id"), "test10-1");
		Autowire result = parser.getAutowire(e);
		assertEquals(result.getType(), "byName");
		
		e = elementLoader.getBeanElement("test10-2");
		result = parser.getAutowire(e);
		assertEquals(result.getType(), "no");
		
		e = elementLoader.getBeanElement("test10-3");
		result = parser.getAutowire(e);
		assertEquals(result.getType(), "byType");
	}
	
	@Test
	public void testGetConstructorValue(){
		Element e = elementLoader.getBeanElement("test11");
		assertEquals(parser.getAttribute(e, "id"), "test11");
		List<LeafElement> result = parser.getConstructorValue(e);
		assertEquals(result.size(), 2);
		
		ValueElement val = (ValueElement) result.get(0);
		System.out.println(val.getValue());
		System.out.println(val.getClassType());
		assertEquals((String)val.getValue(), "mao");
		
		RefElement re = (RefElement) result.get(1);
		assertEquals((String)re.getValue(), "test11");
	}
	
	@Test
	public void testGetpropertyValue(){
		Element e = elementLoader.getBeanElement("test12");
		List<PropertyElement> eles = parser.getPropertyValue(e);
		assertEquals(eles.size(), 4);
		System.out.println(eles.get(0).getLeafElement().getValue());
		assertEquals(eles.get(0).getName(), "property1");
		assertEquals(eles.get(0).getLeafElement().getValue(), "mao");
        assertEquals(eles.get(0).getLeafElement().getType(), "value");
        System.out.println(eles.get(3).getLeafElement());
        System.out.println(eles.get(3).getLeafElement().getType());
        Object[] obj = (Object[])eles.get(3).getLeafElement().getValue();
        System.out.println(obj[0]);
        System.out.println(obj[1]);
	}
}
