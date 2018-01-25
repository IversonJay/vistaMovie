package com.lhh.vista.web.dto;

import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.service.dto.UserTicket;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by liu on 2016/12/26.
 */
@Setter
@Getter
public class CheckTicketRes extends BaseResult{
    private Map<String, List<UserTicket>> tickets;
    private boolean needSelect;
}
