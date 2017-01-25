package ra.sumbayak.ticfucktoe;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

class SimpleDialog {
    
    static void create (Context context, CharSequence message, CharSequence positiveText, final Runnable onPositiveButtonClick) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder (context);
        builder.setCancelable (false);
        builder.setMessage (message);
        builder.setPositiveButton (positiveText, new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialogInterface, int i) {
                onPositiveButtonClick.run ();
            }
        });
        builder.setNegativeButton (context.getString (R.string.negative_default), null);
        builder.show ();
    }
    
    static void create (Context context, CharSequence message, CharSequence positiveText, final CharSequence negativeText, final Runnable onPositiveButtonClick) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder (context);
        builder.setCancelable (false);
        builder.setMessage (message);
        builder.setPositiveButton (positiveText, new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialogInterface, int i) {
                onPositiveButtonClick.run ();
            }
        });
        builder.setNegativeButton (negativeText, null);
        builder.show ();
    }
}
