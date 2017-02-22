#RetrofitUtils

Retrofit封装框架，内部使用gson解析json数据


 PS：如果觉得文章太长，你也可观看该课程的[视频](https://www.boxuegu.com/web/html/video.html?courseId=172&sectionId=8a2c9bed5a3a4c7e015a3ad9a490030d&chapterId=8a2c9bed5a3a4c7e015a3ad9dfdf030e&vId=8a2c9bed5a3a4c7e015a3adaaaa9030f&videoId=4DC518DB11BC473E9C33DC5901307461)，亲，里面还有高清，无码的福利喔


* 爱生活,爱学习,更爱做代码的搬运工,分类查找更方便请下载黑马助手app


![黑马助手.png](http://upload-images.jianshu.io/upload_images/4037105-f777f1214328dcc4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

开始
===
在project的build.gradle添加如下代码(如下图)
```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```
![image](jitpack.png)
 
在build.gradle添加依赖
```groovy
compile 'com.github.open-android:RetrofitUtils:0.3.12'
```

###需要的权限
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

###初始化
retrofitUtils初始化需要二个参数Context、baseUrl，最好在Application的onCreate()中初始化，记得在manifest.xml中注册Application。
```java
ItheimaHttp.init(this, baseUrl);//baseUrl格式："http://xxxxxx/xxxxx/"
```

###设置是否缓存http响应数据（默认支持缓存）
```java
ItheimaHttp.setHttpCache(false);//false不缓存，true缓存
```

###get/Post Bean类型异步请求（内部使用Gson解析json数据）
```java
//ItheimaHttp.newPostRequest(apiUrl)
Request request = ItheimaHttp.newGetRequest(apiUrl);//apiUrl格式："xxx/xxxxx"
Call call = ItheimaHttp.send(request, new HttpResponseListener<Bean>() {
    @Override
    public void onResponse(Bean bean, Headers headers) {
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

//@param httpResponseListener 回调监听
//@param <T> Http响应数据泛型String或者Bean(使用String可以自己解析数据)
//@return Call可以取消网络请求
```


```java
Request request = ItheimaHttp.newGetRequest(apiUrl);//apiUrl格式："xxx/xxxxx"
Call call = ItheimaHttp.send(request, new HttpResponseListener<String>() {
    @Override
    public void onResponse(String string, Headers headers) {
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

//@param httpResponseListener 回调监听
//@param <T> Http响应数据泛型String或者Bean(使用String可以自己解析数据)
//@return Call可以取消网络请求
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
    public void onResponse(String s, Headers headers) {
        ........
    }
});
```

###get/post Bean类型异步请求,内部使用Gson解析json数据
```java
Call call = ItheimaHttp.getAsync(apiUrl, new HttpResponseListener<Bean>);

Call call = ItheimaHttp.postAsync(apiUrl, new HttpResponseListener<Bean>() {
    @Override
    public void onResponse(Bean bean, Headers headers) {
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

* 详细的使用方法在DEMO里面都演示啦,如果你觉得这个库还不错,请赏我一颗star吧~~~

* 欢迎关注微信公众号

![](http://upload-images.jianshu.io/upload_images/4037105-8f737b5104dd0b5d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


[回到顶部](#readme)
