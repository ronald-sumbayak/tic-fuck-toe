package ra.sumbayak.ticfucktoe;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

abstract class SimpleDialog extends AlertDialog.Builder {
    
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
        dialog.setNegativeButton (context.getString (R.string.negative_button), null);
        dialog.show ();
    }
    
    public SimpleDialog (@NonNull Context context, CharSequence message, CharSequence positiveText) {
        super (context);
        setCancelable (false);
        setMessage (message);
        setPositiveButton (positiveText, new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialogInterface, int i) {
                onPositiveButtonClick ();
            }
        });
        setNegativeButton (context.getString (R.string.negative_button), null);
        show ();
    }
    
    abstract void onPositiveButtonClick ();
}
