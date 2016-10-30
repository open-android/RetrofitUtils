package com.itheima.retrofitutils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 日志工具类
 * Created by forever on 2016/4/11.
 */
public class L {

    public static final boolean isDebug = true;
    private static final String TAG = "MY_TAG";

    public static final String HTTP_TAG = "HTTP_TAG";

    public static void i(String tag, String message) {
        if (isDebug) {
            Log.i(tag, message);
        }
    }

    public static void i(String message) {
        i(TAG, message);
    }

    public static void e(String tag, String message) {
        if (isDebug) {
            Log.e(tag, message);
        }
    }

    public static void e(String message) {
        e(TAG, message);
    }

    public static void e(String tag, String message, Throwable e) {
        if (isDebug) {
            Log.e(tag, message, e);
        }


    }

    public static void e(String tag, Throwable e) {
        e(tag, "", e);
    }

    public static void e(Throwable e, String message) {
        e(TAG, message, e);
    }

    public static void e(Throwable e) {
        e(TAG, "", e);
    }


}

