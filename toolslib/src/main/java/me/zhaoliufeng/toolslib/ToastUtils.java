package me.zhaoliufeng.toolslib;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast封装工具类
 */

public class ToastUtils {

    public static void showToast(Context context, String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

}
