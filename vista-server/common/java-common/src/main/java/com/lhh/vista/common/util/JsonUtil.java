package com.lhh.vista.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by soap on 2016/10/25.
 */
public class JsonUtil extends ObjectMapper{
    public JsonUtil() {
        super();
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
