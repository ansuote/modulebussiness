package com.lkl.ansuote.modulebussiness.util;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.BarUtils;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.lkl.ansuote.modulebussiness.R;

/**
 * @author huangdongqiang
 * @date 20/04/2018
 */
public class XViewUtil {

        /**
         * 设置状态栏颜色
         * @param color
         */
    public static void setStatusColor(Activity activity, int color) {
        if (null == activity) {
            return;
        }

        UltimateBar.newColorBuilder()
                .statusColor(ContextCompat.getColor(activity, color))       // 状态栏颜色
                .statusDepth(40)                // 状态栏颜色深度
                //.applyNav(true)                 // 是否应用到导航栏
                //.navColor(navColor)             // 导航栏颜色
                //.navDepth(50)                   // 导航栏颜色深度
                .build(activity)
                .apply();
    }


    @Deprecated
    public static void setStatusBarTranslate(Activity activity) {
        if (null == activity) {
            return;
        }

        RelativeLayout lightTitle = (RelativeLayout)activity.findViewById(R.id.ll_base_include_title_light);
        RelativeLayout darkTitle = (RelativeLayout)activity.findViewById(R.id.ll_base_include_title_dark);

        //未适配的内容高度
        int titleHeight = activity.getResources().getDimensionPixelSize(R.dimen.base_title_layout_height);
        if (null != lightTitle) {
            setStatusBarTranslate(activity, lightTitle, titleHeight);
            BarUtils.setStatusBarLightMode(activity, false);
        }

        if (null != darkTitle) {
            setStatusBarTranslate(activity, darkTitle, titleHeight);
            BarUtils.setStatusBarLightMode(activity, true);
        }

    }

    /**
     * 动态改变布局的高度和上内边距
     * @param activity
     * @param viewGroup  需要适配的view
     * @param originalHeight 未适配的内容高度
     */
    public static void setStatusBarTranslate(Activity activity, ViewGroup viewGroup, int originalHeight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

            int statusBarHeight = BarUtils.getStatusBarHeight();
            if (null != viewGroup) {
                ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
                if (null != layoutParams) {
                    layoutParams.height = originalHeight + statusBarHeight;
                    viewGroup.setPadding(0, 0 + statusBarHeight, 0 ,0);
                }
            }
        }
    }


    /**
     * 设置可以/不可以 点击样式
     * @param enable true:原来样式 ; false:不可以点击样式（50%透明度）
     */
    public static void setViewEnableAlphaStyle(View view, boolean enable){
        if (null != view) {
            view.setEnabled(enable);
            view.getBackground().setAlpha(enable ? 255 : 128);
        }
    }

}
