package com.lhh.vista.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2017/1/5.
 */
@Setter
@Getter
public class RotateItem {
    private String id;
    private String name;

    public RotateItem() {
    }

    public RotateItem(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
