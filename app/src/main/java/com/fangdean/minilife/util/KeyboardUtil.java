package com.fangdean.minilife.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fangdean.minilife.R;

import java.lang.reflect.Field;

/**
 * Created by fda on 2017/8/9.
 */

public class KeyboardUtil {
    /**
     * 关闭activity中打开的键盘
     *
     * @param activity
     */
    public static void closeKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }

        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 关闭dialog中打开的键盘
     *
     * @param dialog
     */
    public static void closeKeyboard(Dialog dialog) {
        View view = dialog.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 打开键盘
     *
     * @param editText
     */
    public static void openKeyboard(final EditText editText) {
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                editText.setSelection(editText.getText().toString().length());
                InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 10);
    }

    public static int getKeyboardHeight(Activity activity) {
        final Context context = activity.getApplicationContext();
        final RelativeLayout parentLayout = (RelativeLayout) activity.findViewById(R.id.parent);
        final View myLayout = activity.getWindow().getDecorView();
        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                parentLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = myLayout.getRootView().getHeight();
                int heightDiff = screenHeight - (r.bottom - r.top);
                if (heightDiff > 100)
//                    int statusBarHeight = 0;
                    try {
                        Class<?> c = Class.forName("com.android.internal.R$dimen");
                        Object obj = c.newInstance();
                        Field field = c.getField("status_bar_height");
                        int x = Integer.parseInt(field.get(obj).toString());
//                    statusBarHeight = context.getResources().getDimensionPixelSize(x);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                int realKeyboardHeight = heightDiff - statusBarHeight;
            }
        });

        return 0;
    }
}
