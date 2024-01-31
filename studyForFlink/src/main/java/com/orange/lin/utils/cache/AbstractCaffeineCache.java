package com.orange.lin.utils.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import cn.hutool.core.date.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Date;
import java.util.concurrent.*;

@Slf4j
public  abstract class AbstractCaffeineCache<K,V> {

    private volatile  LoadingCache<K,V> cache;

    private static final int MAX_SIZE= 100000;

    private static final int HOUR_1 =1;

    private static final int HOUR_3 =3;

    private static final int HOUR_12 =12;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = Math.max(2,Math.min(CPU_COUNT-1,4));

    private static final int MAX_POOL_SIZE =CPU_COUNT*2+1 ;

    public LoadingCache<K,V> getCache() {
        //双重校验锁保证只有一个cache实例
        if(cache == null){
            synchronized (this){
                if(cache==null){
                    log.info(" 初始化缓存 >>>>>>>>> {}",DateUtil.formatDateTime(new Date()));

                    cache = Caffeine.newBuilder()
                            .expireAfterWrite(HOUR_3, TimeUnit.HOURS)
                            .refreshAfterWrite(HOUR_3,TimeUnit.HOURS)
                            .executor(new ThreadPoolExecutor(CORE_POOL_SIZE,
                                    MAX_POOL_SIZE,
                                    60*1000L,
                                    TimeUnit.MILLISECONDS,
                                    new LinkedBlockingDeque<>(),
                                    Executors.defaultThreadFactory(),
                                    new ThreadPoolExecutor.CallerRunsPolicy()))
                            .maximumSize(MAX_SIZE)
                            .build(new CacheLoader<K, V>() {
                                @Override
                                public @Nullable V load(@NonNull K key) throws Exception {
                                    return  fetchData(key);
                                }
                            });
                }

            }
        }
        return  cache;
    }


    /**
     *  根据key从数据库或其他数据源中获取value，并保存到缓存
     * @param key
     * @return
     */
    protected abstract V fetchData(K key);

}
