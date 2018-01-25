package com.lhh.vista.customer.s2v;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RewardWarp {

	@JsonProperty(value = "GetAdvanceBookings")
	private Boolean getAdvanceBookings;
	@JsonProperty(value = "GetAdvanceSeatingRecognitions")
	private Boolean getAdvanceSeatingRecognitions;
	@JsonProperty(value = "GetConcessions")
	private Boolean getConcessions;
	@JsonProperty(value = "GetDiscounts")
	private Boolean getDiscounts;
	@JsonProperty(value = "GetTicketTypes")
	private Boolean getTicketTypes;
	@JsonProperty(value = "SupressSelectedSessionDateTimeFilter")
	private Boolean supressSelectedSessionDateTimeFilter;
	@JsonProperty(value = "UserSessionId")
	private String userSessionId;
	@JsonProperty(value = "OptionalClientClass")
	private String optionalClientClass;
	@JsonProperty(value = "OptionalClientId")
	private String optionalClientId;
	@JsonProperty(value = "OptionalClientName")
	private String optionalClientName;
	
}
