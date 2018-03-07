package com.joker.jrLi.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.joker.jrLi.util.L;
import com.joker.jrLi.util.ActivityTaskManager;
import com.joker.jrLi.util.Tools;

import butterknife.ButterKnife;

/**
 * 作者 : Joker
 * 创建日期 :
 * 修改日期 :
 * 版权所有 :
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected boolean mErrorView;
    protected AlertDialog.Builder mLoginInvalid;
    protected ProgressDialog mProDialog;

    public boolean mIsForeground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        L.i("Base", getClass().getSimpleName());

        ActivityTaskManager.getInstance().addActivity(this);

        initParam(getIntent().getExtras());

        ViewDataBinding bind = DataBindingUtil.setContentView(this, bindLayout());

        ButterKnife.bind(this);

        if (convertView(bind)) {
            mErrorView = true;
            convertError();
            return;
        }

        logicBusiness();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mErrorView) return;
        setDataAgain();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsForeground = false;
    }

    @Override
    protected void onDestroy() {
        clearData();
        ActivityTaskManager.getInstance().finishActivity(this);
        super.onDestroy();
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
        Tools.showMsg(this, "请重新刷新此界面！");
    }

    /**
     * 业务逻辑
     */
    protected abstract void logicBusiness();

    /**
     * set data again
     */
    protected void setDataAgain() {
        if (mErrorView) return;
    }

    /**
     * clear data
     */
    protected void clearData() {
        if (mErrorView) return;
        hideProDialog();
    }

    protected void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }





    public void showProDialog() {
        showProDialog("(String) getText(R.string.loading_message)");
    }

    public void showProDialog(String message) {
        if (mProDialog != null) {
            mProDialog.setMessage(message);
            mProDialog.show();
        } else {
            mProDialog = new ProgressDialog(this);
            mProDialog.setCanceledOnTouchOutside(false);
            mProDialog.setMessage(message);
            mProDialog.show();
        }
    }

    public void hideProDialog() {
        if (mProDialog != null && mProDialog.isShowing()) {
            mProDialog.dismiss();
        }
    }

    public void loginInvalid() {
        if (mLoginInvalid == null) {
            mLoginInvalid = new AlertDialog.Builder(this)
                    .setMessage("登录已失效，是否重新登录？")
                    .setPositiveButton("", (dialog, which) -> {
                        dialog.dismiss();
                        // startActivity(LoginActivity.class);
                        ActivityTaskManager.getInstance().removeBottomOfTheStack();
                    })
                    .setNegativeButton("", (dialog, which) -> {
                        dialog.dismiss();
                        ActivityTaskManager.getInstance().appExit(this);
                    });
            mLoginInvalid.setCancelable(false);
        }
        mLoginInvalid.show();
    }

    //




}
