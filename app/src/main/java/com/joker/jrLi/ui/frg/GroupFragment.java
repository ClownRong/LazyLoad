package com.joker.jrLi.ui.frg;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.joker.jrLi.FrgPageAdapter;
import com.joker.jrLi.R;
import com.joker.jrLi.databinding.FrgContentBinding;
import com.joker.jrLi.ui.BaseFragment;
import com.joker.jrLi.ui.aty.MainActivity;
import com.joker.jrLi.util.L;
import com.joker.jrLi.util.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.OnClick;

/**
 * 作者：Joker
 * 创建日期：
 */

public class GroupFragment extends BaseFragment {

    private FrgContentBinding mBind;

    private int mType, mCurrentIndex;

    private GroupFragment win, ios, linux;

    public boolean mInnerLoad;

    @Override
    protected void initParam(Bundle param) {
        mType = param.getInt("type", 1);
    }

    @Override
    protected int bindLayout() {
        if (mType > 3) mIsViewPager = true; // 容器为mIsViewPager时设为true；
        return R.layout.frg_content;
    }

    @Override
    protected boolean convertView(ViewDataBinding bind) {
        boolean error = false;
        if (bind instanceof FrgContentBinding) {
            mBind = (FrgContentBinding) bind;
        } else {
            L.e(getClass().getName(), "类型错误：" + bind.getClass().getSimpleName());
            error = true;
        }
        return error;
    }

    @Override
    protected void logicBusiness() { // 初始化数据
        switch (mType) {
            case 1:
            case 3:
                mBind.vpGroup.setVisibility(View.GONE);
                break;
            case 2:
                mBind.tvMsg.setVisibility(View.GONE);
                mBind.tvTime.setVisibility(View.GONE);

                ArrayList<Fragment> fragments = new ArrayList<>();

                fragments.add(win = new GroupFragment());
                fragments.add(ios = new GroupFragment());
                fragments.add(linux = new GroupFragment());
                win.setArguments(Tools.setBundleData(4));
                ios.setArguments(Tools.setBundleData(5));
                linux.setArguments(Tools.setBundleData(6));

                // case：Activity中嵌套Fragment：getSupportFragmentManager；
                // case：Fragment中嵌套Fragment：getChildFragmentManager；
                // 原因：getSupportFragmentManager是fragment所在父容器的碎片管理，
                // 而getChildFragmentManager是fragment所在子容器的碎片管理；
                // 如果用getSupportFragmentManager会在viewpager中出现fragment不显示情况；
                FrgPageAdapter myPagerAdapter = new FrgPageAdapter(getChildFragmentManager(), fragments);
                myPagerAdapter.setType(3);
                mBind.vpGroup.setAdapter(myPagerAdapter);

                mBind.vpGroup.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        mCurrentIndex = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
                break;
            case 4:
            case 5:
            case 6:
                mInnerLoad = true;
                mBind.vpGroup.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void loadData() { // 懒加载

        if (mType != 2) {
            setData();
        }

        if (mType == 2) {
            if (win != null && win.mInnerLoad && mCurrentIndex == 0) {
                win.loadData();
            } else if (ios != null && ios.mInnerLoad && mCurrentIndex == 1) {
                ios.loadData();
            } else if (linux != null && linux.mInnerLoad && mCurrentIndex == 2) {
                linux.loadData();
            }
        }

    }

//    @OnClick({R.id.tv_msg, R.id.tv_time})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_msg:
//            case R.id.tv_time:
//                startActivity(new Intent(getActivity(), MainActivity.class));
//                break;
//        }
//    }

    private void setData() {
        String msg = mType == 1 ? "首页(容器：FrameLayout)" : mType == 3 ? "铃声(容器：FrameLayout)" : mType == 4 ? "Win(容器：ViewPager - 第一页)" : mType == 5 ? "Ios(容器：ViewPager - 第二页)" : mType == 6 ? "Linux(容器：ViewPager - 第三页)" : "";
        String time = "刷新时间：" + new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss")
                .format(new Date(System.currentTimeMillis()));
        L.i(msg);
        L.i(time);
        mBind.tvMsg.setText(msg);
        mBind.tvTime.setText(time);
    }

}
