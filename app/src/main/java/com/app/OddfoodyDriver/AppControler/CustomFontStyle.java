package com.app.OddfoodyDriver.AppControler;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public class CustomFontStyle {

    public static void replaceDefaultFount(Context context){
        Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), "Hind-Regular.ttf");
        replaceFont("DEFAULT",customFontTypeface);
    }

    private static void replaceFont(String nameofBeingReplaced, Typeface customFontTypeface) {
        try {
            Field myfield = Typeface.class.getDeclaredField(nameofBeingReplaced);
            myfield.setAccessible(true);
            myfield.set(null,customFontTypeface);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
