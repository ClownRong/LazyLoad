package com.joker.jrLi.util;

import android.util.Log;

import com.joker.jrLi.BuildConfig;

/**
 * 作者 : Joker
 * 创建日期 : 2016-07-22
 * 修改日期 : 2016-11-30
 * 版权所有 :
 */

public class L {

    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;

    private static final int Level = 0;

    private static final String TAG = "TAG";

    public static void v(String msg) {
        if (BuildConfig.DEBUG) split(1, TAG, msg, null);
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) split(2, TAG, msg, null);
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) split(3, TAG, msg, null);
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) split(4, TAG, msg, null);
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) split(5, TAG, msg, null);
    }

    public static void v(String msg, Throwable tr) {
        if (BuildConfig.DEBUG) split(1, TAG, msg, tr);
    }

    public static void d(String msg, Throwable tr) {
        if (BuildConfig.DEBUG) split(2, TAG, msg, tr);
    }

    public static void i(String msg, Throwable tr) {
        if (BuildConfig.DEBUG) split(3, TAG, msg, tr);
    }

    public static void w(String msg, Throwable tr) {
        if (BuildConfig.DEBUG) split(4, TAG, msg, tr);
    }

    public static void e(String msg, Throwable tr) {
        if (BuildConfig.DEBUG) split(5, TAG, msg, tr);
    }

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) split(1, tag, msg, null);
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) split(2, tag, msg, null);
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) split(3, tag, msg, null);
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) split(4, tag, msg, null);
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) split(5, tag, msg, null);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) split(1, tag, msg, tr);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) split(2, tag, msg, tr);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) split(3, tag, msg, tr);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) split(4, tag, msg, tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) split(5, tag, msg, tr);
    }

    private static void split(int level, String tag, String bodyMsg, Throwable tr) {
        if (bodyMsg.length() > 3900) {
            for (int i = 0; i < bodyMsg.length(); i += 3900) {
                if (i + 3900 < bodyMsg.length()) {
                    Log(level, tag, bodyMsg.substring(i, i + 3900), tr);
                } else {
                    Log(level, tag, bodyMsg.substring(i, bodyMsg.length()), tr);
                }
            }
        } else Log(level, tag, bodyMsg, tr);
    }

    private static void Log(int level, String tag, String bodyMsg, Throwable tr) {
        int execute;
        switch (level) {
            case 1:
                execute = tr == null ? Log.v(tag, bodyMsg) : Log.v(tag, bodyMsg, tr);
                break;
            case 2:
                execute = tr == null ? Log.d(tag, bodyMsg) : Log.d(tag, bodyMsg, tr);
                break;
            case 3:
                execute = tr == null ? Log.i(tag, bodyMsg) : Log.i(tag, bodyMsg, tr);
                break;
            case 4:
                execute = tr == null ? Log.w(tag, bodyMsg) : Log.w(tag, bodyMsg, tr);
                break;
            case 5:
                execute = tr == null ? Log.e(tag, bodyMsg) : Log.e(tag, bodyMsg, tr);
                break;
        }
    }

}
