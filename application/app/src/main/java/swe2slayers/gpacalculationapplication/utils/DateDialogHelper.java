package swe2slayers.gpacalculationapplication.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import swe2slayers.gpacalculationapplication.views.EditSemester;

public class DateDialogHelper {

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

        dialog.show();
    }
}
