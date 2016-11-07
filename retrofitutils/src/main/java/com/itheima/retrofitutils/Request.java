package com.itheima.retrofitutils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lyl on 2016/11/6.
 */

public class Request {
    private Map<String, Object> mHeaderMap;
    private Map<String, Object> mParamsMap;
    private List<File> mUploadFiles;
    private String mApiUlr;
    private RequestMethod mRequestMethod;

    private Request(String apiUlr, RequestMethod method) {
        this.mApiUlr = apiUlr;
        this.mRequestMethod = method;
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
     * @param uploadFleUrl 格式：http://xxxx/xxxxx
     * @return
     */
    public static Request newUploadRequest(String uploadFleUrl) {
        return new Request(uploadFleUrl, RequestMethod.GET);
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


    public Map<String, Object> putHeader(String key, Object value) {
        if (mHeaderMap == null) {
            mHeaderMap = new HashMap<>();
        }
        mHeaderMap.put(key, value);
        return mHeaderMap;
    }

    public void putHeaderMap(Map<String, Object> headerMap) {
        if (mHeaderMap != null) {
            mHeaderMap.putAll(headerMap);
        } else {
            mHeaderMap = headerMap;
        }
    }

    public Map<String, Object> putParams(String key, Object value) {
        if (mParamsMap == null) {
            mParamsMap = new HashMap<>();
        }
        mParamsMap.put(key, value);
        return mParamsMap;
    }

    public void putParamsMap(Map<String, Object> paramMap) {
        if (mParamsMap != null) {
            mParamsMap.putAll(paramMap);
        } else {
            mParamsMap = paramMap;
        }
    }

    public List<File> putUploadFile(File uploadFile) {
        if (mUploadFiles == null) {
            mUploadFiles = new ArrayList<>(3);
        }
        mUploadFiles.add(uploadFile);
        return mUploadFiles;
    }

    public List<File> getUploadFiles() {
        return mUploadFiles;
    }

    public Map<String, Object> getHeaderMap() {
        return mHeaderMap;
    }

    public Map<String, Object> getParamsMap() {
        return mParamsMap;
    }

    public String getApiUlr() {
        return mApiUlr;
    }

    public RequestMethod getRequestMethod() {
        return mRequestMethod;
    }
}
