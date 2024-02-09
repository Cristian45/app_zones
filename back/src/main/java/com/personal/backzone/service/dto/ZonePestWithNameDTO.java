package com.personal.backzone.service.dto;

import java.io.Serializable;

public class ZonePestWithNameDTO implements Serializable{
	
	ZonePestWithNameDetailDTO [] zonePestWithNameDetailDTO;

	public ZonePestWithNameDetailDTO[] getZonePestWithNameDetailDTO() {
		return zonePestWithNameDetailDTO;
	}

	public void setZonePestWithNameDetailDTO(ZonePestWithNameDetailDTO[] zonePestWithNameDetailDTO) {
		this.zonePestWithNameDetailDTO = zonePestWithNameDetailDTO;
	}	
	
}
