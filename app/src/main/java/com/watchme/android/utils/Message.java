package com.watchme.android.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hayru on 11/11/2016.
 */
public class Message {
    public static void message(Context context, String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show();
    }
}
