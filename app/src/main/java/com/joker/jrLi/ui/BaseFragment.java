package com.joker.jrLi.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joker.jrLi.util.L;
import com.joker.jrLi.util.Tools;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：Joker
 * 创建日期：
 * 修改时间：
 * 版权所有：
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 是否第一次加载
     */
    private boolean mIsFirstLoad = true;

    /**
     * view error
     */
    protected boolean mErrorView;

    /**
     * 标志位，View已经初始化完成。
     * 用isAdded()属性代替
     * isPrepared还是准一些,isAdded有可能出现onCreateView没走完但是isAdded了
     */
    private boolean mIsPrepared;

    /**
     * 是否可见状态
     */
    private boolean mIsVisible;

    /**
     * 是否已经加载数据
     */
    private boolean mIsLoadData;

    /**
     * 容器是否为ViewPager
     */
    protected boolean mIsViewPager;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
        // 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
        // 导致initData反复执行,所以这里注释掉
        // isFirstLoad = true;

        // 取消 isFirstLoad = true的注释 , 因为上述的initData本身就是应该执行的
        // onCreateView执行 证明被移出过FragmentManager initData确实要执行.
        // 如果这里有数据累加的Bug 请在初始化布局前初始化您的数据 比如 list.clear();

        mIsFirstLoad = false;

        initParam(getArguments());

        ViewDataBinding bind = DataBindingUtil.inflate(inflater, bindLayout(), container, false);
        unbinder = ButterKnife.bind(this, bind.getRoot());

        L.d("Base", getClass().getSimpleName() + " initViews");

        if (convertView(bind)) {
            mErrorView = true;
            convertError();
            return bind.getRoot();
        }

        return bind.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.d(getClass().getSimpleName(), "onActivityCreated");
        mIsPrepared = true;
        lazyLoad();
    }

    /**
     * 与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        L.d(getClass().getSimpleName(), "setUserVisibleHint" + isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    /**
     * 通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     *               visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        L.d(getClass().getSimpleName(), "show and hide");
        if (!hidden) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见时调用
     */
    protected void onVisible() {
        L.d(getClass().getSimpleName(), "可见时调用 : onVisible");
        lazyLoad();
    }

    /**
     * 原fragment开启新activity，remove后返回当前的fragment执行
     */
//    @Override
//    public void onStart() {
//        super.onStart();
//        // case : mIsViewPager = true
//        if (!mIsFirstLoad && !mIsFirstLoad && !mIsVisible) return;
//        if (!mIsLoadData) {
//            L.d(getClass().getSimpleName(), "执行onStart -- > 懒加载");
//            lazyLoad();
//        }
//    }

    /**
     * 再次调用可见方法
     */
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (getUserVisibleHint()) {
//            setUserVisibleHint(true);
//        }
//    }
    @Override
    public void onPause() {
        super.onPause();
        L.d(getClass().getSimpleName(), "不可见 -- onPause");
        mIsLoadData = false;
    }

    /**
     * 不可见时调用
     */
    protected void onInvisible() {
        L.d(getClass().getSimpleName(), "不可见时调用 : onInvisible");
        mIsLoadData = false;
    }

    /**
     * 可见时调用(懒加载)
     */
    protected void lazyLoad() {
        if (mErrorView) {
            L.d(getClass().getSimpleName(), "view error");
            return;
        }
        L.d(getClass().getSimpleName(),
                "mIsFirstLoad : " + mIsFirstLoad +
                        " mIsPrepared : " + mIsPrepared +
                        " mIsVisible : " + mIsVisible);

        if (mIsViewPager && !mIsVisible) {
            mIsViewPager = false;
        } else if (mIsFirstLoad && mIsVisible && !mIsPrepared) {
            L.d(getClass().getSimpleName(), "略过......");
        } else if (!mIsFirstLoad && mIsPrepared) {
            L.d(getClass().getSimpleName(), "先初始化，再懒加载");
            logicBusiness();
            mIsFirstLoad = false;
            mIsPrepared = false;
            loadData();
            mIsLoadData = true;
        } else {
            L.d(getClass().getSimpleName(), "直接执行懒加载");
            loadData();
            mIsLoadData = true;
        }

    }

    @Override
    public void onDestroy() {
        clearData();
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * init Bundle
     *
     * @param param
     */
    protected abstract void initParam(Bundle param);

    /**
     * bind layout
     *
     * @return layout
     */
    protected abstract int bindLayout();

    /**
     * sub
     *
     * @param bind
     */
    protected abstract boolean convertView(ViewDataBinding bind);

    /**
     * =。= bug
     */
    protected void convertError() {
        L.e(getClass().getSimpleName(), "convertError");
        Tools.showMsg(getContext(), "请重新刷新此界面！");
    }

    /**
     * 业务逻辑
     */
    protected abstract void logicBusiness();

    /**
     * 加载数据
     */
    protected abstract void loadData();

    /**
     * clear data
     */
    protected void clearData() {
        if (mErrorView) return;
    }

    protected void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getContext(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getContext(), cls);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getContext(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

}
