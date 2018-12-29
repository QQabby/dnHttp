package com.example.keep.dnhttp;

//传递数据
public interface IJsonDataListener<T> {

    void onSuccess(T clazz);

    void onFail();

}
