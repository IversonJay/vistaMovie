package com.lhh.vista.customer.v2s;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lhh.vista.customer.v2s.dto.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liu on 2016/12/21.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRes extends StateResult {
    @JsonProperty(value = "Order")
    private Order order;
}
