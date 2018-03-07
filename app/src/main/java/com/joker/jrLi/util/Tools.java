package com.joker.jrLi.util;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.joker.jrLi.ui.BaseActivity;

/**
 * 作者：Joker
 * 创建日期：
 */

public class Tools {

    private static Toast mToast;

    public static void showMsg(Context context, String message) {
        if (context == null) return;
        if (!(context instanceof BaseActivity)
                || !((BaseActivity) context).mIsForeground
                || ActivityTaskManager.getInstance().getActivity((BaseActivity) context) == null) {
            return;
        }
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static Bundle setBundleData(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        return bundle;
    }

















}
