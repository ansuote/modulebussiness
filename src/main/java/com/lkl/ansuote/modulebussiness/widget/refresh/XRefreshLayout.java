package com.lkl.ansuote.modulebussiness.widget.refresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.lkl.ansuote.modulebussiness.R;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;


/**
 * 刷新控件外层封装类。
 * 封装目的：以后内部使用开源控件发生改变，
 * 比如 SmartRefreshLayout 被换掉了，外层用法还是不变，只需改动此类即可（内部必须实现 IQRefreshLayout 接口）。
 *
 * 内部基于 SmartRefreshLayout 开源刷新控件。 地址：https://github.com/scwang90/SmartRefreshLayout
 *
 * 常用接口说明
 *  setEnableRefresh(true);     //是否启用下拉刷新功能
 *  setEnableLoadMore(true);    //是否启用上拉加载功能
 *  setEnableAutoLoadmore(true);//是否启用列表惯性滑动到底部时自动加载更多
 *  finishRefresh();            //结束刷新
 *  finishLoadMore();           //结束加载
 *  setLoadmoreFinished();      //设置数据全部加载完成，将不能再次触发加载功能
 *
 *  setRefreshHeader(new ClassicsHeader(this));//重新设置Header
 *  setRefreshFooter(new ClassicsFooter(this));//重新设置Footer
 *
 * 常用回调
 *  setOnRefreshListener()      //单独设置刷新监听器
 *  setOnLoadmoreListener()     //单独设置加载监听器
 *
 *  setOnRefreshLoadmoreListener()  //同时设置刷新和加载监听器
 *  setOnMultiPurposeListener()     //设置多功能监听器
 *
 * 更多接口请查阅接口文档
 *  https://github.com/scwang90/SmartRefreshLayout/blob/master/art/md_property.md
 *
 */
public class XRefreshLayout extends SmartRefreshLayout implements IRefreshLayout {
    private final int DEFAULT_HEADER_HEIGHT = 60;
    private final int DEFAULT_FOOTER_HEIGHT = 50;

    public XRefreshLayout(Context context) {
        super(context);
        init(context, null);
    }

    public XRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public XRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null == context) {
            return;
        }

        initDefaultAttribute(context);

        initTip(context);
    }

    /**
     * 不能在 init 的时候动态设置 setRefreshHeader 和 setRefreshFooter。
     * 因为 init 的时候控件并没有完成绘制，故需在此处设置 header 和 footer
     */
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                //return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
                return new MaterialHeader(context).setShowBezierWave(false); //Material
                //return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);   //经典样式
                //return new CircleHeader(context); //圆形大水滴
                //return new DeliveryHeader(context); //飞行盒子
                //return new WaterDropHeader(context); //苹果水滴效果
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                //return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
                //return new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale); //球脉冲
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Scale);
            }
        });
    }

    /**
     * 设置默认的头布局和底部布局
     * @param context
     */
    private void initDefaultAttribute(Context context) {

//        this.setHeaderHeight(DEFAULT_HEADER_HEIGHT);
        this.setFooterHeight(DEFAULT_FOOTER_HEIGHT);

        //启用内容视图拖动效果
        this.setEnableHeaderTranslationContent(false);

        //设置默认可以下拉刷新
        this.setEnableRefresh(true);

        //设置默认不能上拉加载更多
        this.setEnableLoadMore(false);

        //是否启用列表惯性滑动到底部时自动加载更多
        this.setEnableAutoLoadMore(true);

        this.setDragRate(0.5f);//显示下拉高度/手指真实下拉高度=阻尼效果
        this.setReboundDuration(200);//回弹动画时长（毫秒）
    }

    //根据系统语言初始化加载文字
    private void initTip(Context context) {
        ClassicsHeader.REFRESH_HEADER_PULLDOWN = context.getString(R.string.refresh_header_pulldown);
        ClassicsHeader.REFRESH_HEADER_REFRESHING = context.getString(R.string.refresh_header_refreshing);
        ClassicsHeader.REFRESH_HEADER_LOADING = context.getString(R.string.refresh_header_loading);
        ClassicsHeader.REFRESH_HEADER_RELEASE = context.getString(R.string.refresh_header_release);
        ClassicsHeader.REFRESH_HEADER_FINISH = context.getString(R.string.refresh_header_finish);
        ClassicsHeader.REFRESH_HEADER_FAILED = context.getString(R.string.refresh_header_failed);

        ClassicsFooter.REFRESH_FOOTER_PULLUP = context.getString(R.string.refresh_footer_pullup);
        ClassicsFooter.REFRESH_FOOTER_RELEASE = context.getString(R.string.refresh_footer_release);
        ClassicsFooter.REFRESH_FOOTER_LOADING = context.getString(R.string.refresh_footer_loading);
        ClassicsFooter.REFRESH_FOOTER_FINISH = context.getString(R.string.refresh_footer_finish);
        ClassicsFooter.REFRESH_FOOTER_FAILED = context.getString(R.string.refresh_footer_failed);
        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = context.getString(R.string.refresh_footer_allloaded);
    }
}
