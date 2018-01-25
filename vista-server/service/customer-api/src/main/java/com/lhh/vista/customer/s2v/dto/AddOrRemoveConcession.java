package com.lhh.vista.customer.s2v.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2017/1/5.
 */
@Setter
@Getter
public class AddOrRemoveConcession {
    @JsonProperty(value = "ItemId")
    private String itemId;
    @JsonProperty(value = "Quantity")
    private Integer quantity;

    @JsonProperty(value = "RecognitionId")
    private String recognitionId;
    @JsonProperty(value = "RecognitionSequenceNumber")
    private Integer recognitionSequenceNumber;

    public AddOrRemoveConcession() {
    }

    public AddOrRemoveConcession(String itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public AddOrRemoveConcession(String itemId, Integer quantity, String recognitionId, Integer recognitionSequenceNumber) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.recognitionId = recognitionId;
        this.recognitionSequenceNumber = recognitionSequenceNumber;
    }
}
