package com.example.keep.dnhttp;

import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 返回数据
 */
public class JsonCallbackListener<T> implements CallbackListenr {

    private Class<T> responseClass;
    private IJsonDataListener iJsonDataListener;
    private Handler handler = new Handler();

    public JsonCallbackListener(Class<T> responseClass,IJsonDataListener iJsonDataListener) {
        this.responseClass = responseClass;
        this.iJsonDataListener = iJsonDataListener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {

        //将流转换{"reason":"查询成功","result":[{"id":"82","province_id":"2","city_name":"澳门"}],"error_code":0}/n
        String response = getContent(inputStream).replace("/n","");
        //转换成相对应类型
        final T clazz =JSON.parseObject(response,responseClass);

        handler.post(new Runnable() {
            @Override
            public void run() {
                iJsonDataListener.onSuccess(clazz);
            }
        });
    }

    @Override
    public void onFail() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                iJsonDataListener.onFail();
            }
        });
    }

    private String getContent(InputStream inputStream) {
        //将流转换为Stirng

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder sb = new StringBuilder();

        String line = null;
        try{
            while ((line = reader.readLine()) != null){
                sb.append(line+ "/n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
