package com.mao.ioc.xml.element.loader;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mao.ioc.bean.xml.document.XmlDocumentHolder;
import com.mao.ioc.bean.xml.element.loader.ElementLoader;
import com.mao.ioc.bean.xml.element.loader.ElementLoaderImpl;

public class ElementLoaderImplTest {

	XmlDocumentHolder xmlHodel;
	ElementLoader elementLoader;
	
	@Before
	public void setUp() throws Exception{
		xmlHodel = new XmlDocumentHolder();
		elementLoader = new ElementLoaderImpl();
	}
	
	@After
	public void tearDown() throws Exception{
		xmlHodel = null;
		elementLoader = null;
	}
	
	@Test
	public void testAddElements(){
		String filePath = "test/resources/element/elementLoaderImpl.xml";
		Document document = xmlHodel.getDocument(filePath);
		Assert.assertNotNull(document);
		elementLoader.addBeanElements(document);
		Element e = elementLoader.getBeanElement("test1");
		Assert.assertNotNull(e);
		for(Iterator iter = elementLoader.getBeanElements().iterator();iter.hasNext();){
            System.out.println(iter.next());
        }
	}
}
