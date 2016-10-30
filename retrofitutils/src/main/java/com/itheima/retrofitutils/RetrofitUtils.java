package com.itheima.retrofitutils;

import android.content.Context;

import java.util.Map;

import retrofit2.Call;

/**
 * Created by lyl on 2016/10/30.
 */

public class RetrofitUtils {

    public static final void init(Context context, String httpBaseUrl) {
        HttpHelper.setBaseUrl(httpBaseUrl);
    }

    /**
     *
     * @param apiUrl 请求地址
     * @param paramMap 请求参数（可以传null）
     * @param httpResponseListener 回调监听
     * @param <T> Bean
     * @return Call可以取消网络请求
     */
    public static <T> Call getAsync(String apiUrl, Map<String, Object> paramMap, final HttpResponseListener<T> httpResponseListener){
        return HttpHelper.getAsync(apiUrl,paramMap,httpResponseListener);
    }

    /**
     *
     * @param apiUrl 请求地址
     * @param paramMap 请求参数（可以传null）
     * @param httpResponseListener 回调监听
     * @param <T> Bean
     * @return Call
     */
    public static <T> Call postAsync(String apiUrl, Map<String, Object> paramMap, HttpResponseListener<T> httpResponseListener) {
        return HttpHelper.postAsync(apiUrl,paramMap,httpResponseListener);
    }
}
