package com.lhh.vista.customer.v2s.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by soap on 2016/12/13.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetTicketsForSessionRes {
    @JsonProperty(value = "ResponseCode")
    private Integer responseCode;

    @JsonProperty(value = "Tickets")
    private List<Ticket> tickets;

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Ticket {
        @JsonProperty(value = "AreaCategoryCode")
        private String areaCategoryCode;//区域类别编码
        @JsonProperty(value = "Description")
        private String description;// 票类名称
        @JsonProperty(value = "PriceInCents")
        private Integer priceInCents;// 单价
        @JsonProperty(value = "TicketTypeCode")
        private String ticketTypeCode;//票类编码
        @JsonProperty(value = "HOPK")
        private String hopk;//票类编码
        
        
        
        
        //自定义
        private String barcode;
    }
}