package com.jeeplus.modules.oa.entity;

public class TripArrangements {
	private String ariOrg;
	private String airDest;
	private String hotel;
	private String other;
	public TripArrangements() {
		super();
	}
	
	public TripArrangements(String ariOrg, String airDest, String hotel, String other) {
		super();
		this.ariOrg = ariOrg;
		this.airDest = airDest;
		this.hotel = hotel;
		this.other = other;
	}

	public String getAriOrg() {
		return ariOrg;
	}
	public void setAriOrg(String ariOrg) {
		this.ariOrg = ariOrg;
	}
	public String getAirDest() {
		return airDest;
	}
	public void setAirDest(String airDest) {
		this.airDest = airDest;
	}
	public String getHotel() {
		return hotel;
	}
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
	
}
