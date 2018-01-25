package com.lhh.vista.temp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Voucher {

	@JsonProperty(value = "Description")
	private String description;
	@JsonProperty(value = "EndTime")
	private String endTime;
	@JsonProperty(value = "StartTime")
	private String startTime;
	@JsonProperty(value = "VoucherBarCode")
	private String voucherBarCode;
	@JsonProperty(value = "VoucherName")
	private String voucherName;
	

}
