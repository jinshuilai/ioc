package com.mao.ioc.bean.create;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.mao.ioc.testobj.Mao;

public class PropertyHandlerImplTest {
	
	private PropertyHandler propertyHandler;
	
	@Before
	public void setUp(){
		propertyHandler = new PropertyHandlerImpl();
	}

	@Test
	public void testSetProperties() {
		Mao m = new Mao();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 1);
		map.put("name", "tom");
		map.put("i", 2);
		m = (Mao) propertyHandler.setProperties(m, map);
		assertEquals(m.getId().intValue(), 1);
		assertEquals(m.getI(), 2);
		assertEquals(m.getName(), "tom");
	}

	@Test
	public void testGetSetterMethodsMap() {
		Mao m = new Mao();
		Map<String, Method> map = propertyHandler.getSetterMethodsMap(m);
		Set<Entry<String,Method>> set = map.entrySet();
		for (Entry<String, Method> entry : set) {
			System.out.println(entry.getKey()+":"+entry.getValue());
		}
	}

	@Test
	public void testExecuteMethod() throws NoSuchMethodException, SecurityException {
		Mao m = new Mao();
		Method method = m.getClass().getDeclaredMethod("setI", int.class);
		propertyHandler.executeMethod(m, 2, method);
		assertEquals(m.getI(), 2);
	}

}
