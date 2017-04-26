package com.example.hades.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Hades on 2017/4/9.
 */
public class Utils {
    private static int screenWidth=0;
    private static int screenHeight=0;

    public static int dpToPx(int dp){
        return (int) (dp* Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context context){
        if(screenHeight==0){
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display=windowManager.getDefaultDisplay();
            Point size=new Point();
            display.getSize(size);
            screenHeight=size.y;
        }
        return screenHeight;
    }

    public static int getScreenWidth(Context context) {
        if(screenWidth==0){
            WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display=wm.getDefaultDisplay();
            Point size=new Point();
            display.getSize(size);
            screenWidth=size.x;

        }
        return screenWidth;
    }

    public static boolean isAndroid5(){
        return Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP;
    }
}
