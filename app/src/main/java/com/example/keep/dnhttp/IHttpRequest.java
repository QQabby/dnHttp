package com.example.keep.dnhttp;

public interface IHttpRequest {
    //httpRequest 请求
    void setUrl(String url);

    void setData(byte[] data);

    void setListener(CallbackListenr listener);

    //具体的业务访问
    void execute();
}
