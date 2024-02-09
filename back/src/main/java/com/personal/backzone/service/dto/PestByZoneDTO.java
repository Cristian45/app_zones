package com.personal.backzone.service.dto;

import java.io.Serializable;

public class PestByZoneDTO implements Serializable{
	
	private String[] pets;

	public String[] getPets() {
		return pets;
	}

	public void setPets(String[] pets) {
		this.pets = pets;
	}
	
	
}
