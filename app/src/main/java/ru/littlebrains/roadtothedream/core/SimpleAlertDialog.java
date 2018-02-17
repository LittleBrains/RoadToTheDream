package ru.littlebrains.roadtothedream.core;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


/**
 * Created by evgeniy on 30.10.2017.
 */

public class SimpleAlertDialog {

    public static AlertDialog show(Context context,
                            String message,
                            DialogInterface.OnClickListener positive,
                            DialogInterface.OnClickListener negative){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ok", positive);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "отмена", negative);
        alertDialog.show();
        return alertDialog;
    }

    public static AlertDialog show(Context context,
                            String message,
                            DialogInterface.OnClickListener positive){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ok", positive);
        alertDialog.show();
        return alertDialog;
    }

}
