package me.porterk.mg.gametype;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum GameType {

	CLASSIC(Classic.class, "Classic", "The classic game mode!", 0);
	
	protected Class<?> c;
	protected String name;
	protected String desc;
	protected int id;
	
	private GameType(Class<?> c, String name, String desc, int id){
		
		this.c = c;
		this.name = name;
		this.desc = desc;
		this.id = id;
		
	}
	
	public void doRound(){
		
		Method m;
		try {
			m = c.getMethod("runGame");
				try {
					m.invoke(c.newInstance());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			
			} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getName(){
		
		return name;
	}
	
	public String getDescription(){
		
		return desc;
	}
	
	public int getID(){
		
		return id;
	}
}
