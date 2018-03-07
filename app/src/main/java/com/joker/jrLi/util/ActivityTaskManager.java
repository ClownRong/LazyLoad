package com.joker.jrLi.util;

import android.app.Activity;
import android.content.Context;

import com.joker.jrLi.ui.BaseActivity;

import java.util.Iterator;
import java.util.Stack;


/**
 * 作者 : Joker
 * 创建日期 :
 * 修改日期 :
 * 版权所有 :
 */

public class ActivityTaskManager {

    private static ActivityTaskManager activityTaskManager = null;

    // CopyOnWriteArrayList
    private Stack<BaseActivity> activityStack = new Stack<>();

    private ActivityTaskManager() {
    }

    public static synchronized ActivityTaskManager getInstance() {
        if (activityTaskManager == null) {
            activityTaskManager = new ActivityTaskManager();
        }
        return activityTaskManager;
    }

    public void addActivity(BaseActivity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity
     */
    public Activity getTopActivity() {
        if (activityStack.size() > 0) {
            L.d(getClass().getSimpleName(), "当前Activity：" + activityStack.lastElement());
            return activityStack.lastElement();
        }
        return null;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(BaseActivity activity) {
        if (activity != null) { // && !activity.isFinishing()
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 返回到栈底
     */
    public void getBottomOfStack() {
        for (int i = activityStack.size() - 1; i > 0; i--) {
            finishActivity(activityStack.get(i));
        }
    }

    /**
     * 清除栈底
     */
    public void removeBottomOfTheStack() {
        for (int i = activityStack.size() - 2; i >= 0; i--) {
            finishActivity(activityStack.get(i));
        }
    }

    /**
     * end process
     */
    public void appExit(Context context) {
        try {
            finishAllActivity();
            //ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            //activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * kill all
     */
    public void finishAllActivity() {
        Iterator<BaseActivity> it = activityStack.iterator();
        while (it.hasNext()) {
            BaseActivity activity = it.next();
            finish(activity);
        }
        activityStack.clear();
    }

    /**
     * 结束指定的Activity
     */
    public void finish(BaseActivity activity) {
        if (activity != null) { // && !activity.isFinishing()
            activity.finish();
            activity = null;
        }
    }

    /**
     * 得到保存在管理器中的Activity对象。
     *
     * @return Activity
     */
    public BaseActivity getActivity(Class<? extends BaseActivity> cls) {
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().getSimpleName().equals(cls.getSimpleName())) {
                return activity;
            }
        }
        return null;
    }

    public BaseActivity getActivity(BaseActivity currentActivity) {
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().getSimpleName().equals(currentActivity.getClass().getSimpleName())) {
                return activity;
            }
        }
        return null;
    }

    /**
     *
     */
    public void closeOtherAllActivity(Class<? extends BaseActivity> cls) {
        BaseActivity temp = null;
        for (int i = 0; i < activityStack.size(); i++) {
            BaseActivity activity = activityStack.get(i);
            if (activity.getClass().getSimpleName().equals(cls.getSimpleName())) {
                temp = activity;
            } else {
                finishActivity(activity);
            }
        }
        activityStack.clear();
        if (temp != null) {
            activityStack.add(temp);
        }
    }

    /**
     * 移除Activity对象,如果它未结束则结束它。
     *
     * @param cls 所要删除的Activity类
     */
    public void removeActivity(Class<? extends BaseActivity> cls) {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            BaseActivity activity = activityStack.get(i);
            if (activity.getClass().getSimpleName().equals(cls.getSimpleName())) {
                finishActivity(activity);
                return;
            }
        }
    }

    public int getActivityCount() {
        return activityStack.size();
    }

}
