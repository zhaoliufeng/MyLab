package me.zhaoliufeng.toolslib;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by zhaol on 2017/9/11.
 */

public class ViewSizeUtil {
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }
}
