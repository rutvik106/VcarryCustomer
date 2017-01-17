package io.fusionbit.vcarrycustomer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.inputmethod.InputMethodManager;

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

    public static void hideSoftKeyboard(Activity activity)
    {
        if (activity.getCurrentFocus() != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

}
