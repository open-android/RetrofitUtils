#RetrofitUtils

Retrofit封装框架，内部使用gson解析json数据

开始
===
在project的build.gradle添加如下代码(如下图)
```
allprojects {
    repositories {
        ...
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```
![image](jitpack.png)

在build.gradle添加依赖
```
compile 'com.github.itcastsh:retrofitUtils:0.1.2'
compile 'com.squareup.retrofit2:retrofit:2.1.0'
compile 'com.squareup.retrofit2:converter-gson:2.1.0'
```

######需要的权限
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

######初始化
retrofitUtils初始化需要二个参数Context、baseUrl，最好在Application的onCreate()中初始化，记得在manifest.xml中注册Application。
```
ItheimaHttp.init(this, baseUrl);//baseUrl格式："http://xxxxxx/xxxxx/"
```

######get/Post Bean类型异步请求
```
//Request.newPostRequest(apiUrl)
Request request = Request.newGetRequest(apiUrl);
//添加请求头
request.putHeader(key,value);
//添加请求参数
request.putParams(key,value);
//发送异步网络请求,内部使用Gson解析json数据
Call call = ItheimaHttp.send(request, new HttpResponseListener<Bean>() {
    @Override
    public void onResponse(Bean bean) {
        ........
    }
     /**
     * 可以不重写失败回调
     * @param call
     * @param e
     */
    @Override
    public void onFailure(Call<ResponseBody> call, Throwable e) {
        ......
    }
});
@param apiUrl 请求地址("xxx/xxxxx")
@param httpResponseListener 回调监听
@param <T> Bean
@return Call可以取消网络请求
```

######get/post String类型异步请求
```
Call call = ItheimaHttp.getAsync(apiUrl, httpResponseListener<String>);

Call call = ItheimaHttp.postAsync(apiUrl, new HttpResponseListener<String>() {
    @Override
    public void onResponse(String s) {
        ........
    }
});
```

######get/post Bean类型异步请求,内部使用Gson解析json数据
```
Call call = ItheimaHttp.getAsync(apiUrl, httpResponseListener<Bean>);

Call call = ItheimaHttp.postAsync(apiUrl, new HttpResponseListener<Bean>() {
    @Override
    public void onResponse(Bean bean) {
        ........
    }
});
```

######文件上传
```
Request request = Request.newUploadRequest("http://xxx/xxx");
request.putUploadFile(file);
ItheimaHttp.upload(request,httpResponseListener)
```

######取消网络请求
```
call.cancel();
```
