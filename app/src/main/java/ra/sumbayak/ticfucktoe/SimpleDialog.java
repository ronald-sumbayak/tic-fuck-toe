package ra.sumbayak.ticfucktoe;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

class SimpleDialog{
    
    static void create (Context context, CharSequence message, CharSequence positiveText, final Runnable onPositiveButtonClick) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder (context);
        dialog.setCancelable (false);
        dialog.setMessage (message);
        dialog.setPositiveButton (positiveText, new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialogInterface, int i) {
                onPositiveButtonClick.run ();
            }
        });
        dialog.setNegativeButton (context.getString (R.string.negative_default), null);
        dialog.show ();
    }
    
    static void create (Context context, CharSequence message, CharSequence positiveText, CharSequence negativeText, final Runnable onPositiveButtonClick) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder (context);
        dialog.setCancelable (false);
        dialog.setMessage (message);
        dialog.setPositiveButton (positiveText, new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialogInterface, int i) {
                onPositiveButtonClick.run ();
            }
        });
        dialog.setNegativeButton (negativeText, null);
        dialog.show ();
    }
}
