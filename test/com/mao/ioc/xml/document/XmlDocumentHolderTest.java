package com.mao.ioc.xml.document;

import static org.junit.Assert.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mao.ioc.bean.xml.document.XmlDocumentHolder;

public class XmlDocumentHolderTest {
	private XmlDocumentHolder xmlHolder;

	@Before
	public void setUp() throws Exception{
		xmlHolder = new XmlDocumentHolder();
	}
	
	@After
	public void tearDown() throws Exception{
		xmlHolder = null;
	}
	
	@Test
	public void testGetDocument1() {
		String filePath = "test/resources/xmlDocumentHolder.xml";
		Document doc1 = xmlHolder.getDocument(filePath);
		assertNotNull(doc1);
		Element root = doc1.getRootElement();
		assertEquals(root.getName(), "beans");
		Document doc2 = xmlHolder.getDocument(filePath);
		System.out.println(doc1);
		System.out.println(doc2);
		assertEquals(doc1, doc2);
	}

	@Test(expected = DocumentException.class)
	public void testGetDocument2() {
		String filePath = "test/resources/xmlDocumentHolder2.xml";
		Document doc = xmlHolder.getDocument(filePath);
	}
	
	@Test(expected = DocumentException.class)
	public void testGetDocument3() throws DocumentException {
		String filePath = "test/resources/xmlDocumentHolder3.xml";
		Document doc = xmlHolder.getDocument(filePath);
	}
}
