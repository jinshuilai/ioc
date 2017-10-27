package com.mao.ioc.context;

public class XmlApplicationContext extends AbstractApplicationContext {

	public XmlApplicationContext(String[] xmlPaths){
		initElements(xmlPaths);
		createBeanInstances();
	}
}
