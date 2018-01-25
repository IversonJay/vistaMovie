package com.lhh.vista.customer.v2s;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2017/1/4.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMemberItemRes extends StateResult {
    @JsonProperty(value = "ConcessionList")
    private List<Item> concessionList;

    @JsonProperty(value = "TicketTypeList")
    private List<Item> ticketTypeList;

    @JsonProperty(value = "DiscountList")
    private List<Item> discountList;

    @JsonProperty(value = "AdvanceBookingList")
    private List<Item> advanceBookingList;

    @JsonProperty(value = "AdvanceSeatingList")
    private List<Item> advanceSeatingList;

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {
        @JsonProperty(value = "BalanceTypeID")
        private String balanceTypeID;

        @JsonProperty(value = "Description")
        private String description;

        @JsonProperty(value = "DescriptionAlt")
        private String descriptionAlt;

        @JsonProperty(value = "DisplayItem")
        private Boolean displayItem;

        @JsonProperty(value = "IsManuallySelected")
        private Boolean isManuallySelected;
        @JsonProperty(value = "LatestDateAvail")
        private String latestDateAvail;
        @JsonProperty(value = "MessageText")
        private String messageText;
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

        @JsonProperty(value = "PricingStructure")
        private PricingStructure pricingStructure;

        @Override
        public String toString() {
            return "Item{" +
                    "RecognitionID='" + balanceTypeID + '\'' +
                    ", messageText='" + messageText + '\'' +
                    '}';
        }
    }

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PricingStructure {
        @JsonProperty(value = "DollarAmountOff")
        private String dollarAmountOff;
        @JsonProperty(value = "PercentageOff")
        private String percentageOff;
        @JsonProperty(value = "PointsCost")
        private Integer pointsCost;
        @JsonProperty(value = "PriceToUse")
        private Integer priceToUse;
    }


    public List<GetMemberItemRes.Item> toList(List<String> used) {
        //去掉这行代码则奖励不可重复抽到
        used.clear();
        System.out.println(used);

        List<GetMemberItemRes.Item> memberItems = new ArrayList<>();

        if (this.getAdvanceBookingList() != null) {
            for (GetMemberItemRes.Item item : this.getAdvanceBookingList()) {
                if (!used.contains(item.getRecognitionID())) {
                    memberItems.add(item);
                }
            }
        }
        if (this.getAdvanceSeatingList() != null) {
            for (GetMemberItemRes.Item item : this.getTicketTypeList()) {
                if (!used.contains(item.getRecognitionID())) {
                    memberItems.add(item);
                }
            }
        }
        if (this.getConcessionList() != null) {
            for (GetMemberItemRes.Item item : this.getDiscountList()) {
                if (!used.contains(item.getRecognitionID())) {
                    memberItems.add(item);
                }
            }
        }
        if (this.getDiscountList() != null) {
            for (GetMemberItemRes.Item item : this.getConcessionList()) {
                if (!used.contains(item.getRecognitionID())) {
                    memberItems.add(item);
                }
            }
        }
        if (this.getTicketTypeList() != null) {
            for (GetMemberItemRes.Item item : this.getAdvanceSeatingList()) {
                if (!used.contains(item.getRecognitionID())) {
                    memberItems.add(item);
                }
            }
        }
        System.out.println(memberItems);
        return memberItems;
    }
}
