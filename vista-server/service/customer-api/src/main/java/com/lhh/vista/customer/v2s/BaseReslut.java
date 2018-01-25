package com.lhh.vista.customer.v2s;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liu on 2016/12/1.
 */
@Setter
@Getter
@ToString
public class BaseReslut<T> {
    private T value;
}
