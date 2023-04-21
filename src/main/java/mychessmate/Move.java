/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mychessmate;

/**
 * A class that represents a move in a game.
 *
 * The move is defined by the location of the piece being moved and the location where the piece is being moved to.
 *
 * @author Melvic
 */
public class Move {
    int source_location;
    int destination;

    /**
     * It represents a movement in the game.
     */
    public Move() {
        source_location = -1;
        destination = -1;
    }

    /**
     * Represents a movement from a source location to a destination location.
     *
     * @param source_location The location of origin of the movement.
     * @param destination The destination location of the movement.
     */
    public Move(int source_location, int destination) {
        this.source_location = source_location;
        this.destination = destination;
    }
}
