package com.itheima.retrofitutils.listener;

import com.itheima.retrofitutils.L;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by lyl on 2016/11/11.
 */

public abstract class UploadListener {

    public abstract void onResponse(okhttp3.Call call, okhttp3.Response response);

    public abstract void onProgress(long progress, long total, boolean done);

    public void onFailure(okhttp3.Call call, Throwable t) {
        L.e(t);
    }

}
