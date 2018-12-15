package com.lkl.ansuote.modulebussiness.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.request.RequestOptions;
import com.lkl.ansuote.hdqlibrary.mvp.IBaseFragmentView;
import com.lkl.ansuote.hdqlibrary.mvp.fragment.BaseFragmentPresenter;
import com.lkl.ansuote.hdqlibrary.mvp.fragment.BaseMvpFragment;
import com.lkl.ansuote.hdqlibrary.util.Utils;
import com.lkl.ansuote.hdqlibrary.util.image.ImageLoader;
import com.lkl.ansuote.hdqlibrary.widget.dialog.base.BaseDialog;
import com.lkl.ansuote.modulebussiness.R;
import com.lkl.ansuote.modulebussiness.style.StatusBarStyle;
import com.lkl.ansuote.modulebussiness.style.TitleLayoutStyle;
import com.lkl.ansuote.modulebussiness.util.XViewUtil;
import com.lkl.ansuote.modulebussiness.widget.refresh.XRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

/**
 * @author huangdongqiang
 * @date 13/04/2018
 */
public abstract class BusinessMvpFragment<P extends BaseFragmentPresenter, C> extends BaseMvpFragment<P, C> implements IBaseFragmentView {
    private BaseDialog mBaseDialog;
    private XRefreshLayout mRefreshLayout;

    // ------------------------------ {初始化相关 ------------------------------


   /* @Override
    protected BusinessFragmentComponent getFragmentComponent() {
        return DaggerBusinessFragmentComponent
                .builder()
                .appComponent(BusinessApplication.getAppComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
    }
*/

    @Override
    public void regEvent(boolean b) {

    }

    @Override
    public void setUpData() {
        mPresenter.setUpData();
    }

    @Override
    public void setUpView(View view) {
        if (getStatusBarStyle() == StatusBarStyle.TRANSLATE) {
            BarUtils.setStatusBarAlpha(getActivity(), 0);
            XViewUtil.setStatusBarTranslate(getActivity());
        }

        //设置默认
        BarUtils.setStatusBarLightMode(getActivity(), false);
        mPresenter.setUpView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //每次界面可见的时候，重新设置状态栏颜色
        if (isVisibleToUser) {
            if (null != getActivity()) {
                TitleLayoutStyle titleLayoutStyle = getTitleLayoutStyle();
                if (titleLayoutStyle == TitleLayoutStyle.TITLE_DARK) {
                    BarUtils.setStatusBarLightMode(getActivity(), true);
                } else if (titleLayoutStyle == TitleLayoutStyle.TITLE_WEBVIEW_SCROLL_TEXT_TITLE){
                    BarUtils.setStatusBarVisibility(getActivity(), true);
                } else {
                    BarUtils.setStatusBarLightMode(getActivity(), false);
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        if (isLazyLoadEnabled()) {
            enableLazyLoad();
        }
        return view;
    }

    /**
     * 默认使用懒加载
     * @return
     */
    public boolean isLazyLoadEnabled() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // ------------------------------ 初始化相关} ------------------------------



    // ------------------------------ {布局/标题栏相关 ------------------------------

    /**
     * 获取状态栏的样式
     * @return
     */
    protected StatusBarStyle getStatusBarStyle() {
        return StatusBarStyle.TRANSLATE;
    }

    /**
     * 获取title的样式
     * @return
     */
    protected TitleLayoutStyle getTitleLayoutStyle(){
        return TitleLayoutStyle.TITLE_LIGHT;
    }

    // ------------------------------ 布局/标题栏相关} ------------------------------

    // ------------------------------ {通用接口 ------------------------------

    @Override
    public void actionStart(Class<? extends Activity> cls) {
        if (null != getActivity()) {
            Intent intent = new Intent(getActivity(), cls);
            this.startActivity(intent);
        }
    }


    @Override
    public void initTitle() {
        if (null != getActivity()) {
            View titleLeft = getActivity().findViewById(R.id.btn_title_left);
            if (null != titleLeft) {
                titleLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //getActivity().finish();
                        getActivity().onBackPressed();
                    }
                });
            }
        }

    }

    @Override
    public void initTitle(int contentRes) {
        initTitle();
        if (null != getActivity()) {
            TextView contentTv = (TextView) getView().findViewById(R.id.text_title_content);
            if (null != contentTv) {
                if (contentRes != 0) {
                    contentTv.setText(contentRes);
                }
            }
        }
    }

    @Override
    public void initTitle(String contentText) {
        initTitle();
        if (null != getActivity()) {
            TextView contentTv = (TextView) getActivity().findViewById(R.id.text_title_content);
            if (null != contentTv) {
                if (null != contentText) {
                    contentTv.setText(contentText);
                }
            }
        }
    }

    @Override
    public void showMsg(String errorMsg) {
        if (null != errorMsg) {
            ToastUtils.showShort(errorMsg);
        }
    }

    @Override
    public void showMsg(int resId) {
        ToastUtils.showShort(resId);
    }


