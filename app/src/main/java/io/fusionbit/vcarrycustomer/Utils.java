package io.fusionbit.vcarrycustomer;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

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

}
