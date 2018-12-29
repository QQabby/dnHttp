package com.example.keep.dnhttp;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    //1.创建请求队列，用来存储请求
    private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    //创建重试任务队列
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();

    public void addDelayTask(HttpTask ht){
        if(ht != null){
            ht.setDelayTime(3000);
            mDelayQueue.offer(ht);
        }
    }

    //2.任务添加到队列中
    public void addTask(Runnable runnable){

        try {
            if(null != runnable){
                queue.put(runnable);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Runnable mDelayRunnable = new Runnable() {
        @Override
        public void run() {
            while(true){
                HttpTask ht = null;
                try {
                    ht = mDelayQueue.take();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(ht.getRetryCount() < 3){
                    mThreadPoolExecutor.execute(ht);
                    ht.setRetryCount(ht.getRetryCount()+1);
                    Log.i("xx","重试机制：：："+ht.getRetryCount());
                }else{
                    Log.i("xx","超过最大限制");
                }
            }
        }
    };

    //3.创建线程池
    private ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolManager(){
        mThreadPoolExecutor = new ThreadPoolExecutor(3, 10, 15,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //失败被拒绝
                addTask(r);//可以重新添加到队列中去
            }
        });

        //mThreadPoolExecutor 请coreThread 不停的做任务
        mThreadPoolExecutor.execute(coreThread);
        mThreadPoolExecutor.execute(mDelayRunnable);
    }
    //4.创建核心线程，不停的从队列中获取任务，然后交给线程池处理
    public Runnable coreThread = new Runnable() {
        @Override
        public void run() {
            Runnable runnable = null;
            while(true){
                try {

                    runnable = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExecutor.execute(runnable);
            }
        }
    };

    private static ThreadPoolManager mInstance = new ThreadPoolManager();
    public static ThreadPoolManager getInstance(){
        return mInstance;
    }



























}
