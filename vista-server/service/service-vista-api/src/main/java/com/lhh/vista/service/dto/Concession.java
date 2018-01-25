package com.lhh.vista.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2017/1/1.
 */
@Setter
@Getter
public class Concession {
    private String id;
    private String description;
    private Integer price;
    private Integer count;

    public Concession(String id, String description, Integer price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public Concession() {
    }
}
