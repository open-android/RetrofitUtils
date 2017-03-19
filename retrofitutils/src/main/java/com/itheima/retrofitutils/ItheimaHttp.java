package com.itheima.retrofitutils;

import android.app.Application;
import android.content.Context;

import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.itheima.retrofitutils.listener.UploadListener;

import retrofit2.Call;

/**
 * Created by lyl on 2016/10/30.
 */

public class ItheimaHttp {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setHttpCache(boolean cache){
        HttpHelper.setHttpCache(cache);
    }

    public static final void init(Context context, String httpBaseUrl) {
        mContext = context.getApplicationContext();
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

    public static <T> okhttp3.Call upload(Request request, UploadListener uploadListener) {
        return HttpHelper.upload(request, uploadListener);
    }

    /**
     * @param apiUlr 格式：xxxx/xxxxx
     * @return
     */
    public static Request newRequest(String apiUlr, RequestMethod method) {
        return new Request(apiUlr, method);
    }

    /**
     * @param apiUlr 格式：xxxx/xxxxx
     * @return
     */
    public static Request newPostRequest(String apiUlr) {
        return new Request(apiUlr, RequestMethod.POST);
    }

    /**
     * @param uploadFileUrl 格式：http://xxxx/xxxxx
     * @return
     */
    public static Request newUploadRequest(String uploadFileUrl, RequestMethod method) {
        return new Request(uploadFileUrl, method);
    }

    /**
     * 默认是GET方式
     *
     * @param apiUlr 格式：xxxx/xxxxx
     * @return
     */
    public static Request newGetRequest(String apiUlr) {
        return new Request(apiUlr, RequestMethod.GET);
    }

    /**
     * 是否显示日志，默认不现实 true:显示
     *
     * @param isDebug
     */
    public static void setDebug(boolean isDebug) {
        L.isDebug = isDebug;
    }
}
