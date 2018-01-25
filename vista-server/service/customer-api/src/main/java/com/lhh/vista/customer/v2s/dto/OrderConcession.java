package com.lhh.vista.customer.v2s.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liu on 2017/1/5.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderConcession {
    @JsonProperty(value = "ItemId")
    private String itemId;
    @JsonProperty(value = "Quantity")
    private Integer quantity;
}
