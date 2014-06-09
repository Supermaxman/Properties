package me.supermaxman.properties;

import java.io.Serializable;

public class HouseType implements Serializable{
	

	private static final long serialVersionUID = 5901541960517265602L;

	private String type;
	
    private String schematic;
    
    private int price;
    
    private int earnings;
    
    public HouseType(String type, String schematic, int price, int earnings) {
    	this.setType(type);
    	this.setSchematic(schematic);
    	this.setPrice(price);
    	this.setEarnings(earnings);
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSchematic() {
		return schematic;
	}

	public void setSchematic(String schematic) {
		this.schematic = schematic;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getEarnings() {
		return earnings;
	}

	public void setEarnings(int earnings) {
		this.earnings = earnings;
	}
}
