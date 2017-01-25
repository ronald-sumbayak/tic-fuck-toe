package ra.sumbayak.ticfucktoe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class BoardAdapter extends RecyclerView.Adapter<BoardViewHolder> {
    
    private final MainActivity context;
    private final int height;
    
    BoardAdapter (MainActivity context, int height) {
        this.context = context;
        this.height = height;
    }
    
    @Override
    public BoardViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from (context).inflate (R.layout.itemview_board, parent, false);
        return new BoardViewHolder (itemView, height);
    }
    
    @Override
    public void onBindViewHolder (BoardViewHolder holder, int position) {
        holder.bind (context);
    }
    
    @Override
    public int getItemCount () {
        return 9;
    }
}
