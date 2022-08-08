package com.ambitious.fghdoctor.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ambitious.fghdoctor.R;
import com.google.android.material.snackbar.Snackbar;


public class CustomSnakbar {

    @SuppressLint("NewApi")
    public static void showSnakabar(Context context, View v, String msg){

        Snackbar snackbar;
        snackbar = Snackbar.make(v,msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.colorBase));
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setMaxLines(3);
        snackbar.getView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        snackbar.show();
    }

    @SuppressLint("NewApi")
    public static void showDarkSnakabar(Context context, View v, String msg) {

        Snackbar snackbar;
        snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.colorBase));
        TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.colorWhite));
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setMaxLines(3);
        snackbar.getView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        snackbar.show();
    }

}
