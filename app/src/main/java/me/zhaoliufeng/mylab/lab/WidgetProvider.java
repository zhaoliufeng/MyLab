package me.zhaoliufeng.mylab.lab;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import me.zhaoliufeng.mylab.R;

public class WidgetProvider extends AppWidgetProvider {
    public static final String CLICK_ACTION = "me.zhaoliufeng.mylab.lab.action.CLICK"; // 点击事件的广播ACTION
    public static final String CLICK_CIRCLE_ACTION = "me.zhaoliufeng.mylab.lab.action.CIRCLE_CLICK";
    /**
     * 每次窗口小部件被更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        Intent intent = new Intent(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, R.id.img_logo, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_logo, pendingIntent);

        intent = new Intent(CLICK_CIRCLE_ACTION);
        pendingIntent = PendingIntent.getBroadcast(context, R.id.img_circle, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_circle, pendingIntent);

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (CLICK_ACTION.equals(intent.getAction())) {
            Toast.makeText(context, "hello!", Toast.LENGTH_SHORT).show();
        }

        if (CLICK_CIRCLE_ACTION.equals(intent.getAction())){
            Toast.makeText(context, "circle!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Toast.makeText(context, "被添加到桌面上了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Toast.makeText(context, "被移除了", Toast.LENGTH_SHORT).show();
    }
}
