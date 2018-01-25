package com.lhh.vista.temp.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TicketInfo {

	private int id;
	private String key;
	private String hopk;
	private String name;
	private float bookingFee;
	private int type;
	private String description;
	private float price;
}
