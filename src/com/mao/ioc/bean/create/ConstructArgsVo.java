package com.mao.ioc.bean.create;

public class ConstructArgsVo {

	private Class<?> type;
	private Object value;

	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
