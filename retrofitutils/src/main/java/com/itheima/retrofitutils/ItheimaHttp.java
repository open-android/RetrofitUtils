package com.itheima.retrofitutils;

import android.content.Context;

import retrofit2.Call;

/**
 * Created by lyl on 2016/10/30.
 */

public class ItheimaHttp {

    public static final void init(Context context, String httpBaseUrl) {
        HttpHelper.setBaseUrl(httpBaseUrl);
    }

    public static <T> Call getAsync(String apiUrl, final HttpResponseListener<T> httpResponseListener) {
        return HttpHelper.getAsync(apiUrl, null, null, httpResponseListener);
    }

    public static <T> Call postAsync(String apiUrl, HttpResponseListener<T> httpResponseListener) {
        return HttpHelper.postAsync(apiUrl, null, null, httpResponseListener);
    }

    /**
     * 发送http网络请求
     *
     * @param request
     * @param httpResponseListener
     * @param <T>
     * @return
     */
    public static <T> Call send(Request request, HttpResponseListener<T> httpResponseListener) {
        if (RequestMethod.GET.equals(request.getRequestMethod())) {
            return HttpHelper.getAsync(request.getApiUlr()
                    , request.getHeaderMap()
                    , request.getParamsMap()
                    , httpResponseListener);
        } else {
            return HttpHelper.postAsync(request.getApiUlr()
                    , request.getHeaderMap()
                    , request.getParamsMap()
                    , httpResponseListener);
        }
    }

    public static <T> Call upload(Request request, HttpResponseListener<T> httpResponseListener) {
        return HttpHelper.upload(request, httpResponseListener);
    }
}
