package com.example.keep.dnhttp;

/**
 * 工具类访问
 */
public class DnHttp {

    public static<T,M>  void sendHttpRequest(T requestData,String url,Class<M> respose,
                                        IJsonDataListener iJsonDataListener){
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallbackListenr listenr = new JsonCallbackListener<>(respose,iJsonDataListener);


        HttpTask httpTask = new HttpTask(requestData,url,httpRequest,listenr);


        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
