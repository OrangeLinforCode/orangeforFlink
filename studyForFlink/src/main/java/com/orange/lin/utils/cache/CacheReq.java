package com.orange.lin.utils.cache;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class CacheReq implements Serializable {

    private String queryType;

    private Map<String,Object> queryParamMap;
}
