package com.cl.testapp.widget.test;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.cl.testapp.R;
import com.cl.testapp.util.Util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 桌面部件
 * Created by Administrator on 2016-12-26.
 */

public class TestWidgetProvider extends AppWidgetProvider {

    // 启动ExampleAppWidgetService服务对应的action
    private final Intent EXAMPLE_SERVICE_INTENT = new Intent("android.appwidget.action.XL");
    // 更新 widget 的广播对应的action
    private final String ACTION_START = "com.cl.testapp.action.start";
    private final String ACTION_REFESH = "com.cl.testapp.action.refresh";
    // 保存 widget 的id的HashSet，每新建一个 widget 都会为该 widget 分配一个 id。
    private static Set idsSet = new HashSet();
    // 按钮信息
    private static final int BUTTON_SHOW = 1;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        //创建第一个Widget时调用
        context.startService(Util.getExplicitIntent(context, EXAMPLE_SERVICE_INTENT));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        //首次添加或大小改变时调用
    }

    @Override
    public void onDisabled(Context context) {
        //删除最后一个Widget调用
        context.stopService(Util.getExplicitIntent(context, EXAMPLE_SERVICE_INTENT));
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //Widget的一个实例被删除时调用
        for (int appWidgetId: appWidgetIds) {
            idsSet.remove(appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        //当Widget需要更新它的View时调用
        for (int appWidgetId: appWidgetIds) {
            idsSet.add(appWidgetId);
        }
        /*
         * 构造pendingIntent发广播，onReceive()根据收到的广播，更新
         */
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.test_widget);
        Intent refreshIntent = new Intent().setAction(ACTION_REFESH);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.start_stop, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, rv);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("xl" , intent.getAction());
        //接受广播的回调
        if (intent.getAction().equals(ACTION_START)) {
//            updateWidget(context);
        }else if (intent.getAction().equals(ACTION_REFESH)) {
            // 按钮点击广播
            Util.toastShow(context, intent.getAction());
            if (!Util.isServiceWork(context, "com.cl.testapp.widget.test.TestWidgetService")){
                context.startService(Util.getExplicitIntent(context, EXAMPLE_SERVICE_INTENT));
            }
        }
        super.onReceive(context, intent);
    }

    private void updateWidget(Context context) {
        int appID;
        for (Object anIdsSet : idsSet) {
            appID = (Integer) anIdsSet;
            // 获得随机数
            int index = (new Random().nextInt(5));
            // 获取 example_appwidget.xml 对应的RemoteViews
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.test_widget);
            // 设置显示图片
            remoteView.setTextViewText(R.id.counter, String.valueOf(index));
            // 设置点击按钮对应的PendingIntent：即点击按钮时，发送广播。
            Intent refreshIntent = new Intent().setAction(ACTION_REFESH);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.start_stop, pendingIntent);
            // 更新 widget
            AppWidgetManager.getInstance(context).updateAppWidget(appID, remoteView);
        }
    }



}
