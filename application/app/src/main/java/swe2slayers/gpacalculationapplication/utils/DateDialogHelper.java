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

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import swe2slayers.gpacalculationapplication.views.EditSemester;

import static java.util.Calendar.SUNDAY;

public class DateDialogHelper {

    /**
     * Function that shows a date picker dialog given some criterion
     * @param context The context from which this function is called (i.e. the activity)
     * @param editText The edit text that was clicked to show the dialog
     * @param currentDate The default date to set for the dialog (defaults to current date)
     * @param start The minimum date to limit the selection to (can be null)
     * @param end the maximum date to limit the selection to (can be null)
     */
    public static void showDateDialog(Context context, final TextInputEditText editText, Date currentDate, Date start, Date end){

        Calendar c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        if(currentDate != null && currentDate.getYear() != -1) {
            dialog.updateDate(currentDate.getYear(), currentDate.getMonth()-1, currentDate.getDay());
        }

        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            java.util.Date d;
            if(start != null && start.getYear() != -1) {
                d = myFormat.parse(start.toString());
                dialog.getDatePicker().setMinDate(d.getTime());
            }
            if (end != null && end.getYear() != -1){
                d = myFormat.parse(end.toString());
                dialog.getDatePicker().setMaxDate(d.getTime());
            }
        }catch (Exception e){}

        // By user request
        if (Build.VERSION.SDK_INT >= 21) {
            dialog.getDatePicker().setFirstDayOfWeek(SUNDAY);
        }
        dialog.show();
    }
}
