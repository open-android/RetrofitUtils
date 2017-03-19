package com.itheima.retrofitutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.itheima.retrofitutils.helper.ProgressRequestBody;
import com.itheima.retrofitutils.helper.ProgressResponseBody;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.itheima.retrofitutils.listener.UploadListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by lyl on 2016/10/3.
 */

public final class HttpHelper {
    private static WeakReference<HttpHelper> sInstance;
    private volatile Retrofit mRetrofit;

    private static String sBaseUrl;

    private File mCacheFile;
    //默认支持http缓存
    private static boolean mIsCache = true;

    public static void setBaseUrl(String baseUrl) {
        sBaseUrl = baseUrl;
    }

    /**
     * 设置是否缓存http请求数据
     *
     * @param isCache
     */
    public static void setHttpCache(boolean isCache) {
        mIsCache = isCache;
    }


    public static String getBaseUrl() {
        return sBaseUrl;
    }

    private HttpHelper() {
        //缓存路径
        mCacheFile = new File(ItheimaHttp.getContext().getCacheDir().getAbsolutePath() + File.separator + "retrofit2_http_cache");
        //判断缓存路径是否存在
        if (!mCacheFile.exists() && !mCacheFile.isDirectory()) {
            mCacheFile.mkdir();
        }

        OkHttpClient okHttpClient = createOkhttpAndCache();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(sBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(StringConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    /**
     * 创建okhttp & 添加缓存
     *
     * @return
     */
    private OkHttpClient createOkhttpAndCache() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (mIsCache) {
            //http缓存大小10M
            Cache cache = new Cache(mCacheFile, 1024 * 1024 * 10);
            //添加网络过滤 & 实现网络数据缓存
            Interceptor interceptor = createHttpInterceptor();
            builder.addNetworkInterceptor(interceptor);
            builder.addInterceptor(interceptor);
            //给okhttp添加缓存
            builder.cache(cache);
        }
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    public static HttpHelper getInstance() {
        if (sInstance == null || sInstance.get() == null) {
            synchronized (HttpHelper.class) {
                if (sInstance == null || sInstance.get() == null) {
                    sInstance = new WeakReference<HttpHelper>(new HttpHelper());
                }
            }
        }
        return sInstance.get();
    }


    public static <T> Call getAsync(String apiUrl, @HeaderMap Map<String, Object> headers, Map<String, Object> paramMap, final HttpResponseListener<T> httpResponseListener) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        if (headers == null) {
            headers = new HashMap<>();
        }
        HttpService httpService = getInstance().mRetrofit.create(HttpService.class);
        Call<ResponseBody> call = httpService.get(apiUrl, headers, paramMap);
        parseNetData(call, httpResponseListener);
        return call;
    }

    public static <T> Call postAsync(String apiUrl, @HeaderMap Map<String, Object> headers, Map<String, Object> paramMap, HttpResponseListener<T> httpResponseListener) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        if (headers == null) {
            headers = new HashMap<>();
        }
        HttpService httpService = getInstance().mRetrofit.create(HttpService.class);
        Call<ResponseBody> call = httpService.post(apiUrl, headers, paramMap);
        parseNetData(call, httpResponseListener);
        return call;
    }

    public static okhttp3.Call upload(Request request, final UploadListener uploadListener) {
        if (request.getUploadFiles() == null || request.getUploadFiles().size() == 0) {
            /*new FileNotFoundException("file does not exist(文件不存在)").printStackTrace();*/
            new FileNotFoundException("please add upload file").printStackTrace();
        }
        //000000000000000000000000000000


        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    //拦截
//                    Response originalResponse = chain.proceed(chain.request());
                    okhttp3.Response proceed = chain.proceed(chain.request());
                    //包装响应体并返回
                    return proceed.newBuilder()
                            .body( new ProgressResponseBody(proceed.body(), uploadListener))
                            .build();
                }
            }).build();
            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            RequestBody requestBody = null;
            //第一个参数是服务获取文件的key,第二个参数文件的名称,第三个参数上传到服务器文件以流的形式
            if (request.getUploadFiles() != null) {
                for (String key : request.getUploadFiles().keySet()) {
                    multipartBodyBuilder.addFormDataPart(key
                            , request.getUploadFiles().get(key).getName()
                            , RequestBody.create(request.getMediaType(), request.getUploadFiles().get(key)));
                }
            }

            if (request.getParamsMap() != null) {
                for (String key : request.getParamsMap().keySet()) {
                    multipartBodyBuilder.addFormDataPart(key, request.getParamsMap().get(key).toString());
                }
            }

            requestBody = multipartBodyBuilder.build();
            ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody, uploadListener);
            okhttp3.Request.Builder methodBuilder = new okhttp3.Request.Builder().method(request.getRequestMethod().getValue(), progressRequestBody);
            if (request.getHeaderMap() != null) {
                for (String key : request.getHeaderMap().keySet()) {
                    methodBuilder.addHeader(key, request.getHeaderMap().get(key).toString());
                }
            }

            okhttp3.Request okHttpRequest = methodBuilder.url(request.getApiUlr()).build();
            okhttp3.Call call = okHttpClient.newCall(okHttpRequest);
            call.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    uploadListener.onFailure(call, e);
                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    uploadListener.onResponse(call, response);
                }
            });
            return call;
        } catch (Throwable e) {
            L.e(e);
        }

        //000000000000000000000000000000

        /*Map<String, RequestBody> requestBodyMap = new HashMap<>();
        RequestBody requestBody = RequestBody.create(request.getMediaType(), request.getUploadFiles().get(0));
        requestBodyMap.put("file[]\"; filename=\"" + request.getUploadFiles().get(0).getName(), requestBody);

        String httpUrl = request.getApiUlr().trim();
        String tempUrl = httpUrl.substring(0, httpUrl.length() - 1);
        String baseUrl = tempUrl.substring(0, tempUrl.lastIndexOf(File.separator) + 1);
        if (L.isDebug) {
            L.i("httpUrl:" + httpUrl);
            L.i("tempUrl:" + tempUrl);
            L.i("baseUrl:" + baseUrl);
            L.i("apiUrl:" + httpUrl.substring(baseUrl.length()));
        }
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(new ChunkingConverterFactory(requestBody, uploadListener))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
        HttpService service = retrofit.create(HttpService.class);
        Call<ResponseBody> model = null;


        if (RequestMethod.GET.equals(request.getRequestMethod())) {
            model = service.getUpload(
                    httpUrl.substring(baseUrl.length())
                    , headers
                    , params
                    , requestBodyMap);
        } else {
            model = service.postUpload(
                    httpUrl.substring(baseUrl.length())
                    , headers
                    , params
                    , requestBodyMap);
        }


        model.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                uploadListener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                uploadListener.onFailure(call, t);
            }
        });*/
        return null;
    }


    private static <T> void parseNetData(Call<ResponseBody> call, final HttpResponseListener<T> httpResponseListener) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    if (L.isDebug) {
                        L.i("response data:" + json);
                    }
                    if (!String.class.equals(httpResponseListener.getType())) {
                        Gson gson = new Gson();
                        T t = gson.fromJson(json, httpResponseListener.getType());
                        httpResponseListener.onResponse(t, response.headers());
                    } else {
                        httpResponseListener.onResponse((T) json, response.headers());
                    }
                } catch (Exception e) {
                    if (L.isDebug) {
                        L.e("Http Exception:", e);
                    }
                    httpResponseListener.onFailure(call, e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                httpResponseListener.onFailure(call, t);
            }
        });
    }

    /**
     * 创建网络过滤对象，添加缓存
     *
     * @return
     */
    private Interceptor createHttpInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request();

                boolean isNetwork = isNetworkConnected(ItheimaHttp.getContext());

                //没有网络情况下，仅仅使用缓存（CacheControl.FORCE_CACHE;）
                if (!isNetwork) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                okhttp3.Response response = chain.proceed(request);
                if (isNetwork) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    public static interface HttpService<T> {
        @GET
        Call<ResponseBody> get(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> param);

        @FormUrlEncoded
        @POST
        Call<ResponseBody> post(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, Object> param);

        @Multipart
        @POST
        Call<String> postUpload(@Url String url, @HeaderMap Map<String, Object> headers, @QueryMap Map<String, Object> paramsField/*, @Part("filedes") String des*/, @PartMap Map<String, RequestBody> params);

        @Multipart
        @GET
        Call<String> getUpload(@Url String url, @HeaderMap Map<String, Object> headers, @FieldMap Map<String, Object> paramsField,/*@Part("filedes") String des,*/ @PartMap Map<String, RequestBody> params);
    }
}
