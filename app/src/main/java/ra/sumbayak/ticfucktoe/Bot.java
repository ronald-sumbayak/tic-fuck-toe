package ra.sumbayak.ticfucktoe;

import android.content.Context;

import java.util.*;

class Bot {
    
    private static Map<String, Integer> movesO, movesX;
    static String prev;
    
    static void init (Context context) {
        movesO = new HashMap<> ();
        String[] stringsO = context.getResources ().getStringArray (R.array.moves_o);
        for (String string : stringsO) {
            String[] split = string.split (",");
            movesO.put (split[0], Integer.valueOf (split[1]));
        }
        
        movesX = new HashMap<> ();
        String[] stringsX = context.getResources ().getStringArray (R.array.moves_x);
        for (String string : stringsX) {
            String[] split = string.split (",");
            movesX.put (split[0], Integer.valueOf (split[1]));
        }
    }
    
    static int move (int[][] states, int turn) {
        Integer result;
        if (turn == 8) return lastMove (states);
        
        result = isWinning (states, turn % 2);
        if (result >= 0) return result;
        
        result = isLosing (states, (turn + 1) % 2);
        if (result >= 0) return result;
        
        result = movesO.get (prev);
        if (result != null) return result;
        
        result = movesX.get (prev);
        if (result != null) return result;
        
        return random (states);
    }
    
    private static int lastMove (int[][] states) {
        for (int i = 0; i < 9; i++) if (states[i/3][i%3] < 0) return i;
        return -1;
    }
    
    private static int isWinning (int[][] states, int turn) {
        for (int i = 0; i < 3; i++) {
            if (states[i][1] == turn && states[i][1] == states[i][2] && states[i][0] < 0) return (i * 3);
            if (states[i][0] == turn && states[i][0] == states[i][2] && states[i][1] < 0) return (i * 3) + 1;
            if (states[i][0] == turn && states[i][0] == states[i][1] && states[i][2] < 0) return (i * 3) + 2;
        }
        
        for (int i = 0; i < 3; i++) {
            if (states[1][i] == turn && states[1][i] == states[2][i] && states[0][i] < 0) return i;
            if (states[0][i] == turn && states[0][i] == states[2][i] && states[1][i] < 0) return 3 + i;
            if (states[0][i] == turn && states[0][i] == states[1][i] && states[2][i] < 0) return 6 + i;
        }
        
        if (states[1][1] == turn && states[1][1] == states[2][2] && states[0][0] < 0) return 0;
        if (states[0][0] == turn && states[0][0] == states[2][2] && states[1][1] < 0) return 4;
        if (states[0][0] == turn && states[0][0] == states[1][1] && states[2][2] < 0) return 8;
        
        if (states[1][1] == turn && states[1][1] == states[2][0] && states[0][2] < 0) return 2;
        if (states[0][2] == turn && states[0][2] == states[2][0] && states[1][1] < 0) return 4;
        if (states[0][2] == turn && states[0][2] == states[1][1] && states[2][0] < 0) return 6;
        
        return -1;
    }
    
    private static int isLosing (int[][] states, int opponent) {
        for (int i = 0; i < 3; i++) {
            if (states[i][1] == opponent && states[i][1] == states[i][2] && states[i][0] < 0) return (i * 3);
            if (states[i][0] == opponent && states[i][0] == states[i][2] && states[i][1] < 0) return (i * 3) + 1;
            if (states[i][0] == opponent && states[i][0] == states[i][1] && states[i][2] < 0) return (i * 3) + 2;
        }
        
        for (int i = 0; i < 3; i++) {
            if (states[1][i] == opponent && states[1][i] == states[2][i] && states[0][i] < 0) return i;
            if (states[0][i] == opponent && states[0][i] == states[2][i] && states[1][i] < 0) return 3 + i;
            if (states[0][i] == opponent && states[0][i] == states[1][i] && states[2][i] < 0) return 6 + i;
        }
        
        if (states[1][1] == opponent && states[1][1] == states[2][2] && states[0][0] < 0) return 0;
        if (states[0][0] == opponent && states[0][0] == states[2][2] && states[1][1] < 0) return 4;
        if (states[0][0] == opponent && states[0][0] == states[1][1] && states[2][2] < 0) return 8;
        
        if (states[1][1] == opponent && states[1][1] == states[2][0] && states[0][2] < 0) return 2;
        if (states[0][2] == opponent && states[0][2] == states[2][0] && states[1][1] < 0) return 4;
        if (states[0][2] == opponent && states[0][2] == states[1][1] && states[2][0] < 0) return 6;
        
        return -1;
    }
    
    private static int random (int[][] states) {
        List<Integer> remains = new ArrayList<> ();
        for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) if (states[i][j] < 0) remains.add ((i*3)+j);
        Random random = new Random ();
        return remains.get (random.nextInt (remains.size ()));
    }
}
