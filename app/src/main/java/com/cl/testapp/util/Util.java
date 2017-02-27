package com.cl.testapp.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

/**
 * 工具
 * Created by Administrator on 2016-09-06.
 */
public class Util {

    private static Toast toast;

    public Util() {
    }

    /**
     * 弹出单例吐司
     * @param content Context
     * @param info String
     */
    public static void toastShow(Context content, String info) {
        if (toast == null) {
            toast = Toast.makeText(content, info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
    }

    /**
     * 获取屏幕宽度
     * @param context Context
     * @return Width
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * dp2px
     * @param context Context
     * @param dip dp
     * @return px
     */
    public static int dip2px(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + .5f);
    }


    /**
     * 判断某个服务是否正在运行的方法
     * @param mContext Context
     * @param serviceName 是包名+服务的类名
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 获取显式意图
     * @param context Context
     * @param implicitIntent 隐式意图
     * @return 显式意图
     */
    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}
