package ra.sumbayak.ticfucktoe;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.widget.ProgressBar;

class TransparentProgressDialog {
    
    private static ProgressDialog dialog;
    
    static void create (Context context) {
        if (dialog != null) dialog.cancel ();
        dialog = new ProgressDialog (context);
        dialog.setProgressStyle (ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable (false);
        dialog.show ();
        dialog.setContentView (new ProgressBar (context), new WindowManager.LayoutParams (-2, -2));
        assert dialog.getWindow () != null;
        dialog.getWindow ().setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));
        
        ProgressBar bar = new ProgressBar (context, null, android.R.attr.progressBarStyle);
        bar.getIndeterminateDrawable ().setColorFilter (
            ContextCompat.getColor (context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY
        );
        dialog.getWindow ().setContentView (bar);
    }
    
    static void cancelLast () {
        if (dialog != null) dialog.cancel ();
        dialog = null;
    }
}