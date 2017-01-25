package ra.sumbayak.ticfucktoe;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    
    private int turn;
    private BoardManager board;
    private Controller o, x;
    private View endGameConfirm;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView (R.layout.activity_main);
        init ();
        setupView ();
        resetGame ();
    }
    
    private void init () {
        board = new BoardManager (this);
        o = new Controller (this, 0);
        x = new Controller (this, 1);
        o.sync (x);
        x.sync (o);
        Bot.init (this);
    }
    
    private void setupView () {
        endGameConfirm = findViewById (R.id.end_game_confirm);
        endGameConfirm.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                EndGameDialog.showLast ();
                endGameConfirm.setVisibility (View.GONE);
            }
        });
        
        findViewById (R.id.reset).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                SimpleDialog.create (MainActivity.this, getString (R.string.dialog_reset), getString (R.string.positive_reset), new Runnable () {
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
                panel.requestLayout ();
            }
        });
        
        o.setSelectionOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                ControlDialog.create (MainActivity.this, "O", o.isControlled () && x.isControlled (), new Runnable () {
                    @Override
                    public void run () {
                        o.switchControl ();
                        resetGame ();
                    }
                });
            }
        });
        o.takeControl ();
        x.setSelectionOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                ControlDialog.create (MainActivity.this, "X", o.isControlled () && x.isControlled (), new Runnable () {
                    @Override
                    public void run () {
                        x.switchControl ();
                        resetGame ();
                    }
                });
            }
        });
    }
    
    void resetGame () {
        o.reset ();
        x.reset ();
        resetBoard ();
    }
    
    private void resetBoard () {
        board.reset ();
        Bot.prev = "";
        turn = 0;
        if (!o.isControlled ()) botMove ();
    }
    
    int state (int position) {
        return board.states ()[position/3][position%3];
    }
    
    public void assign (int position) {
        Bot.prev += position;
        board.assign (position, turn);
        turn++;
        int winner = qualifyTurn ();
        
        if (winner > -1) gameOver (winner);
        else if (!((turn % 2 == 0 && o.isControlled ()) || (turn % 2 != 0 && x.isControlled ()))) botMove ();
    }
    
    public void botMove () {
        TransparentProgressDialog.create (this);
        Handler handler = new Handler ();
        handler.postDelayed (new Runnable () {
            @Override
            public void run () {
                TransparentProgressDialog.cancelLast ();
                assign (Bot.move (board.states (), turn));
            }
        }, 1729);
    }
    
    private int qualifyTurn () {
        int result;
        result = alreadyWin ();
        if (result >= 0) return result;
        else return draw ();
    }
    
    private int alreadyWin () {
        int[][] states = board.states ();
        for (int i = 0; i < 3; i++) if (states[i][0] == states[i][1] && states[i][1] == states[i][2]) return states[i][1];
        for (int i = 0; i < 3; i++) if (states[0][i] == states[1][i] && states[1][i] == states[2][i]) return states[1][i];
        if (states[0][0] == states[1][1] && states[1][1] == states[2][2]) return states[1][1];
        else if (states[0][2] == states[1][1] && states[1][1] == states[2][0]) return states[1][1];
        return -1;
    }
    
    private int draw () {
        for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) if (board.states ()[i][j] < 0) return -1;
        return 2;
    }
    
    private void gameOver (int winner) {
        if (winner == 0) o.win ();
        else if (winner == 1) x.win ();
        EndGameDialog.create (this, winner, o.isControlled (), x.isControlled (), new Runnable () {
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
    }
    
    @Override
    public void onBackPressed () {
        SimpleDialog.create (
            this,
            getString (R.string.dialog_exit),
            getString (R.string.positive_exit),
            getString (R.string.negative_exit),
            new Runnable () {
                @Override
                public void run () {
                    finish ();
                }
            }
        );
    }
}
