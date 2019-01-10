package com.tomcat.ocr.idcard;


import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.VectorEnabledTintResources;

/**
 * Created by Android on 2019/1/8.
 */

public class MyResources extends VectorEnabledTintResources {
    public MyResources(@NonNull Context context, @NonNull Resources res) {
        super(context, res);
    }

    @NonNull
    @Override
    public String getString(int id) throws NotFoundException {
        return super.getString(id);
    }

    @NonNull
    @Override
    public String getString(int id, Object... formatArgs) throws NotFoundException {
        return super.getString(id, formatArgs);
    }


    public static boolean shouldBeUsed() {
        return AppCompatDelegate.isCompatVectorFromResourcesEnabled()
                && Build.VERSION.SDK_INT <= MAX_SDK_WHERE_REQUIRED;
    }
}
