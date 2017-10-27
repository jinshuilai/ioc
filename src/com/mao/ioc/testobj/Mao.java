package com.mao.ioc.testobj;

public class Mao {

	private Integer id;
	private String name;
	private int i;
	
	public Mao() {
		this.id = 0;
	}
	
	public Mao(Integer id, String name, int i) {
		super();
		this.id = id;
		this.name = name;
		this.i = i;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getI() {
		return i;
	}
	
	public void setI(int i) {
		this.i = i;
	}
	
	public String info() {
		return "编号:"+id+",名字:"+name+",岁数:"+i;
	}
	
}
