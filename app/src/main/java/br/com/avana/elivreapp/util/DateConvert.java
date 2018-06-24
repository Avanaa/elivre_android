package br.com.avana.elivreapp.util;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.avana.elivreapp.R;

public class DateConvert {

    @SuppressLint("SimpleDateFormat")
    public static String getDateFormatByCalendar(Context context, Calendar date){
        SimpleDateFormat dateformat = new SimpleDateFormat(context.getString(R.string.form_datetime_format));
        return dateformat.format(date.getTime());
    }

    public static String addHourInterval(Context context, String dateString, int min){
        Calendar c = DateConvert.getCalendarByString(context, dateString);
        if (c == null){
            c = Calendar.getInstance();
        }
        c.add(Calendar.HOUR_OF_DAY, -(min - 1));
        return DateConvert.getstringByCalendar(context, c);
    }

    @SuppressLint("SimpleDateFormat")
    private static Calendar getCalendarByString(Context context, String s) {
        if (s == null){ return null; }
        try {
            Date parse = new SimpleDateFormat(context.getString(R.string.form_datetime_format)).parse(s);
            Calendar c = Calendar.getInstance();
            c.setTime(parse);
            return c;
        } catch (ParseException e) {
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private static String getstringByCalendar(Context context, Calendar c){
        if (c == null){ return ""; }
        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.form_datetime_format));
        return format.format(c.getTime());
    }
}
