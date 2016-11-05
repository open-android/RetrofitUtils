package com.itheima.retrofitutils;

import android.content.Context;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.HeaderMap;

/**
 * Created by lyl on 2016/10/30.
 */

public class RetrofitUtils {

    public static final void init(Context context, String httpBaseUrl) {
        HttpHelper.setBaseUrl(httpBaseUrl);
    }

    public static <T> Call getAsync(String apiUrl, final HttpResponseListener<T> httpResponseListener) {
        return HttpHelper.getAsync(apiUrl, null, null, httpResponseListener);
    }

    public static <T> Call postAsync(String apiUrl, HttpResponseListener<T> httpResponseListener) {
        return HttpHelper.postAsync(apiUrl, null, null, httpResponseListener);
    }

    public static <T> Call getAsync(String apiUrl, Map<String, Object> paramMap, final HttpResponseListener<T> httpResponseListener) {
        return HttpHelper.getAsync(apiUrl, null, paramMap, httpResponseListener);
    }



    public static <T> Call postAsync(String apiUrl, Map<String, Object> paramMap, HttpResponseListener<T> httpResponseListener) {
        return HttpHelper.postAsync(apiUrl, null, paramMap, httpResponseListener);
    }


    public static <T> Call getAsync(String apiUrl, @HeaderMap Map<String, String> headerMap, Map<String, Object> paramMap, final HttpResponseListener<T> httpResponseListener) {
        return HttpHelper.getAsync(apiUrl, headerMap, paramMap, httpResponseListener);
    }


    public static <T> Call postAsync(String apiUrl, @HeaderMap Map<String, String> headerMap, Map<String, Object> paramMap, HttpResponseListener<T> httpResponseListener) {
        return HttpHelper.postAsync(apiUrl, headerMap, paramMap, httpResponseListener);
    }
}