    @Override
    public void showLoadingDialog() {
        if (null != getActivity() && !getActivity().isFinishing()) {
            if (null == mBaseDialog) {
                BaseDialog.Builder builder = new BaseDialog.Builder(getActivity(), R.layout.dialog_loading);
                mBaseDialog = builder.create();
            }
            if (null != mBaseDialog) {
                mBaseDialog.show();
            }
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (null != getActivity() && !getActivity().isFinishing()) {
            if (null != mBaseDialog) {
                mBaseDialog.dismiss();
            }
        }
    }




    @Override
    public void showNetworkErrorView() {
        // TODO: 2018/5/19
    }

    @Override
    public void showContentView() {
        // TODO: 2018/5/19
    }

    @Override
    public void showEmptyDataView() {
        // TODO: 2018/5/19
    }

    @Override
    public void loadCircleCrop(Context context, String url, ImageView iv) {
        if (null == context || null == iv) {
            return;
        }

        RequestOptions options = new RequestOptions()
                //.placeholder(R.mipmap.placeholder) //加载中图片
                /**
                 * 加载失败图片
                 */
                .error(R.drawable.base_default_head_portrait_icon)
                /**
                 * url为空图片
                 */
                .fallback(R.drawable.base_default_head_portrait_icon)
                //.centerCrop() // 填充方式
                //.override(600,600) //尺寸
                //.transform(new CircleCrop()) //圆角
                //.priority(Priority.HIGH) //优先级
                //.diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略，后面详细介绍
                .circleCrop();
        ImageLoader.load(context, url, iv, options);
    }

    public void loadCircleCrop(Context context, Uri uri, ImageView iv) {
        if (null == context || null == iv) {
            return;
        }

        RequestOptions options = new RequestOptions()
                //.placeholder(R.mipmap.placeholder) //加载中图片
                /**
                 * 加载失败图片
                 */
                .error(R.drawable.base_default_head_portrait_icon)
                /**
                 * url为空图片
                 */
                .fallback(R.drawable.base_default_head_portrait_icon)
                //.centerCrop() // 填充方式
                //.override(600,600) //尺寸
                //.transform(new CircleCrop()) //圆角
                //.priority(Priority.HIGH) //优先级
                //.diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略，后面详细介绍
                .circleCrop();
        ImageLoader.load(context, uri, iv, options);
    }

    @Override
    public void hideSoftInputFromWindow() {
        Utils.hideSoftInputFromWindow(getActivity());
    }

    // ------------------------------ 通用接口} ------------------------------


    // ------------------------------ {刷新控件相关 ------------------------------

    /**
     * 提供外界设置刷新界面，适配 Activity 有多个 refreshLayout 的情况
     * @return
     */
    public XRefreshLayout getRefreshLayoutFromModule() {
        return (XRefreshLayout) getActivity().findViewById(R.id.refreshLayout);
    }

    @Override
    public void initRefreshLayout() {
        if (null != getActivity()) {
            mRefreshLayout = getRefreshLayoutFromModule();
        }
        if (null != mRefreshLayout) {
            if (getStatusBarStyle() == StatusBarStyle.TRANSLATE) {
                mRefreshLayout.setHeaderInsetStart(BarUtils.getStatusBarHeight());
            }

            mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshLayout) {
                    /**
                     * 上拉加载更多回调
                     */
                    onLoadMoreCallBack();
                }

                @Override
                public void onRefresh(RefreshLayout refreshLayout) {
                    /**
                     * 下拉刷新回调
                     */
                    onRefreshCallback();
                }
            });
            //设置显示拉到最后出现底部布局
            mRefreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        }
    }

    @Override
    public void autoRefresh() {
        if (null != mRefreshLayout) {
            mRefreshLayout.autoRefresh();
        }
    }

    /**
     * 加载更多回调
     */
    public void onLoadMoreCallBack(){

    }

    /**
     * 下拉刷新回调
     */
    public void onRefreshCallback(){

    }


    @Override
    public void finishRefresh() {
        if (null != mRefreshLayout) {
            mRefreshLayout.finishRefresh();
        }
    }


    @Override
    public void finishLoadMore() {
        if (null != mRefreshLayout) {
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void setEnableLoadMore(boolean enable) {
        if (null != mRefreshLayout) {
            mRefreshLayout.setEnableLoadMore(enable);
            mRefreshLayout.setNoMoreData(false);
        }
    }

    @Override
    public void setEnableRefresh(boolean enable) {
        if (null != mRefreshLayout) {
            mRefreshLayout.setEnableRefresh(enable);
        }
    }

    @Override
    public void finishLoadMoreWithNoMoreData() {
        if (null != mRefreshLayout) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        }
    }

    @Override
    public void setEnableFooterFollowWhenLoadFinished(boolean enable) {
        if (null != mRefreshLayout) {
            mRefreshLayout.setEnableFooterFollowWhenLoadFinished(enable);
        }
    }
}
