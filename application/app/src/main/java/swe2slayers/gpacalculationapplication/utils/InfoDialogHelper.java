/*
 * Copyright (c) 2018. Software Engineering Slayers
 *
 * Azel Daniel (816002285)
 * Amanda Seenath (816002935)
 * Christopher Joseph (814000605)
 * Michael Bristol (816003612)
 * Maya Bannis (816000144)
 *
 * COMP 3613
 * Software Engineering II
 *
 * GPA Calculator Project
 */

package swe2slayers.gpacalculationapplication.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import swe2slayers.gpacalculationapplication.R;

public class InfoDialogHelper {

    public static void showInfoDialog(Context context, String title, String message){
        View view = LayoutInflater.from(context).inflate(R.layout.info_header, null);
        TextView titleText = (TextView) view.findViewById(R.id.title);
        final TextView messageText = (TextView) view.findViewById(R.id.message);
        titleText.setText(title + " Guide");
        messageText.setMovementMethod(new ScrollingMovementMethod());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            messageText.setText(Html.fromHtml(message,Html.FROM_HTML_MODE_LEGACY));
        } else {
            messageText.setText(Html.fromHtml(message));
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                messageText.scrollTo(0,0);
            }
        }, 100);

        new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
