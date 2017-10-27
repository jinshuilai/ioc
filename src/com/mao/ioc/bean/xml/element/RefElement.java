package com.mao.ioc.bean.xml.element;

/**
 * 代表引用的
 * @author Mao
 *
 */
public class RefElement implements LeafElement {

	private Object value;
	
	public RefElement(Object value) {
		this.value = value;
	}
	
	@Override
	public String getType() {
		return "ref";
	}

	@Override
	public Object getValue() {
		return this.value;
	}

}
