package fr.deepanse.soywod.deepanse.model;

import android.content.DialogInterface;

/**
 * Created by soywod on 05/02/2015.
 */
abstract public class AlertBox implements DialogInterface.OnClickListener {

    @Override
    public final void onClick(DialogInterface dialog, int which) {
        execute();
        dialog.dismiss();
    }

    abstract public void execute();
}
