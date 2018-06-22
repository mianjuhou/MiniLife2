package com.fangdean.minilife.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fangdean.minilife.app.App;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fda on 2017/7/14.
 */

public class PopUtil {
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public PopupWindow pop;
    private OnShowBeforeListener showBeforeListener;

    /**
     * 默认宽高都为MATCH_PARENT
     */
    public static PopUtil init() {
        PopUtil popUtil = new PopUtil();
        popUtil.pop = new PopupWindow(App.getContext());
        //设置焦点事件
        popUtil.pop.setFocusable(true);
        //设置背景色
        popUtil.pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //显示宽高
        popUtil.pop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popUtil.pop.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        return popUtil;
    }


    /**
     * 设置背景颜色
     */
    public PopUtil setBackgroundDrawable(@ColorInt int color) {
        pop.setBackgroundDrawable(new ColorDrawable(color));
        return this;
    }

    /**
     * 设置阴影
     */
    public PopUtil setElevation(float dp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pop.setElevation(UiUtil.dp2px(dp));
        }
        return this;
    }

    /**
     * 设置是否自动获取焦点，获取后点击其他区域自动消失
     *
     * @param focusable
     * @return
     */
    public PopUtil setFocusable(boolean focusable) {
        pop.setFocusable(focusable);
        return this;
    }

    /**
     * 设置宽
     *
     * @param width
     * @return
     */
    public PopUtil setWidth(int width) {
        if (width == MATCH_PARENT || width == WRAP_CONTENT) {
            pop.setWidth(width);
        } else {
            pop.setWidth(UiUtil.dp2px(width));
        }
        return this;
    }

    /**
     * 设置高
     *
     * @param height
     * @return
     */
    public PopUtil setHeight(int height) {
        if (height == MATCH_PARENT || height == WRAP_CONTENT) {
            pop.setHeight(height);
        } else {
            pop.setHeight(UiUtil.dp2px(height));
        }
        return this;
    }

    /**
     * 设置动画
     *
     * @param animationStyle R.style.popwin_anim_style
     * @return
     */
    public PopUtil setAnimationStyle(int animationStyle) {
        pop.setAnimationStyle(animationStyle);
        return this;
    }

    public interface OnShowBeforeListener {
        void onShow(PopupWindow pop);
    }

    /**
     * 显示前的操作
     *
     * @return
     */
    public PopUtil setShowListener(OnShowBeforeListener listener) {
        showBeforeListener = listener;
        return this;
    }

    /**
     * 消失后触发的事件
     *
     * @return
     */
    public PopUtil setCloseListener(PopupWindow.OnDismissListener listener) {
        pop.setOnDismissListener(listener);
        return this;
    }

    public PopUtil setPopView(View popView) {
        pop.setContentView(popView);
        return this;
    }

    /**
     * 默认底部显示,不偏移
     *
     * @return
     */
    public PopUtil showAtLocation(View parentView) {
        if (showBeforeListener != null) {
            showBeforeListener.onShow(pop);
        }
        pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        return this;
    }

    /**
     * 指定位置，不偏移
     *
     * @return
     */
    public PopUtil showAtLocation(int gravity, View parentView) {
        if (showBeforeListener != null) {
            showBeforeListener.onShow(pop);
        }
        pop.showAtLocation(parentView, gravity, 0, 0);
        return this;
    }

    /**
     * @param gravity
     * @param xoff       dp
     * @param yoff       dp
     * @param parentView
     * @return
     */
    public PopUtil showAtLocation(int gravity, float xoff, float yoff, View parentView) {
        if (showBeforeListener != null) {
            showBeforeListener.onShow(pop);
        }
        pop.showAtLocation(parentView, gravity, UiUtil.dp2px(xoff), UiUtil.dp2px(yoff));
        return this;
    }

    public PopUtil showAsDropdown(View parentView) {
        if (showBeforeListener != null) {
            showBeforeListener.onShow(pop);
        }
        pop.showAsDropDown(parentView);
        return this;
    }

    public PopUtil showAsDropdown(View parentView, float xoff, float yoff) {
        if (showBeforeListener != null) {
            showBeforeListener.onShow(pop);
        }
        int xo = UiUtil.dp2px(xoff);
        int yo = UiUtil.dp2px(yoff);
        if (xoff < 0) {
            xo = -UiUtil.dp2px(Math.abs(xoff));
        }
        if (yoff < 0) {
            yo = -UiUtil.dp2px(Math.abs(yoff));
        }
        pop.showAsDropDown(parentView, xo, yo);
        return this;
    }

    public PopUtil showAsDropdownInt(View parentView, int xoff, int yoff) {
        if (showBeforeListener != null) {
            showBeforeListener.onShow(pop);
        }
        pop.showAsDropDown(parentView, xoff, yoff);
        return this;
    }

    public PopUtil showAsDropdown(View parentView, int gravity) {
        if (showBeforeListener != null) {
            showBeforeListener.onShow(pop);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pop.showAsDropDown(parentView, 0, 0, gravity);
        } else {
            showAsDropdown(parentView, 0, 0);
        }
        return this;
    }

    public PopUtil showAsDropdown(View parentView, float xoff, float yoff, int gravity) {
        if (showBeforeListener != null) {
            showBeforeListener.onShow(pop);
        }
        int xo = UiUtil.dp2px(xoff);
        int yo = UiUtil.dp2px(yoff);
        if (xoff < 0) {
            xo = -UiUtil.dp2px(Math.abs(xoff));
        }
        if (yoff < 0) {
            yo = -UiUtil.dp2px(Math.abs(yoff));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pop.showAsDropDown(parentView, xo, yo, gravity);
        } else {
            showAsDropdown(parentView, xoff, yoff);
        }
        return this;
    }

    public void dismiss() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }

    /**
     * 用于填充View
     *
     * @param layout
     * @return
     */
    public static View inflate(int layout) {
        return View.inflate(App.getContext(), layout, null);
    }
}
