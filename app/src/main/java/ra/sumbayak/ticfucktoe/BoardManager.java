package ra.sumbayak.ticfucktoe;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

class BoardManager {
    
    private int[][] states = new int[3][3];
    private MainActivity context;
    private RecyclerView board;
    private BoardAdapter adapter;
    
    BoardManager (MainActivity context) {
        this.context = context;
        setupRV ();
    }
    
    private void setupRV () {
        board = (RecyclerView) context.findViewById (R.id.board);
        board.setLayoutManager (new GridLayoutManager (context, 3) {
            @Override
            public boolean canScrollVertically () {
                return false;
            }
        });
        board.addItemDecoration (new DividerItemDecoration (context, DividerItemDecoration.VERTICAL));
        board.addItemDecoration (new DividerItemDecoration (context, DividerItemDecoration.HORIZONTAL));
        board.getViewTreeObserver ().addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener () {
            @Override
            public void onGlobalLayout () {
                board.getViewTreeObserver ().removeOnGlobalLayoutListener (this);
                adapter = new BoardAdapter (context, board.getMeasuredHeight ());
                board.setAdapter (adapter);
            }
        });
    }
    
    void reset () {
        for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) states[i][j] = (i * (-3)) - j - 1;
        if (adapter == null) return;
        for (int i = 0; i < 9; i++) adapter.notifyItemChanged (i);
    }
    
    void assign (int position, int turn) {
        states[position/3][position%3] = turn % 2;
        adapter.notifyItemChanged (position);
    }
    
    int[][] states () {
        return states;
    }
}
