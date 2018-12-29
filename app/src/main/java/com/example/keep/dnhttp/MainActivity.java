package com.example.keep.dnhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


   // private String url = "http://v.juhe.cn/historyWeather/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b";

    private String url = "http://xxxx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DnHttp.sendHttpRequest(new RequestBean("2","bb52107206585ab074f5e59a8c73875b"), url, ResponseBean.class, new IJsonDataListener<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean clazz) {

                Log.i("xx","response::"+clazz.getReason()+"===="+clazz.getError_code());
            }

            @Override
            public void onFail() {

                Log.i("xx","onFail.......");
            }
        });
    }
}
