package com.kenick.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MongoLogUtil {
    private static ExecutorService executorService = new ThreadPoolExecutor(0,50,
            60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());

    // 使用线程池，避免频繁创建线程，关闭线程，浪费资源
    public static void writeLog(String username,String pwd,String dbName,String msg){
        try {
            MongoWriteLogRunnable mongoWriteLogRunnable = new MongoWriteLogRunnable(username, pwd, dbName, msg);
            executorService.execute(mongoWriteLogRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 测试
    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String threadName = Thread.currentThread().getName()+"_"+System.currentTimeMillis();
                for(int i=0;i<10;i++){
                    MongoLogUtil.writeLog("kenick", "kenick.com","tutorial",threadName);
                }
            }
        },"writeLogThread1");

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String threadName = Thread.currentThread().getName()+"_"+System.currentTimeMillis();
                for(int i=0;i<10;i++){
                    MongoLogUtil.writeLog("kenick", "kenick.com", "tutorial", threadName);
                }
            }
        },"writeLogThread2");

        thread1.start();
        thread2.start();
    }
}
