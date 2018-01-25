package com.lhh.vista.temp.dto;

import com.lhh.vista.temp.model.Cinema;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by soap on 2016/12/14.
 */
@Setter
@Getter
public class CinemaWithSession extends Cinema {
    private String stime;
}
