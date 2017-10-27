package com.mao.ioc.bean.create;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mao.ioc.testobj.Mao;

public class BeanCreatorImplTest {

	@Test
	public void testCreateBeanUseDefaultConstruct() {
		BeanCreator creator = new BeanCreatorImpl();
		Mao m = (Mao) creator.createBeanUseDefaultConstruct("com.mao.ioc.util.Mao");
		assertEquals(m.getId().intValue(), 0);
	}

	@Test
	public void testCreateBeanUseDefineConstruct() {
		BeanCreator creator = new BeanCreatorImpl();
		List<ConstructArgsVo> args = new ArrayList<>();
		ConstructArgsVo vo = new ConstructArgsVo();
		vo.setType(Integer.class);
		vo.setValue(1);
		args.add(vo);
		vo = new ConstructArgsVo();
		vo.setType(String.class);
		vo.setValue("mao");
		args.add(vo);
		vo = new ConstructArgsVo();
		vo.setType(int.class);
		vo.setValue(6);
		args.add(vo);
		Mao m = (Mao) creator.createBeanUseDefineConstruct("com.mao.ioc.util.Mao",args);
		assertEquals(m.getI(), 6);
	}

}
