package com.itheima.retrofitutils.listener;

import com.itheima.retrofitutils.L;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by lyl on 2016/11/11.
 */

public abstract class UploadListener {

    public abstract void onResponse(Call<ResponseBody> call, Response<ResponseBody> response);

    public abstract void onProgress(long progress, long total, boolean done);

    public void onFailure(Call<ResponseBody> call, Throwable t) {
        L.e(t);
    }

}
