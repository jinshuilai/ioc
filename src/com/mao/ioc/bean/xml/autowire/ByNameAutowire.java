package com.mao.ioc.bean.xml.autowire;

/**
 * 按名字匹配
 * @author Mao
 *
 */
public class ByNameAutowire implements Autowire {

	private String type;
	public ByNameAutowire(String type) {
		this.type = type;
	}
	
	@Override
	public String getType() {
		return type;
	}
}
