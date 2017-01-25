package ra.sumbayak.ticfucktoe;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

class EndGameDialog {
    
    private static AlertDialog dialog;
    
    static void create (Context context, int winner, boolean o, boolean x, final Runnable onPositiveButtonClick, final Runnable onNegativeButtonClick) {
        if (dialog != null) dialog.cancel ();
        AlertDialog.Builder builder = new AlertDialog.Builder (context);
        builder.setMessage (title (context, winner, o, x));
        builder.setCancelable (false);
        builder.setPositiveButton (context.getString (R.string.positive_game_over), new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                onPositiveButtonClick.run ();
                EndGameDialog.dialog = null;
            }
        });
        builder.setNegativeButton (context.getString (R.string.negative_game_over), new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                onNegativeButtonClick.run ();
            }
        });
        builder.show ();
        dialog = builder.create ();
    }
    
    private static String title (Context context, int winner, boolean controlO, boolean controlX) {
        if (winner == 0 && controlX && controlO) return context.getString (R.string.dialog_o_wins);
        else if (winner == 1 && controlO && controlX) return context.getString (R.string.dialog_x_wins);
        else if (winner == 2) return context.getString (R.string.dialog_draw);
        else return context.getString (R.string.dialog_bot_wins);
    }
    
    static void showLast () {
        if (dialog != null) dialog.show ();
    }
}
