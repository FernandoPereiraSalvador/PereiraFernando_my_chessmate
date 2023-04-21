/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mychessmate;

/**
 * This class represents a chess piece, with a specific value, location and movement state.
 *
 * @author Melvic
 * @author Documentation: Fernando Pereira
 */
public class Piece {

    /**
     * This constant represents the value of a pawn in a chess game.
     */
    public final static int PAWN = 100;

    /**
     * This constant represents the value of a knight in a chess game.
     */
    public final static int KNIGHT = 320;

    /**
     * This constant represents the value of a bishop in a chess game.
     */
    public final static int BISHOP = 325;

    /**
     * This constant represents the value of a rook in a chess game.
     */
    public final static int ROOK = 500;

    /**
     * This constant represents the value of a queen in a chess game.
     */
    public final static int QUEEN = 900;

    /**
     * This constant represents the value of a king in a chess game.
     */
    public final static int KING = 1000000;       
    int value;
    int location;
    boolean has_moved;

    /**
     * This constructor creates a new Piece object with the given value and location, and sets its movement state to false.
     *
     * @param value the value of the piece.
     * @param location the location of the piece.
     */
    public Piece(int value,int location){
        this(value,location,false);
    }

    /**
     * This constructor creates a new Piece object with the given value, location and movement state.
     *
     * @param value the value of the piece.
     * @param location the location of the piece.
     * @param hasMoved the movement state of the piece.
     */
    public Piece(int value,int location,boolean hasMoved){
        this.value = value;
        this.location = location;
        this.has_moved = hasMoved;
    }

    /**
     * This method creates and returns a new Piece object that is a copy of the current piece, with the same value, location and movement state.
     *
     * @return a new Piece object that is a copy of the current piece.
     */
    @Override
    public Piece clone(){
        return new Piece(value,location,has_moved);
    }
}
