package io.fusionbit.vcarrycustomer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by rutvik on 1/17/2017 at 9:39 AM.
 */

public class Utils
{

    public static void showSimpleAlertBox(final Context context, final String message,
                                          final DialogInterface.OnClickListener onClickListener)
    {
        new AlertDialog.Builder(context)
                .setTitle(R.string.simple_alert_title)
                .setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .show();
    }

    public static void dialNumber(final Context context,final  String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    public static void hideSoftKeyboard(Activity activity)
    {
        if (activity.getCurrentFocus() != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static String getDate(Date d)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        return sdf.format(d);
    }

    public static boolean isDeviceOnline(Context context)
    {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static Date convertToDate(String stringDate)
    {
        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try
        {
            date = sdf.parse(stringDate);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return date;
    }

    public static void ShareApp(final Activity activity)
    {
        try
        {
            int applicationNameId = activity.getApplicationInfo().labelRes;
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, activity.getString(applicationNameId));
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            activity.startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e)
        {
            //e.toString();
        }
    }

    public static void sendFeedback(Activity activity)
    {
        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"sales@vcarry.co.in"});
        Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        Email.putExtra(Intent.EXTRA_TEXT, "Dear ...," + "");
        activity.startActivity(Intent.createChooser(Email, "Send Feedback:"));
    }

}
