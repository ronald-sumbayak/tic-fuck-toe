package ra.sumbayak.ticfucktoe;

import android.content.Context;

class ControlDialog {
    
    static void create (Context context, String player, boolean state, Runnable onPositiveButtonClick) {
        SimpleDialog.create (
            context,
            state ? changeToBot (context, player) : takeControl (context, player),
            state ? context.getString (R.string.positive_change_bot) : context.getString (R.string.positive_take_control),
            onPositiveButtonClick
        );
    }
    
    private static String changeToBot (Context context, String player) {
        return context.getString (R.string.dialog_change_bot).replaceAll (context.getString (R.string.player), player);
    }
    
    private static String takeControl (Context context, String player) {
        return context.getString (R.string.dialog_take_control).replaceAll (context.getString (R.string.player), player);
    }
}
