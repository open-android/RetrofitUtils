package com.itheima.retrofitutils;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by lyl on 2016/10/3.
 */

public final class HttpHelper {
    private volatile static WeakReference<HttpHelper> sInstance;
    private final Retrofit mRetrofit;

    private static String sBaseUrl;

    public static void setBaseUrl(String baseUrl) {
        sBaseUrl = baseUrl;
    }

    public static String getBaseUrl() {
        return sBaseUrl;
    }

    private HttpHelper() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(new OkHttpClient() {
            @Override
            public List<Interceptor> interceptors() {
                return super.interceptors();
            }
        });
        if (TextUtils.isEmpty(getBaseUrl())) {
            throw new NullPointerException("init(Context,httpBaseUrl)：httpBaseUrl is not null");
        }
        mRetrofit = builder.addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getBaseUrl())
                .build();
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

    public static <T> Call upload(Request request, HttpResponseListener<T> httpResponseListener) {
        if (request.getUploadFiles() == null || !request.getUploadFiles().get(0).exists()) {
            new FileNotFoundException("file does not exist(文件不存在)").printStackTrace();
        }
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        RequestBody requestBody = RequestBody.create(request.getMediaType(), request.getUploadFiles().get(0));
        requestBodyMap.put("file[]\"; filename=\"" + request.getUploadFiles().get(0).getName(), requestBody);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(new ChunkingConverterFactory(requestBody, httpResponseListener))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(request.getApiUlr())
                .build();
        HttpService service = retrofit.create(HttpService.class);

        Call<ResponseBody> model = service.upload(
                ""
                , "uploadDes"
                , requestBodyMap);
        parseNetData(model, httpResponseListener);
        return model;
    }


    private static <T> void parseNetData(Call<ResponseBody> call, final HttpResponseListener<T> httpResponseListener) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    if (!String.class.equals(httpResponseListener.getType())) {
                        Gson gson = new Gson();
                        T t = gson.fromJson(json, httpResponseListener.getType());
                        httpResponseListener.onResponse(t);
                    } else {
                        httpResponseListener.onResponse((T) json);
                    }
                } catch (Exception e) {
                    L.e("HttpLelper onResponse:", e);
                    httpResponseListener.onFailure(call, e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                httpResponseListener.onFailure(call, t);
            }
        });
    }


    public static interface HttpService<T> {
        @GET
        public Call<ResponseBody> get(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> param);

        @FormUrlEncoded
        @POST
        public Call<ResponseBody> post(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, Object> param);

        @Multipart
        @POST
        Call<String> upload(@Url String url, @Part("filedes") String des, @PartMap Map<String, RequestBody> params);
    }
}
