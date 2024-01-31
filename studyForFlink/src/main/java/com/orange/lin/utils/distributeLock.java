package com.orange.lin.utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;


public class distributeLock {

    RetryPolicy retryPolicy= new ExponentialBackoffRetry(1000,3);
    private CuratorFramework client;
    private String zkAddress;
    private String nodeAddress;


    public distributeLock(String zkAddress,String nodeAddress) {
        this.zkAddress = zkAddress;
        this.nodeAddress=nodeAddress;
    }


    public InterProcessMutex createLock() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkAddress,retryPolicy);
        InterProcessMutex mutex = new InterProcessMutex(client, nodeAddress);
        return mutex;
    }

    public void stopClient(){
        client.close();
    }



    public static void main(String[] args) {

        distributeLock distributeLock = new distributeLock("aaa", "/curator/lock");
        InterProcessMutex lock = distributeLock.createLock();

        try{
            lock.acquire();
            System.out.println("处理逻辑");
            lock.release();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {

            distributeLock.stopClient();
        }





    }
}
