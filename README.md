#RetrofitUtils

Retrofit封装框架，内部使用gson解析json数据

开始
===
在project的build.gradle添加如下代码(如下图)
```xml
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
```xml
compile 'com.github.open-android:RetrofitUtils:0.1.9'
```

###需要的权限
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

###初始化
retrofitUtils初始化需要二个参数Context、baseUrl，最好在Application的onCreate()中初始化，记得在manifest.xml中注册Application。
```java
ItheimaHttp.init(this, baseUrl);//baseUrl格式："http://xxxxxx/xxxxx/"
```

###get/Post Bean类型异步请求（内部使用Gson解析json数据）
```java
//ItheimaHttp.newPostRequest(apiUrl)
Request request = ItheimaHttp.newGetRequest(apiUrl);//apiUrl格式："xxx/xxxxx"
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
@param httpResponseListener 回调监听
@param <T> Bean
@return Call可以取消网络请求
```
###添加请求参数
```java
request.putParams(key,value)
.putParams(key,value)
.putParams(key,value);


Map<String,Object> map = new HashMap<>();
map.put(key,value);
request.putParamsMap(map);
```
###添加请求头
```java
//添加请求头
request.putHeader(key,value)
.putHeader(key,value);
```

###get/post String类型异步请求
```java
Call call = ItheimaHttp.getAsync(apiUrl, new HttpResponseListener<String>);

Call call = ItheimaHttp.postAsync(apiUrl, new HttpResponseListener<String>() {
    @Override
    public void onResponse(String s) {
        ........
    }
});
```

###get/post Bean类型异步请求,内部使用Gson解析json数据
```java
Call call = ItheimaHttp.getAsync(apiUrl, new HttpResponseListener<Bean>);

Call call = ItheimaHttp.postAsync(apiUrl, new HttpResponseListener<Bean>() {
    @Override
    public void onResponse(Bean bean) {
        ........
    }
});
```

###文件上传
```java
Request request = ItheimaHttp.newUploadRequest("http://xxx/xxx/xxx", RequestMethod.POST);
request.putUploadFile(uploadFile)
        .putMediaType(MediaType.parse("multipart/form-data"));
ItheimaHttp.upload(request, new UploadListener() {
    @Override
    public void onResponse(Call call, Response response) {
        //上传成功回调
    }
    
    @Override
    public void onProgress(long progress, long total, boolean done) {
        //上传进度回调progress:上传进度，total:文件长度， done:上传是否完成
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        //上传失败
    }
});
```

###取消网络请求
```java
call.cancel();
```

###是否需要查看日志
```java
ItheimaHttp.setDebug(true);
```

###交流群
```
334700525
```

[回到顶部](#readme)
