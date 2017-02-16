package com.ansqun.example.clock.util;

import android.content.Context;

/**
 * Created by ansqun on 2017/2/15.
 */
public class SizeUtil {
    public static float Dp2Px(Context context, float value) {
        float scale = context.getResources().getDisplayMetrics().density;//获得当前屏幕密度
        return value * scale + 0.5f;
    }

    public static float Sp2Px(Context context, float value) {
        return Dp2Px(context, value);
    }

    public static float Px2Dp(Context context, int value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return value / scale + 0.5f;
    }
}
