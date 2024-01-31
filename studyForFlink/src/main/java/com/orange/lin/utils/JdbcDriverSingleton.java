package com.orange.lin.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcDriverSingleton {

    private volatile static JdbcDriverSingleton jdbcDriverSingleton=null;

    private JdbcDriverSingleton(){

    }

    /**
     *
     * @return
     * @throws Exception
     */
    public static JdbcDriverSingleton getInstance() throws  Exception{
        if(jdbcDriverSingleton==null){
            synchronized (JdbcDriverSingleton.class){
                if(jdbcDriverSingleton == null){
                    log.info("load Driver");
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
                    jdbcDriverSingleton = new JdbcDriverSingleton();
                }
            }
        }

        return jdbcDriverSingleton;
    }
}
