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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.request.RequestOptions;
import com.lkl.ansuote.hdqlibrary.mvp.BaseMVPActivity;
import com.lkl.ansuote.hdqlibrary.mvp.BasePresenter;
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
 * 业务模块 MVP 基类
 * @author huangdongqiang
 * @date 08/04/2018
 */
public abstract class BusinessMvpActivity<V, P extends BasePresenter<V>, C> extends BaseMVPActivity<V, P, C> {
    private BaseDialog mBaseDialog;
    private XRefreshLayout mRefreshLayout;
    /**
     * 标题栏布局
     */
    private ViewGroup mTitleLayout;

    /**
     * 内容布局
     */
    private View mContentView;
    /**
     * 空布局
     */
    private View mEmptyDataView;

    /**
     * 网络错误布局
     */
    private View mNetworkErrorView;

    // ------------------------------ {初始化相关 ------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void regEvent(boolean b) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (null != mBaseDialog) {
            mBaseDialog.dismiss();
            mBaseDialog = null;
        }
        super.onDestroy();
    }

    // ------------------------------ 初始化相关} ------------------------------

    // ------------------------------ {布局/标题栏相关 ------------------------------

    @Override
    public void setContentView(int layoutResID) {
        if (layoutResID != 0) {
            //模块里面设置了总布局,则加载总布局
            super.setContentView(layoutResID);
            mTitleLayout = (RelativeLayout) this.findViewById(R.id.ll_base_include_title_light);
            if (null == mTitleLayout) {
                mTitleLayout = (RelativeLayout) this.findViewById(R.id.ll_base_include_title_dark);
            }
        } else {
            //模块里面没有设置总布局，则加载 contentLayout
            int contentLayoutId = getContentLayoutId();
            if (0 == contentLayoutId) {
                throw new NullPointerException(this.getString(R.string.business_empty_content_layout));
            }

            super.setContentView(getLayoutView(contentLayoutId));
        }

        setStatusBarStyle();

    }

    /**
     * 设置状态栏的样式
     */
    private void setStatusBarStyle() {
        StatusBarStyle statusBarStyle = getStatusBarStyle();
        if (statusBarStyle == StatusBarStyle.NORMAL) {
            BarUtils.setStatusBarVisibility(this, true);
        } else if (statusBarStyle == StatusBarStyle.FULL_SCREEN) {
            BarUtils.setStatusBarVisibility(this, false);
        } else if (statusBarStyle == StatusBarStyle.TRANSLATE) {
            BarUtils.setStatusBarVisibility(this, true);
            if (null != mTitleLayout) {
                int height = mTitleLayout.getHeight();
                if (height == 0) {
                    height = this.getResources().getDimensionPixelSize(R.dimen.base_title_layout_height);
                }

                XViewUtil.setStatusBarTranslate(this, mTitleLayout, height);
            }
        }

        TitleLayoutStyle titleLayoutStyle = getTitleLayoutStyle();
        if (titleLayoutStyle == TitleLayoutStyle.TITLE_DARK) {
            BarUtils.setStatusBarLightMode(this, true);
        } else if (titleLayoutStyle == TitleLayoutStyle.TITLE_WEBVIEW_SCROLL_TEXT_TITLE){
            BarUtils.setStatusBarVisibility(this, true);
        } else {
            BarUtils.setStatusBarLightMode(this, false);
        }
    }


    /**
     * 获取总布局
     * @param contentLayoutId
     * @return
     */
    private View getLayoutView(int contentLayoutId) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        if (null != layoutInflater) {
            LinearLayout layout = new LinearLayout(this);
            if (null != layout) {
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                layout.setOrientation(LinearLayout.VERTICAL);

                mTitleLayout = getTitleLayout();
                mContentView = layoutInflater.inflate(contentLayoutId, null);
                mEmptyDataView = layoutInflater.inflate(R.layout.business_empty_data_view, null);
                mEmptyDataView.setVisibility(View.GONE);
                mNetworkErrorView = LayoutInflater.from(this).inflate(R.layout.business_network_error_view, null);
                mNetworkErrorView.setVisibility(View.GONE);
                layout.addView(mTitleLayout);
                layout.addView(mContentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layout.addView(mEmptyDataView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layout.addView(mNetworkErrorView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                return layout;
            }
        }
        return null;
    }

    /**
     * 获取当前Activity的UI布局 （内容布局）
     *
     * @return 布局id
     */
    protected int getContentLayoutId() {
        return 0;
    }

    /**
     * 获取当前Activity的UI布局 （总容布局）
     *
     * @return 布局id
     */
    @Override
    protected int getLayoutId() {
        return 0;
    }

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

    /**
     * 获取标题栏布局，子类可以重写此方法，重写title
     * @return
     */
    protected ViewGroup getTitleLayout() {
        if (null == mTitleLayout) {
            TitleLayoutStyle style = getTitleLayoutStyle();
            LayoutInflater inflater = LayoutInflater.from(this);
            ViewGroup view = null;
            if (null != inflater) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (style == TitleLayoutStyle.TITLE_DARK) {
                    view = (ViewGroup) inflater.inflate(R.layout.base_include_title_dark, null);
                } else if (style == TitleLayoutStyle.TITLE_LIGHT) {
                    view = (ViewGroup) inflater.inflate(R.layout.base_include_title_light, null);
                } else if (style == TitleLayoutStyle.TITLE_HIDE) {
                    view = (ViewGroup) inflater.inflate(R.layout.base_include_title_dark, null);
                    view.setVisibility(View.GONE);
                } else if (style == TitleLayoutStyle.TITLE_WEBVIEW_SCROLL_TEXT_TITLE) {
                    view = (ViewGroup) inflater.inflate(R.layout.base_include_title_dark, null);
                    lp.topMargin = -BarUtils.getStatusBarHeight();
                } else {
                    view = new RelativeLayout(this);
                }
                view.setLayoutParams(lp);
            }
            return view;
        } else {
            return mTitleLayout;
        }

    }

    // ------------------------------ 布局/标题栏相关} ------------------------------

    // ------------------------------ {通用接口 ------------------------------

    public View getContentView() {
        return mContentView;
    }

    /*@Override
    public void initView() {

    }*/

    @Override
    public void initTitle() {
        View titleLeft = findViewById(R.id.btn_title_left);
        if (null != titleLeft) {
            titleLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //finish();
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void initTitle(int contentRes) {
        initTitle();
        TextView contentTv = (TextView) findViewById(R.id.text_title_content);
        if (null != contentTv) {
            if (contentRes != 0) {
                contentTv.setText(contentRes);
            }
        }
    }

    @Override
    public void initTitle(String contentText) {
        initTitle();
        TextView contentTv = (TextView) findViewById(R.id.text_title_content);
        if (null != contentTv) {
            if (null != contentText) {
                contentTv.setText(contentText);
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
        if (null == mBaseDialog) {
            BaseDialog.Builder builder = new BaseDialog.Builder(this, R.layout.dialog_loading);
            mBaseDialog = builder.create();
        }
        if (null != mBaseDialog) {
            mBaseDialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (null != mBaseDialog) {
            mBaseDialog.dismiss();
        }
    }

    @Override
    public void actionStart(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        this.startActivity(intent);
    }


    @Override
    public void showContentView() {
        if (null != mContentView) {
            mContentView.setVisibility(View.VISIBLE);
        }

        if (null != mEmptyDataView) {
            mEmptyDataView.setVisibility(View.GONE);
        }

        if (null != mNetworkErrorView) {
            mNetworkErrorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyDataView() {
        if (null != mContentView) {
            mContentView.setVisibility(View.GONE);
        }

        if (null != mEmptyDataView) {
            mEmptyDataView.setVisibility(View.VISIBLE);
        }

        if (null != mNetworkErrorView) {
            mNetworkErrorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showNetworkErrorView() {
        if (null != mContentView) {
            mContentView.setVisibility(View.GONE);
        }

        if (null != mEmptyDataView) {
            mEmptyDataView.setVisibility(View.GONE);
        }

        if (null != mNetworkErrorView) {
            mNetworkErrorView.setVisibility(View.VISIBLE);
        }
    }


    // ------------------------------ 通用接口} ------------------------------


    // ------------------------------ {刷新控件相关 ------------------------------

    /**
     * 提供外界设置刷新界面，适配 Activity 有多个 refreshLayout 的情况
     * @return
     */
    public XRefreshLayout getRefreshLayoutFromModule() {
        return (XRefreshLayout) findViewById(R.id.refreshLayout);
    }

    @Override
    public void initRefreshLayout() {
        mRefreshLayout = getRefreshLayoutFromModule();
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
    public void onLoadMoreCallBack() {

    }

    /**
     * 下拉刷新回调
     */
    public void onRefreshCallback() {

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
        KeyboardUtils.hideSoftInput(this);
        //Utils.hideSoftInputFromWindow(this);
    }

    // ------------------------------ 刷新控件相关} ------------------------------

}
