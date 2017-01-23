package ra.sumbayak.ticfucktoe;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;

public class MainActivity extends AppCompatActivity {
    
    public Drawable[] colorStates = new Drawable[2];
    public int[][] states = new int[3][3];
    public RecyclerView board;
    private BoardAdapter adapter;
    private int turn, pointO, pointX;
    private boolean controlO = true, controlX = false;
    private Context context = this;
    private TextView tvPointO, tvPointX;
    private View endGameConfirm, selectionO, selectionX, stateO, stateX;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView (R.layout.activity_main);
        init ();
        setupView ();
        setupBoard ();
        resetGame ();
    }
    
    private void init () {
        Bot.init (this);
        colorStates[0] = ContextCompat.getDrawable (this, android.R.color.transparent);
        colorStates[1] = ContextCompat.getDrawable (this, android.R.color.holo_green_light);
    }
    
    private void setupView () {
        tvPointO = (TextView) findViewById (R.id.point_o);
        tvPointX = (TextView) findViewById (R.id.point_x);
        selectionO = findViewById (R.id.selection_o);
        selectionX = findViewById (R.id.selection_x);
        stateO = findViewById (R.id.state_o);
        stateX = findViewById (R.id.state_x);
        endGameConfirm = findViewById (R.id.end_game_confirm);
        endGameConfirm.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                EndGameDialog.showLast ();
                endGameConfirm.setVisibility (View.GONE);
            }
        });
        ImageButton reset = (ImageButton) findViewById (R.id.reset);
        reset.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                SimpleDialog.create (context, getString (R.string.dialog_reset), getString (R.string.positive_reset), new Runnable () {
                    @Override
                    public void run () {
                        resetGame ();
                    }
                });
            }
        });
        final RelativeLayout panel = (RelativeLayout) findViewById (R.id.panel);
        panel.getViewTreeObserver ().addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener () {
            @Override
            public void onGlobalLayout () {
                panel.getViewTreeObserver ().removeOnGlobalLayoutListener (this);
                int height = panel.getMeasuredHeight ();
                findViewById (R.id.players_selection).getLayoutParams ().height = height;
                findViewById (R.id.players_state).getLayoutParams ().height = height;
                refreshView ();
            }
        });
    }
    
    private void setupBoard () {
        board = (RecyclerView) findViewById (R.id.board);
        board.setLayoutManager (new GridLayoutManager (this, 3) {
            @Override
            public boolean canScrollVertically () {
                return false;
            }
        });
        board.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.VERTICAL));
        board.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.HORIZONTAL));
        board.getViewTreeObserver ().addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener () {
            @Override
            public void onGlobalLayout () {
                board.getViewTreeObserver ().removeOnGlobalLayoutListener (this);
                adapter.resizeBoard (board.getMeasuredHeight ());
            }
        });
        adapter = new BoardAdapter (this);
        board.setAdapter (adapter);
    }
    
    private void resetGame () {
        pointO = pointX = 0;
        resetBoard ();
    }
    
    private void resetBoard () {
        for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) states[i][j] = (i * (-3)) - j - 1;
        turn = 0;
        Bot.prev = "";
        refreshView ();
        if (!controlO) botMove ();
    }
    
    private void refreshView () {
        com.transitionseverywhere.TransitionManager.beginDelayedTransition ((ViewGroup) findViewById (R.id.panel));
        tvPointO.setText (String.valueOf (pointO));
        com.transitionseverywhere.TransitionManager.beginDelayedTransition ((ViewGroup) findViewById (R.id.panel));
        tvPointX.setText (String.valueOf (pointX));
        com.transitionseverywhere.TransitionManager.beginDelayedTransition ((ViewGroup) findViewById (R.id.panel));
        stateO.setBackground (controlO ? colorStates[1] : colorStates[0]);
        com.transitionseverywhere.TransitionManager.beginDelayedTransition ((ViewGroup) findViewById (R.id.panel));
        stateX.setBackground (controlX ? colorStates[1] : colorStates[0]);
        selectionO.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                ControlDialog.create (context, "O", controlO && controlX, new Runnable () {
                    @Override
                    public void run () {
                        controlO = !controlO;
                        resetGame ();
                    }
                });
            }
        });
        selectionO.setClickable (!(controlO && !controlX));
        selectionX.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                ControlDialog.create (context, "X", controlO && controlX, new Runnable () {
                    @Override
                    public void run () {
                        controlX = !controlX;
                        resetGame ();
                    }
                });
            }
        });
        selectionX.setClickable (!(controlX && !controlO));
        adapter.notifyDataSetChanged ();
    }
    
    public void assign (int position) {
        Bot.prev += position;
        states[position/3][position%3] = turn++ % 2;
        adapter.notifyDataSetChanged ();
        int winner = qualifyTurn ();
        
        if (winner > -1) gameOver (winner);
        else if (!((turn % 2 == 0 && controlO) || (turn % 2 != 0 && controlX))) botMove ();
    }
    
    public void botMove () {
        TransparentProgressDialog.create (this);
        Handler handler = new Handler ();
        handler.postDelayed (new Runnable () {
            @Override
            public void run () {
                TransparentProgressDialog.cancelLast ();
                assign (Bot.move (states, turn));
            }
        }, 1500);
    }
    
    private int qualifyTurn () {
        int result;
        result = alreadyWin ();
        if (result >= 0) return result;
        else return draw ();
    }
    
    private int alreadyWin () {
        for (int i = 0; i < 3; i++) if (states[i][0] == states[i][1] && states[i][1] == states[i][2]) return states[i][1];
        for (int i = 0; i < 3; i++) if (states[0][i] == states[1][i] && states[1][i] == states[2][i]) return states[1][i];
        if (states[0][0] == states[1][1] && states[1][1] == states[2][2]) return states[1][1];
        else if (states[0][2] == states[1][1] && states[1][1] == states[2][0]) return states[1][1];
        return -1;
    }
    
    private int draw () {
        for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) if (states[i][j] < 0) return -1;
        return 2;
    }
    
    private void gameOver (int winner) {
        if (winner == 0) pointO++;
        else if (winner == 1) pointX++;
        EndGameDialog.create (this, winner, controlO, controlX, new Runnable () {
            @Override
            public void run () {
                resetBoard ();
            }
        }, new Runnable () {
            @Override
            public void run () {
                endGameConfirm.setVisibility (View.VISIBLE);
            }
        });
        refreshView ();
    }
}
