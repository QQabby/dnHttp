package com.example.keep.dnhttp;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class HttpTask<T> implements Runnable,Delayed {

    private IHttpRequest httpRequest;
    public <T> HttpTask(T requestData, String url, IHttpRequest httpRequest, CallbackListenr listenr) {

        this.httpRequest = httpRequest;
        httpRequest.setUrl(url);
        httpRequest.setListener(listenr);
        if(requestData != null){
            String content = JSON.toJSONString(requestData);

            try {
                httpRequest.setData(content.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try{
            httpRequest.execute();
        }catch (Exception e){
            ThreadPoolManager.getInstance().addDelayTask(this);
        }
    }

    private long delayTime;
    private int retryCount ;

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = System.currentTimeMillis() + 3000;
    }

    //延时多久
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.delayTime - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
