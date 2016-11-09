package com.itheima.retrofitutils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by lyl on 2016/11/6.
 */

public class Request {
    private Map<String, Object> mHeaderMap;
    private Map<String, Object> mParamsMap;
    private List<File> mUploadFiles;
    private String mApiUlr;
    private RequestMethod mRequestMethod;

    private MediaType mMediaType = MediaType.parse("application/otcet-stream");

    public Request(String apiUlr, RequestMethod method) {
        this.mApiUlr = apiUlr;
        this.mRequestMethod = method;
    }


    public Request putHeader(String key, Object value) {
        if (mHeaderMap == null) {
            mHeaderMap = new HashMap<>();
        }
        mHeaderMap.put(key, value);
        return this;
    }

    public void putHeaderMap(Map<String, Object> headerMap) {
        if (mHeaderMap != null) {
            mHeaderMap.putAll(headerMap);
        } else {
            mHeaderMap = headerMap;
        }
    }

    public Request putParams(String key, Object value) {
        if (mParamsMap == null) {
            mParamsMap = new HashMap<>();
        }
        mParamsMap.put(key, value);
        return this;
    }

    public void putParamsMap(Map<String, Object> paramMap) {
        if (mParamsMap != null) {
            mParamsMap.putAll(paramMap);
        } else {
            mParamsMap = paramMap;
        }
    }

    public Request putMediaType(MediaType mediaType) {
        mMediaType = mediaType;
        return this;
    }

    public MediaType getMediaType() {
        return mMediaType;
    }

    public Request putUploadFile(File uploadFile) {
        if (mUploadFiles == null) {
            mUploadFiles = new ArrayList<>(3);
        }
        mUploadFiles.add(uploadFile);
        return this;
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
