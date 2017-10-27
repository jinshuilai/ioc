package com.mao.ioc.bean.xml.autowire;

/**
 * 不自动装配
 * @author Mao
 *
 */
public class NoAutowire implements Autowire {

	private String type;
	
	public NoAutowire(String type) {
		this.type = type;
	}
	
	@Override
	public String getType() {
		return "no";
	}

}
