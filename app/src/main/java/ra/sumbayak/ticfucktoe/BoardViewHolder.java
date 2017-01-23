package ra.sumbayak.ticfucktoe;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.transitionseverywhere.TransitionManager;

class BoardViewHolder extends RecyclerView.ViewHolder {
    
    private RelativeLayout bg;
    private ImageView ic;
    
    BoardViewHolder (View itemView) {
        super (itemView);
        bg = (RelativeLayout) itemView.findViewById (R.id.background);
        ic = (ImageView) itemView.findViewById (R.id.item);
    }
    
    void bind (final MainActivity context, final int position, int height) {
        int drawable = -1, state = context.states[position/3][position%3];
        if (state < 0) drawable = R.drawable.space;
        else if (state == 0) drawable = R.drawable.o;
        else if (state == 1) drawable = R.drawable.x;
        TransitionManager.beginDelayedTransition ((ViewGroup) itemView);
        ic.setImageDrawable (ContextCompat.getDrawable (context, drawable));
        
        bg.getLayoutParams ().height = height / 3;
        if (position > 5 && height / 3 > 0) bg.getLayoutParams ().height += height % (height / 3);
        if (state < 0) {
            TypedValue outValue = new TypedValue ();
            context.getTheme().resolveAttribute (android.R.attr.selectableItemBackground, outValue, true);
            TransitionManager.beginDelayedTransition ((ViewGroup) itemView);
            bg.setBackground (ContextCompat.getDrawable (context, outValue.resourceId));
            bg.setClickable (true);
            bg.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    context.assign (position);
                    bg.setClickable (false);
                }
            });
        }
        else {
            TransitionManager.beginDelayedTransition ((ViewGroup) itemView);
            bg.setBackground (null);
            bg.setClickable (false);
        }
    }
}
