package com.mao.ioc.bean.xml.element;

/**
 * 叶子元素的代表
 * 例如property下会有value，ref，collection类型的
 * @author Mao
 *
 */
public interface LeafElement {

	public String getType();
	
	public Object getValue();
}
