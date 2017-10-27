package com.mao.ioc.context;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.mao.ioc.testobj.Children;
import com.mao.ioc.testobj.Mao;
import com.mao.ioc.testobj.Parent;

public class XmlApplicationContextTest {
	ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception{
		ctx = new XmlApplicationContext(new String[]{"/resources/application.xml"});
	}

	@Test
	public void test() {
		Children ch = (Children) ctx.getBeanWithoutCreate("children");
		assertNotNull(ch);
		System.out.println(ch.getId()+":"+ch.getName());
		Mao mao = ch.getMao();
		System.out.println(mao.info());
	}

}
