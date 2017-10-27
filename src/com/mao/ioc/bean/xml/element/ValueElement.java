package com.mao.ioc.bean.xml.element;

import com.mao.ioc.bean.create.ConstructArgsVo;
/**
 * 代表直接设入的
 * @author Mao
 *
 */
public class ValueElement implements LeafElement {

	private Object value;
	private Class<?> classType;
	
	public ValueElement(ConstructArgsVo vo) {
		this.value = vo.getValue();
		this.classType = vo.getType();
	}
	
	@Override
	public String getType() {
		
		return "value";
	}

	@Override
	public Object getValue() {
		
		return this.value;
	}

	public Class<?> getClassType() {
		return classType;
	}
}
