package com.orange.lin.utils.cache;

import cn.hutool.db.Entity;

import java.util.List;

public class DefaultCaffeineManager {

    private static AbstractCaffeineCache<CacheReq, List<Entity>> cacheWrapper;

    public static boolean initCaffeineCache(){


        return false;
    }
}
