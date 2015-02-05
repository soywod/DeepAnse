package fr.deepanse.soywod.deepanse.model;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by soywod on 05/02/2015.
 */
public class AlertBox extends AlertDialog.Builder {

    public AlertBox(Context context) {
        super(context);
    }

    public AlertBox(Context context, int theme) {
        super(context, theme);
    }
}
