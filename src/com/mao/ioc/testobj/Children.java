package com.mao.ioc.testobj;

public class Children extends Parent {

	private String name;
	private Mao mao;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Mao getMao() {
		return mao;
	}
	public void setMao(Mao mao) {
		this.mao = mao;
	}
	public Children() {
	}
	public Children(String name) {
		super();
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
