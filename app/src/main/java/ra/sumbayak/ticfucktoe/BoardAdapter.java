package ra.sumbayak.ticfucktoe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class BoardAdapter extends RecyclerView.Adapter<BoardViewHolder> {
    
    private MainActivity context;
    private int height;
    
    BoardAdapter (MainActivity context) {
        this.context = context;
        height = 0;
    }
    
    @Override
    public BoardViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from (context).inflate (R.layout.itemview_board, parent, false);
        return new BoardViewHolder (itemView);
    }
    
    @Override
    public void onBindViewHolder (BoardViewHolder holder, int position) {
        holder.bind (context, position, height);
    }
    
    @Override
    public int getItemCount () {
        return 9;
    }
    
    void resizeBoard (int height) {
        this.height = height - 1;
        notifyDataSetChanged ();
    }
}
