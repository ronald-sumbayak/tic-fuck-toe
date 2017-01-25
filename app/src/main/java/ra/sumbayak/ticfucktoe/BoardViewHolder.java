package ra.sumbayak.ticfucktoe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.transitionseverywhere.TransitionManager;

class BoardViewHolder extends RecyclerView.ViewHolder {
    
    private final ImageView ic;
    private final RelativeLayout bg;
    
    BoardViewHolder (View itemView, int height) {
        super (itemView);
        ic = (ImageView) itemView.findViewById (R.id.ic);
        bg = (RelativeLayout) itemView.findViewById (R.id.background);
        resizeItem (height);
    }
    
    void bind (final MainActivity context) {
        int state = context.state (getLayoutPosition ());
        ic.setImageDrawable (ContextCompat.getDrawable (context, icon (state)));
        
        bg.setOnClickListener (state < 0 ? new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                context.assign (getLayoutPosition ());
            }
        } : null);
    
        bg.setBackground (state < 0 ? selectableItemBackground (context) : null);
        bg.setClickable (state < 0);
    }
    
    private int icon (int state) {
        if (state == 0) return R.drawable.o;
        else if (state == 1) return R.drawable.x;
        else return R.drawable.space;
    }
    
    private Drawable selectableItemBackground (Context context) {
        TypedValue outValue = new TypedValue ();
        context.getTheme().resolveAttribute (android.R.attr.selectableItemBackground, outValue, true);
        TransitionManager.beginDelayedTransition ((ViewGroup) itemView);
        return ContextCompat.getDrawable (context, outValue.resourceId);
    }
    
    private void resizeItem (int height) {
        int newHeight = height / 3;
        if (getLayoutPosition () > 5) newHeight += height % (height / 3);
        bg.getLayoutParams ().height = newHeight;
    }
}
