package com.lhh.vista.customer.v2s.value;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RewardRes {
	
	@JsonProperty(value = "AdvanceBookingList")
	private List<RewardInfo> advanceBookingList;
	@JsonProperty(value = "AdvanceSeatingList")
	private List<RewardInfo> advanceSeatingList;
	@JsonProperty(value = "ConcessionList")
	private List<RewardInfo> concessionList;
	@JsonProperty(value = "DiscountList")
	private List<RewardInfo> discountList;
	@JsonProperty(value = "TicketTypeList")
	private List<RewardInfo> ticketTypeList;
	
	@JsonProperty(value = "Result")
	private Integer result;
	

    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
	public static class RewardInfo {
		@JsonProperty(value = "Description")
		private String description;
		@JsonProperty(value = "LatestDateAvail")
		private String latestDateAvail;
		@JsonProperty(value = "Name")
		private String name;
		@JsonProperty(value = "QtyAvailable")
		private Integer qtyAvailable;
		@JsonProperty(value = "QtyEarned")
		private Integer qtyEarned;
		@JsonProperty(value = "RecognitionID")
		private String recognitionID;
		@JsonProperty(value = "SequenceNumber")
		private Integer sequenceNumber;
		@JsonProperty(value = "VistaID")
		private String vistaID;
		
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getLatestDateAvail() {
			return latestDateAvail;
		}
		public void setLatestDateAvail(String latestDateAvail) {
			String aString = latestDateAvail.substring(latestDateAvail.indexOf("(") + 1);
			String bString = aString.substring(0, aString.indexOf("+"));
			System.out.println(bString);
			Date date = new Date(Long.parseLong(bString));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			this.latestDateAvail = dateFormat.format(date);
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getQtyAvailable() {
			return qtyAvailable;
		}
		public void setQtyAvailable(Integer qtyAvailable) {
			this.qtyAvailable = qtyAvailable;
		}
		public Integer getQtyEarned() {
			return qtyEarned;
		}
		public void setQtyEarned(Integer qtyEarned) {
			this.qtyEarned = qtyEarned;
		}
		public String getRecognitionID() {
			return recognitionID;
		}
		public void setRecognitionID(String recognitionID) {
			this.recognitionID = recognitionID;
		}
		public Integer getSequenceNumber() {
			return sequenceNumber;
		}
		public void setSequenceNumber(Integer sequenceNumber) {
			this.sequenceNumber = sequenceNumber;
		}
		public String getVistaID() {
			return vistaID;
		}
		public void setVistaID(String vistaID) {
			this.vistaID = vistaID;
		}
	}
	
}


