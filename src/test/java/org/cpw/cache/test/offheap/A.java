package org.cpw.cache.test.offheap;

public class A {
	
	public String str = "String";

	public A(String str) {
		super();
		this.str = str;
	}

	
	public String getStr() {
		return str;
	}


	public void setStr(String str) {
		this.str = str;
	}


	private A() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "A [str=" + str + "]";
	}
	
	

}
