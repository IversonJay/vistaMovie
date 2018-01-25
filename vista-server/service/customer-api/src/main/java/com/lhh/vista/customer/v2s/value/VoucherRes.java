package com.lhh.vista.customer.v2s.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherRes {

	@JsonProperty(value = "description")
	private String description;
	@JsonProperty(value = "endTime")
	private String endTime;
	@JsonProperty(value = "startTime")
	private String startTime;
	@JsonProperty(value = "voucherBarCode")
	private String voucherBarCode;
	@JsonProperty(value = "voucherName")
	private String voucherName;
	
	
	private String status;
	
}
