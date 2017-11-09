package com.example.moveico.adapter;

public class Bean
{
	private int image;
	private String name;
	private String type;
	
	
	public Bean(String name, int image ,String type)
	{
		this.setName(name);
		this.setImage(image);
		this.setType(type);
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public int getImage() {
		return image;
	}


	public void setImage(int image) {
		this.image = image;
	}

}
