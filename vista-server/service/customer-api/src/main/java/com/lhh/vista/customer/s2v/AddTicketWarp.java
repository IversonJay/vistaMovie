package com.lhh.vista.customer.s2v;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by liu on 2016/12/21.
 */
@Setter
@Getter
public class AddTicketWarp {
    @JsonProperty(value = "UserSessionId")
    private String orderId;
    
    @JsonProperty(value = "CinemaId")
    private String cinemaId;

    @JsonProperty(value = "SessionId")
    private String sessionId;

    @JsonProperty(value = "ReturnOrder")
    private Boolean returnOrder = true;

    @JsonProperty(value = "OptionalClientClass")
    private String clientClass;
    @JsonProperty(value = "OptionalClientId")
    private String clientId;
    @JsonProperty(value = "OptionalClientName")
    private String clientName;

    @JsonProperty(value = "TicketTypes")
    private List<TicketType> ticketTypes;

    @JsonProperty(value = "SelectedSeats")
    private List<SelectedSeat> selectedSeats;

    @Getter
    @Setter
    public static class TicketType {
        @JsonProperty(value = "TicketTypeCode")
        private String ticketType;//;票类编码

        @JsonProperty(value = "Qty")
        private Integer qty;//票数

        @JsonProperty(value = "PriceInCents")
        private Integer price;//票的价格

        @JsonProperty(value = "BookingFeeOverride")
        private String bookingFeeOverride;//定位费
        
        @JsonProperty(value = "OptionalBarcode")
        private String optionalBarcode;//定位费

        public TicketType(String ticketType, Integer qty, Integer price) {
            this.ticketType = ticketType;
            this.qty = qty;
            this.price = price;
        }
        
        public TicketType(String ticketType, Integer qty, Integer price, String bookingFeeOverride) {
            this.ticketType = ticketType;
            this.qty = qty;
            this.price = price;
            this.bookingFeeOverride = bookingFeeOverride;
        }
        
        public TicketType(String ticketType, Integer qty, Integer price, String bookingFeeOverride, String optionalBarcode) {
            this.ticketType = ticketType;
            this.qty = qty;
            this.price = price;
            this.bookingFeeOverride = bookingFeeOverride;
            this.optionalBarcode = optionalBarcode;
        }

        public TicketType() {
        }
    }

    @Getter
    @Setter
    public static class SelectedSeat {
        @JsonProperty(value = "AreaCategoryCode")
        private String areaCode;//区域编码

        @JsonProperty(value = "AreaNumber")
        private Short areaNum;//区域num

        @JsonProperty(value = "RowIndex")
        private Integer row;//行

        @JsonProperty(value = "ColumnIndex")
        private Integer column;//列

        public SelectedSeat() {
        }

        public SelectedSeat(String areaCode, Short areaNum, Integer row, Integer column) {
            this.areaCode = areaCode;
            this.areaNum = areaNum;
            this.row = row;
            this.column = column;
        }
    }


}
