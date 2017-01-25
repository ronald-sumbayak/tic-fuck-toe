package ra.sumbayak.ticfucktoe;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;

class Controller {
    
    private static final int[] points = {R.id.point_o, R.id.point_x};
    private static final int[] selections = {R.id.selection_o, R.id.selection_x};
    private static final int[] states = {R.id.state_o, R.id.state_x};
    
    private Controller opponent;
    private Context context;
    private boolean control = false;
    private int point = 0;
    private TextView tvPoint;
    private View selection, state;
    
    Controller (final MainActivity context, final int player) {
        this.context = context;
        tvPoint = (TextView) context.findViewById (points[player]);
        selection = context.findViewById (selections[player]);
        state = context.findViewById (states[player]);
    }
    
    void sync (Controller opponent) {
        this.opponent = opponent;
    }
    
    void setSelectionOnClickListener (@Nullable View.OnClickListener listener) {
        selection.setOnClickListener (listener);
    }
    
    void reset () {
        point = 0;
        tvPoint.setText (String.valueOf (point));
    }
    
    void switchControl () {
        if (control) changeToBot ();
        else takeControl ();
    }
    
    void takeControl () {
        control = true;
        TransitionManager.beginDelayedTransition ((ViewGroup) state.getParent ());
        state.setBackground (ContextCompat.getDrawable (context, android.R.color.holo_green_light));
        selection.setClickable (!(control && !opponent.isControlled ()));
        if (opponent.isControlled ()) opponent.setChangeable (!(opponent.isControlled () && !control));
    }
    
    private void changeToBot () {
        control = false;
        TransitionManager.beginDelayedTransition ((ViewGroup) state.getParent ());
        state.setBackground (ContextCompat.getDrawable (context, android.R.color.transparent));
        selection.setClickable (!(control && !opponent.isControlled ()));
        if (opponent.isControlled ()) opponent.setChangeable (!(opponent.isControlled () && !control));
    }
    
    private void setChangeable (boolean changeable) {
        selection.setClickable (changeable);
    }
    
    void win () {
        point++;
        TransitionManager.beginDelayedTransition ((ViewGroup) tvPoint.getParent ());
        tvPoint.setText (String.valueOf (point));
    }
    
    boolean isControlled () {
        return control;
    }
}
