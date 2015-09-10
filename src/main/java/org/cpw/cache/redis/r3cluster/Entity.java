package org.cpw.cache.redis.r3cluster;


public class Entity implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5290136896672732878L;

	private String name;
	
	private long id;
	
	private transient String t;

	public Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Entity(String name, long id, String t) {
		super();
		this.name = name;
		this.id = id;
		this.t = t;
	}

	@Override
	public String toString() {
		return "Entity [name=" + name + ", id=" + id + ", t=" + t + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	
		
	
	
	

}
