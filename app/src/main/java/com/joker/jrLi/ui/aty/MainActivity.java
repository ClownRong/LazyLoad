package com.joker.jrLi.ui.aty;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.joker.jrLi.R;
import com.joker.jrLi.databinding.ActivityMainBinding;
import com.joker.jrLi.ui.BaseActivity;
import com.joker.jrLi.ui.frg.GroupFragment;
import com.joker.jrLi.util.L;
import com.joker.jrLi.util.Tools;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBind;

    private FragmentManager mManager;
    private GroupFragment mHomepageFragment, mSystemFragment, mRingFragment;
    private int mCurrentIndex = 1;

    @Override
    protected void initParam(Bundle param) {
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean convertView(ViewDataBinding bind) {
        boolean error = false;
        if (bind instanceof ActivityMainBinding) {
            mBind = (ActivityMainBinding) bind;
        } else {
            L.e(getClass().getName(), "类型错误：" + bind.getClass().getSimpleName());
            error = true;
        }
        return error;
    }

    @Override
    protected void logicBusiness() { // 初始化数据

        mManager = getSupportFragmentManager();
        getFragment(mCurrentIndex);

        mBind.navigation.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_page:
                    if (mCurrentIndex != 1) getFragment(mCurrentIndex = 1);
                    return true;
                case R.id.system:
                    if (mCurrentIndex != 2) getFragment(mCurrentIndex = 2);
                    return true;
                case R.id.ring:
                    if (mCurrentIndex != 3) getFragment(mCurrentIndex = 3);
                    return true;
            }
            return false;
        });

    }

    @Override

    protected void setDataAgain() {
        super.setDataAgain();
    }

    @Override
    protected void clearData() {
        super.clearData();
    }

    public void getFragment(int i) {
        FragmentTransaction transaction = mManager.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 1:
                if (mHomepageFragment == null) {
                    mHomepageFragment = new GroupFragment();
                    transaction.add(R.id.fl_group, mHomepageFragment, "tag");
                    mHomepageFragment.setArguments(Tools.setBundleData(i));
                } else transaction.show(mHomepageFragment);
                break;
            case 2:
                if (mSystemFragment == null) {
                    mSystemFragment = new GroupFragment();
                    transaction.add(R.id.fl_group, mSystemFragment);
                    mSystemFragment.setArguments(Tools.setBundleData(i));
                } else transaction.show(mSystemFragment);
                break;
            case 3:
                if (mRingFragment == null) {
                    mRingFragment = new GroupFragment();
                    transaction.add(R.id.fl_group, mRingFragment);
                    mRingFragment.setArguments(Tools.setBundleData(i));
                } else transaction.show(mRingFragment);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mHomepageFragment != null) {
            transaction.hide(mHomepageFragment);
        }
        if (mSystemFragment != null) {
            transaction.hide(mSystemFragment);
        }
        if (mRingFragment != null) {
            transaction.hide(mRingFragment);
        }
    }

}
