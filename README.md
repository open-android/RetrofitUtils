* 欢迎关注微信公众号,长期推荐技术文章和技术视频

* 微信公众号名称：Android干货程序员

![](http://upload-images.jianshu.io/upload_images/4037105-8f737b5104dd0b5d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

PS：如果觉得文章太长，你也可观看该课程的[视频教程](https://v.qq.com/x/page/w0355urm511.html)，亲，里面还有高清，无码的福利喔
>写在前面: 
Retrofit与okhttp共同出自于Square公司，是目前市场上使用最多的联网框架,retrofit是对okhttp做了一层封装,不过封装之后的retrofit上手还是极其复杂,为了解决使用难度问题,本文推荐使用github开源项目RetrofitUtils.

* RetrofitUtils开源项目地址：https://github.com/open-android/RetrofitUtils 

* 定义我们请求的Api，我们假设是这样的:
 
* http://www.oschina.net/action/apiv2/banner?catalog=1 

*   返回服务器的数据

---

    {  
    "code": 1,  
    "message": "success",  
    "result": {  
    "items": [  
        {  
            "detail": "",  
            "href": "http://www.oschina.net/news/79650/2017-top-programming-languages",  
            "id": 79650,  
            "img": "https://static.oschina.net/uploads/cooperation/75410/google-beta-natural-language-api_048424e4-a8c3-41e8-91ef-aa009e3fc559.jpg",  
            "name": "2017 年热门编程语言排行榜",  
            "pubDate": "2016-12-06 11:51:24",  
            "type": 6  
            },  
        {  
            "detail": "",  
            "href": "https://www.oschina.net/question/2720166_2210842",  
            "id": 2210842,  
            "img": "https://static.oschina.net/uploads/cooperation/75323/ubuntu-forum-black-sql_eb9137ea-efb2-49aa-99fd-025a221dcfe7.jpg",  
            "name": "高手问答 | MySQL 开发和运维规范",  
            "pubDate": "2016-12-06 15:48:10",  
            "type": 2  
        },  
        {  
            "detail": "",  
            "href": "http://www.oschina.net/news/79757/tiobe-12",  
            "id": 79757,  
            "img": "https://static.oschina.net/uploads/cooperation/78083/chrome55-save-at-least-35-percent-memory_70ceba24-eb96-4710-99ec-c1cb5a26a3d6.jpg",  
            "name": "C 语言为何一蹶不振？",  
            "pubDate": "2016-12-08 15:18:20",  
            "type": 6  
        },  
        {  
            "detail": "",  
            "href": "https://www.oschina.net/news/79732/firebug-stop-develope-and-maintain",  
            "id": 79732,  
            "img": "https://static.oschina.net/uploads/cooperation/77929/top-income-programming-languages-2016_16e9be1b-2a6b-453f-bafa-442fd043024b.jpg",  
            "name": "Firebug 宣布不再维护，讲不出再见！",  
            "pubDate": "2016-12-08 10:56:47",  
            "type": 6  
        },  
        {  
            "detail": "",  
            "href": "http://www.oschina.net/news/79673/the-founder-of-cm-was-fired",  
            "id": 79673,  
            "img": "https://static.oschina.net/uploads/cooperation/78455/intellij-idea-2016-3-public-preview_63725513-45e4-4fb2-a0bf-c7940a7a87bc.jpg",  
            "name": "Cyanogen 之父被踢出局",  
            "pubDate": "2016-12-06 11:48:43",  
            "type": 6  
        }],  
        "nextPageToken": "61AF0C190D6BD629",  
        "prevPageToken": "3EA621243546C8A5",  
        "requestCount": 5,  
        "responseCount": 5,  
        "totalResults": 5  
    },  
      "time": "2016-12-13 10:56:41"}  

- 这样一个接口我看看使用RetrofitUtils该如何使用


![1.png](http://upload-images.jianshu.io/upload_images/4037105-2af135a770cc8393.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>大家看简单实用只需要new一个request然后发送一个send请求,就能返回服务器的数据,并且压根不需要解析拿到的直接就是各位需要的bean对象.那么我们来看看RetrofitUtils的使用步骤

 

>  **在project的build.gradle添加如下代码(如下图)**


![jitpack.png](http://upload-images.jianshu.io/upload_images/4037105-ff17197957c18533.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>**引入当前项目**

    compile 'com.github.open-android:RetrofitUtils:0.1.9'

>**在清单文件添加如下权限**

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

>**新建Application类并且在oncreate方法当中进行初始化： 
千万注意Application记得在manifest.xml中注册。**

  
      ItheimaHttp.init(this, Urls.getBaseUrl());

eg:请求地址:http://www.oschina.net/action/apiv2/banner?catalog=1 

那么baseUrl = http://www.oschina.net 
RetrofitUtils内部封装了如下使用方法

>看一个完整Get和Post请求是如何实现：异步请求（内部使用Gson解析json数据）直接返回实体bean对象

    

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
    @return Call可以取消网络请求</span>  

>添加请求参数

    request.putParams(key,value)  
    .putParams(key,value)  
    .putParams(key,value);  
    
    
    Map<String,Object> map = new HashMap<>();  
    map.put(key,value);  
    request.putParamsMap(map);  

>添加请求头

    request.putHeader(key,value)  
    .putHeader(key,value);

>如果不需要RetrofitUtils框架自动解析json,那么可以使用如下请求

    Call call = ItheimaHttp.getAsync(apiUrl, new HttpResponseListener<String>);  
    
    Call call = ItheimaHttp.postAsync(apiUrl, new HttpResponseListener<String>() {  
        @Override  
        public void onResponse(String s) {  
        ........  
        }  
    });

>如果需要RetrofitUtils框架自动解析json,直接获取javabean,那么可以使用如下请求

    Call call = ItheimaHttp.getAsync(apiUrl, new HttpResponseListener<Bean>);  
    
    Call call = ItheimaHttp.postAsync(apiUrl, new HttpResponseListener<Bean>() {  
        @Override  
        public void onResponse(Bean bean) {  
        ........  
        }  
    });

>文件上传
    
    Request request = ItheimaHttp.newUploadRequest("http://xxx/xxx/xxx", RequestMethod.POST);  
    request.putUploadFile(uploadFile).putMediaType(MediaType.parse("multipart/form-data"));  
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

>取消网络请求

    call.cancel();
