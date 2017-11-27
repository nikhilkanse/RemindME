package com.in.nyk.remindme.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.in.nyk.remindme.R;
import com.in.nyk.remindme.core.RemindMEApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by nikhilkanse on 27/11/17.
 */

public class UIHelper {


    private UIHelper() {

    }

    public static String getUTCDate() {
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = iso8601Format.format(new Date());
        return date;
    }

    public static String getUTCDateFromDate(Date date) {
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return iso8601Format.format(date);
    }

    public static String getOnlyDate(Date date) {
        if (date == null) return "-";
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd MMM, yyyy");
        return dateOnlyFormat.format(date);
    }

    public static String getUTCFormattedDate(String dateToParse) {
        java.text.DateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");
        try {
            Date date = formatter.parse(dateToParse);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Date time = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ+a");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void checkKeyboardIsOpenOrNotAndHideIt() {

        InputMethodManager imm = (InputMethodManager) RemindMEApplication.getAppContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public static void showSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) RemindMEApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) RemindMEApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density));
    }

    // use this method if you want to send current time to server API
    // date will be in .net frameworks DateTime format
    public static String buildIso8601FormattedCurrentDate() {
        String result = null;
        SimpleDateFormat dateFormat = buildIso8601Format();
        result = dateFormat.format(new Date()).toString();
        return result;
    }

    public static String buildIso8601FormattedDateMonthAgo() {
        String result = null;
        SimpleDateFormat dateFormat = buildIso8601Format();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DAY_OF_YEAR, -30);
        result = dateFormat.format(c.getTime()).toString();
        return result;
    }


    // get formatted date from string date in iso8601 format which is used by .net
    public static String getFormattedDate(String date) {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        Date convertedDate = new Date();

        try {
            convertedDate = buildIso8601Format().parse(date);
        } catch (ParseException e) {

        }
        result = sdf.format(convertedDate);
        return result;
    }

    public static String getFormattedStringDate(String date) {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = buildIso8601Format().parse(date);
        } catch (ParseException e) {

        }
        result = sdf.format(convertedDate);
        return result;
    }

    public static Date getLocalDateFromUTCString(String date) {
        if (TextUtils.isEmpty(date))
            return null;
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date convertedDate = new Date();
        try {
            convertedDate = iso8601Format.parse(date);
        } catch (ParseException e) {

        }
        return convertedDate;
    }

    public static String getUserReadableDifferenceFromCurrentDate(String date) {
        Date convertedDate = new Date();
        try {
            convertedDate = buildIso8601Format().parse(date);
        } catch (ParseException e) {
            //TODO: return something, may be blank date ?
        }
        if (convertedDate != null) {
            Calendar today = Calendar.getInstance();
            Calendar inputDate = Calendar.getInstance();
            inputDate.setTime(convertedDate);

            int differenceInYear = today.get(Calendar.YEAR) - inputDate.get(Calendar.YEAR);
            if (differenceInYear == 1) {
                return RemindMEApplication.getAppContext().getResources().getString(R.string.one_year_ago);
            } else if (differenceInYear > 1) {
                return differenceInYear + RemindMEApplication.getAppContext().getResources().getString(R.string.years_ago);
            }

            long difference = System.currentTimeMillis() - convertedDate.getTime();
            long days = difference / (24 * 60 * 60 * 1000);

            if (days == 0) {
                long hourDiff = difference / (60 * 60 * 1000);
                if (hourDiff == 0) {
                    long minDiff = difference / 60000;
                    if (minDiff <= 5) {
                        return "just now";
                    } else {
                        return minDiff + " mins ago";
                    }
                } else {
                    if (hourDiff > 12) {
                        if (hourDiff == 1) {
                            return "an hour ago";
                        }
                        return RemindMEApplication.getAppContext().getResources().getString(R.string.today);
                    } else {
                        return hourDiff + " hours ago";
                    }
                }
            } else if (days > 0 && days <= 7) {
                if (days > 1) {
                    return days + RemindMEApplication.getAppContext().getResources().getString(R.string.days_ago);
                } else {
                    return RemindMEApplication.getAppContext().getResources().getString(R.string.yesterday);
                }

            } else if (days > 7 && days < 30) {
                if (days / 7 == 1) {
                    return RemindMEApplication.getAppContext().getResources().getString(R.string.last_week);
                } else {
                    return days / 7 + RemindMEApplication.getAppContext().getResources().getString(R.string.weeks_ago);
                }
            } else if (days >= 30) {
                if (days / 30 == 1) {
                    return RemindMEApplication.getAppContext().getResources().getString(R.string.last_month);
                } else {
                    return days / 30 + RemindMEApplication.getAppContext().getResources().getString(R.string.months_ago);
                }
            }
        }
        return date;
    }

    public static String getDuration(String date1, String date2) {
        String durationStr = "";
        if (date1 == null) return "--";
        if (date2 == null) return "--";
        Date localDate1 = getLocalDateFromUTCString(date1);
        Date localDate2 = getLocalDateFromUTCString(date2);

        if (localDate1.before(localDate2)) {
            Date localDateTemp = localDate1;
            localDate1 = localDate2;
            localDate2 = localDateTemp;
        }

        long diff = localDate1.getTime() - localDate2.getTime();

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long acthours = minutes / 60;

        long mindiff = diff - acthours * 60 * 60 * 1000;

        long diffseconds = mindiff / 1000;
        long actminutes = diffseconds / 60;

        long diffSec = mindiff - actminutes * 60 * 1000;

        long actSec = diffSec / 1000;

        if (acthours > 0) {
            durationStr = durationStr + acthours + "hr ";
        }
        if (acthours > 0 || actminutes > 0) {
            durationStr = durationStr + actminutes + "min ";
        }

        if (seconds > 0) {
            durationStr = durationStr + actSec + "sec";
        }
        if (durationStr.equalsIgnoreCase("")) {
            durationStr = "--";
        }
        return durationStr;
    }

    // build date formatter for .net DateTime format
    private static SimpleDateFormat buildIso8601Format() {
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return iso8601Format;
    }

    public static String getTodaysUTCDate() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return buildIso8601Format().format(c.getTime().toString());
    }

    public String getTomorrowDate(){
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeZone(TimeZone.getTimeZone("UTC"));
        gc.add(Calendar.DATE, 1);
        return buildIso8601Format().format(gc.getTime().toString());
    }
}
