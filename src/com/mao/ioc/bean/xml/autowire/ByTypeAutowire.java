package com.mao.ioc.bean.xml.autowire;

/**
 * 按类型匹配
 * @author Mao
 *
 */
public class ByTypeAutowire implements Autowire {

	private String type;
	public ByTypeAutowire(String type) {
		this.type = type;
	}
	
	@Override
	public String getType() {
		return type;
	}

}
