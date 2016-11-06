#RetrofitUtils

Retrofit封装框架，内部使用gson解析json数据

开始
===
在build.gradle添加依赖
```
compile 'com.squareup.retrofit2:retrofit:2.1.0'
compile 'com.squareup.retrofit2:converter-gson:2.1.0'
```

######需要的权限
```
<uses-permission android:name="android.permission.INTERNET" />
```

######初始化
retrofitUtils初始化需要二个参数Context、baseUrl，最好在Application的onCreate()中初始化，记得在manifest.xml中注册Application。
```
RetrofitUtils.init(this, baseUrl);

baseUrl格式："http://xxxxxx/xxxxx/"
```

######get/post String类型异步请求
```
RetrofitUtils.getAsync.......同下.....

Call call = RetrofitUtils.postAsync(apiUrl, paramMap, new HttpResponseListener<String>() {
    @Override
    public void onResponse(String s) {
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
@param paramMap 请求参数（可以传null）
@param httpResponseListener 回调监听
@param <T> Bean
@return Call可以取消网络请求
```

######get/post Bean类型异步请求,内部使用Gson解析json数据
```
RetrofitUtils.getAsync.......同下.....

Call call = RetrofitUtils.postAsync(apiUrl, paramMap, new HttpResponseListener<Bean>() {
    @Override
    public void onResponse(Bean bean) {
        ........
    }
});
```

######get/post异步请求添请求加头
```
RetrofitUtils.getAsync.......同下.....

Call call = RetrofitUtils.postAsync(apiUrl,headerMap, paramMap, new HttpResponseListener<Bean>() {
    @Override
    public void onResponse(Bean bean) {
        ........
    }
});
```

######取消网络请求
```
call.cancel();
```
