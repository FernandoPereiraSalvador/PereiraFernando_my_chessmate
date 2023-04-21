/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mychessmate;

/**
 *
 * @author Melvic
 */
public class GameData {

    /**
     * Constant representing an empty value.
     */
    public final static int EMPTY = 55;

    /**
     * Constant representing an illegal value.
     */
    public final static int ILLEGAL = 77;

    /**
     * Constant representing an human value.
     */
    public final static int HUMAN = 1;

    /**
     * Constant representing an computer value.
     */
    public final static int COMPUTER = 0;

    /**
     * Constant representing an board_image value.
     */
    public final static int BOARD_IMAGE = 1000;

    /**
     * Constant representing an glow value.
     */
    public final static int GLOW = 1001;

    /**
     * Constant representing an glow2 value.
     */
    public final static int GLOW2 = 1002;    

    /**
     * Constant representing an prepare_animation value.
     */
    public final static int PREPARE_ANIMATION = 1003;

    /**
     * Constant representing an animating value.
     */
    public final static int ANIMATING = 1004;

    /**
     * Constant representing an human_move value.
     */
    public final static int HUMAN_MOVE = 1005;

    /**
     * Constant representing an computer_move value.
     */
    public final static int COMPUTER_MOVE = 1006;

    /**
     * Constant representing an game_ended value.
     */
    public final static int GAME_ENDED = 1007;

    /**
     * Constant representing an draw value.
     */
    public final static int DRAW = 0;

    /**
     * Constant representing an checkmate value.
     */
    public final static int CHECKMATE = 1;

    /**
     * Constant representing an new_button value.
     */
    public final static int NEW_BUTTON = 10078;

    /**
     * Constant representing an new_button2 value.
     */
    public final static int NEW_BUTTON2 = 10079;

    /**
     * Constant representing an quit_button value.
     */
    public final static int QUIT_BUTTON = 10080;

    /**
     * Constant representing an quit_button2 value.
     */
    public final static int QUIT_BUTTON2 = 10081;

    /**
     * Constant representing an about_button value.
     */
    public final static int ABOUT_BUTTON = 10082;

    /**
     * Constant representing an about_button2 value.
     */
    public final static int ABOUT_BUTTON2 = 10083;

    /**
     * Constant representing an history_button value.
     */
    public final static int HISTORY_BUTTON = 10084;

    /**
     * Constant representing an history_button2 value.
     */
    public final static int HISTORY_BUTTON2 = 10085;    

    /**
     * Constant representing an first_button value.
     */
    public final static int FIRST_BUTTON = 10086;

    /**
     * Constant representing an first_button2 value.
     */
    public final static int FIRST_BUTTON2 = 10087;

    /** 
     * Constant representing an last_button value.
     */
    public final static int LAST_BUTTON = 10088;

    /**
     * Constant representing an last_button2 value.
     */
    public final static int LAST_BUTTON2 = 10089;

    /**
     * Constant representing an prev_button value.
     */
    public final static int PREV_BUTTON = 10090;

    /**
     * Constant representing an prev_button2 value.
     */
    public final static int PREV_BUTTON2 = 10091;

    /**
     * Constant representing an next_button value.
     */
    public final static int NEXT_BUTTON = 10092;

    /**
     * Constant representing an next_button value.
     */
    public final static int NEXT_BUTTON2 = 10093; 

    /**
     * Constant representing an promoting value.
     */
    public final static int PROMOTING = 10086;    

    /**
     * Constant representing an board_image2 value.
     */
    public final static int BOARD_IMAGE2 = 10087;

    /**
     * Constant representing an history_title.
     */
    public final static int HISTORY_TITLE = 10094;

    /**
     * Constant representing an mychessmate value.
     */
    public final static int MYCHESSMATE = 10095;

    /**
     * String constant representing the application version.
     */
    public final static String VERSION = new Resource().getResourceString("version");
    /*public final static int[] HUMAN_PAWN_TABLE = {
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,10, 10, 10, 10, 10, 10, 10, 10,0,
        0,7, 7, 7, 7, 7, 7, 7, 7,0,
        0,4, 4, 5, 6, 6, 5, 4, 4,0,
        0,2, 1, 1, 5, 5, 1, 1, 2,0,
        0,1, 1, 0, 2, 2, 0, 1, 1,0,
        0,1, 1, 1, 0, 0, 1, 1, 1,0,
        0,2, 0, 0, 0, 0, 0, 0, 2,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0
    };
    public final static int[] COMPUTER_PAWN_TABLE = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 
        0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 
        0, 1, 1, 0, 2, 2, 0, 1, 1, 0, 
        0, 2, 1, 1, 5, 5, 1, 1, 2, 0, 
        0, 4, 4, 5, 6, 6, 5, 4, 4, 0, 
        0, 7, 7, 7, 7, 7, 7, 7, 7, 0, 
        0, 10, 10, 10, 10, 10, 10, 10, 10, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    public final static int[] HUMAN_KNIGHT_TABLE = {
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,1, 0, 0, 0, 0, 0, 0, 1,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,0, 0, 1, 1, 1, 1, 0, 0,0,
        0,0, 1, 1, 2, 2, 1, 1, 0,0,
        0,0, 0, 1, 2, 2, 1, 0, 0,0,
        0,0, 0, 3, 1, 1, 3, 0, 0,0,
        0,0, 0, 1, 2, 2, 1, 0, 0,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0
    };
    public final static int[] COMPUTER_KNIGHT_TABLE = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 
        0, 0, 0, 3, 1, 1, 3, 0, 0, 0, 
        0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 
        0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 
        0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    public final static int[] HUMAN_BISHOP_TABLE = {
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,-20, -10, -10, -10, -10, -10, -10, -20,0,
        0,-10, 0, 0, 0, 0, 0, 0, -10,0,
        0,-10, 0, 5, 10, 10, 5, 0, -10,0,
        0,-10, 5, 5, 10, 10, 5, 5, -10,0,
        0,-10, 0, 10, 10, 10, 10, 0, -10,0,
        0,-10, 10, 10, 10, 10, 10, 10, -10,0,
        0,-10, 5, 0, 0, 0, 0, 5, -10,0,
        0,-20, -10, -40, -10, -10, -40, -10, -20,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0,
        0,0, 0, 0, 0, 0, 0, 0, 0,0
    };   
    public final static int[] COMPUTER_BISHOP_TABLE = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, -20, -10, -40, -10, -10, -40, -10, -20, 0, 
        0, -10, 5, 0, 0, 0, 0, 5, -10, 0, 
        0, -10, 10, 10, 10, 10, 10, 10, -10, 0, 
        0, -10, 0, 10, 10, 10, 10, 0, -10, 0, 
        0, -10, 5, 5, 10, 10, 5, 5, -10, 0, 
        0, -10, 0, 5, 10, 10, 5, 0, -10, 0, 
        0, -10, 0, 0, 0, 0, 0, 0, -10, 0, 
        0, -20, -10, -10, -10, -10, -10, -10, -20, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    }; 
    public static void main(String[] args){
        int j = 119;
        for(int i=0; i<120; i++){            
            if(i%10==0) System.out.println();
            System.out.print(HUMAN_PAWN_TABLE[j--]+", ");            
        }
    }*/
}
