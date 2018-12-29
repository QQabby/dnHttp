package com.example.keep.dnhttp;

import java.io.InputStream;

public interface CallbackListenr {
    //回调接口
    void onSuccess(InputStream inputStream);

    void onFail();
}
