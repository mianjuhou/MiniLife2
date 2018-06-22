package com.fangdean.minilife.util;

import com.fangdean.minilife.app.App;

class UiUtil {

    private static float density = 0;

    public static int dp2px(float dp) {
        if (density == 0) {
            density = App.getContext().getResources().getDisplayMetrics().density;
        }
        return (int) (dp * density + 0.5f);
    }
}
