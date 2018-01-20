package me.zhaoliufeng.mylab;

import android.app.Application;
import android.util.Log;

/**
 * Created by zhaol on 2017/12/29.
 */

public class LabApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("applicationLog", "onCreate");
    }

}
