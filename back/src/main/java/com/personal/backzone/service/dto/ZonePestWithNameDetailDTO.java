package com.personal.backzone.service.dto;

import java.io.Serializable;

public class ZonePestWithNameDetailDTO implements Serializable{
	
	Long id;
	Long zoneId;
	String zoneName;
	Long pestId;
	String pestName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getZoneId() {
		return zoneId;
	}
	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public Long getPestId() {
		return pestId;
	}
	public void setPestId(Long pestId) {
		this.pestId = pestId;
	}
	public String getPestName() {
		return pestName;
	}
	public void setPestName(String pestName) {
		this.pestName = pestName;
	}


}
