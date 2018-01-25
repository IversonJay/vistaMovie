package com.lhh.vista.customer.s2v;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lhh.vista.customer.s2v.dto.AddOrRemoveConcession;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by liu on 2017/1/1.
 */
@Setter
@Getter
public class RemoveConcessionWarp {
    @JsonProperty(value = "ReturnOrder")
    private Boolean returnOrder = false;
    @JsonProperty(value = "GiftStoreOrder")
    private Boolean giftStoreOrder = false;
    @JsonProperty(value = "SessionId")
    private String sessionId;
    @JsonProperty(value = "UserSessionId")
    private String orderId;
    @JsonProperty(value = "CinemaId")
    private String cinemaId;
    @JsonProperty(value = "ConcessionRemovals")
    private List<AddOrRemoveConcession> concessionRemoves;

    @JsonProperty(value = "OptionalClientClass")
    private String clientClass;
    @JsonProperty(value = "OptionalClientId")
    private String clientId;
    @JsonProperty(value = "OptionalClientName")
    private String clientName;

}
