package com.lhh.vista.customer.v2s.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by liu on 2016/12/2.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CinemasRes {
    private List<CinemasRes.Value> value;

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Value {
        @JsonProperty(value = "ID")
        private String id;
        @JsonProperty(value = "Name")
        private String name;
        @JsonProperty(value = "Address1")
        private String address1;
        @JsonProperty(value = "Address2")
        private String address2;
        @JsonProperty(value = "City")
        private String city;

        @JsonProperty(value = "Latitude")
        private Double latitude;
        @JsonProperty(value = "Longitude")
        private Double longitude;

        @JsonProperty(value = "ParkingInfo")
        private String parkingInfo;
        
        @JsonProperty(value = "LoyaltyCode")
        private Integer loyaltyCode;
    }
}
