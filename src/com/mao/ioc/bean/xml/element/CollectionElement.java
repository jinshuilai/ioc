package com.mao.ioc.bean.xml.element;

import java.util.ArrayList;
import java.util.List;

/**
 * 代表list和set集合的
 * @author Mao
 *
 */
public class CollectionElement implements LeafElement {

	private String type;
	private List<LeafElement> list;
	public void setList(List<LeafElement> list) {
		this.list = list;
	}
	public List<LeafElement> getList() {
		return list;
	}

	public void add(LeafElement leafElement){
		this.list.add(leafElement);
	}
	
	public CollectionElement(String type) {
		this.type = type;
	}
	
	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public Object getValue() {
		List<Object> value = new ArrayList<>();
		for (LeafElement le : this.getList()) {
			value.add(le.getValue());
		}
		return value.toArray();
	}

}
