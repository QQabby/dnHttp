package com.example.keep.dnhttp;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * JsonHttpRequest
 */
public class JsonHttpRequest implements IHttpRequest {


    private String url;
    private byte[] data;
    private CallbackListenr listenr;
    private HttpURLConnection urlConnection;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setListener(CallbackListenr listener) {
        this.listenr = listener;
    }

    /**
     * 真正网络请求的操作
     */
    @Override
    public void execute() {

        URL urll = null;
        try{
            urll = new URL(url);
            urlConnection = (HttpURLConnection) urll.openConnection();
            urlConnection.setConnectTimeout(6000);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(3000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json;charset-UTF-8");
            urlConnection.connect();

            OutputStream out = urlConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out);
            bos.write(data);
            bos.flush();
            out.close();
            bos.close();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in = urlConnection.getInputStream();
                listenr.onSuccess(in);

                //这里
            }else{
                throw new RuntimeException("请求失败");
            }

        }catch (Exception e){
            throw new RuntimeException("请求失败");
        }finally {
            //关闭链接
            urlConnection.disconnect();
        }

    }
}
