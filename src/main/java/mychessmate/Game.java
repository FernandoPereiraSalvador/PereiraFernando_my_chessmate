/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mychessmate;

/**
 * A Game represents a chess game.
 *
 * It contains information about the pieces, their locations, and the current state of the game.
 *
 * @author Melvic
 * @author Documentation: Fernando Pereira
 *
 */
public class Game {          
    Position position;
    Piece human_king;
    Piece computer_king;
    
    /**
     * Constructs a new Game object with the given position.
     *
     * @param position the starting position of the game
     */
    public Game(Position position){
        human_king = position.human_pieces[8];
        computer_king = position.computer_pieces[8];
        this.position = position;
    }

    /**
     * Calculates and returns the result of the game for the given player.
     *
     * @param player the player for whom to calculate the result
     * @return the result of the game for the given player, as an integer value
     */
    public int getResult(int player){
        int state = -1;
        MoveGenerator mg = new MoveGenerator(position,player);
        mg.generateMoves();
        Position[] positions = mg.getPositions();
        if(positions.length == 0){
            if(isChecked(player)) {
                state = GameData.CHECKMATE;
            }
            else state = GameData.DRAW;
        }
        return state;
    }    

    /**
     *
     * Determines whether a given move is safe for the specified player in the current game position.
     *
     * @param player the player who is making the move
     * @param source the source square of the move
     * @param destination the destination square of the move
     * @return true if the move is safe for the specified player, false otherwise
     */
    public boolean safeMove(int player, int source,int destination){
        Move _move = new Move(source,destination);
        Position _position = new Position(position,_move);  
        Game gs = new Game(_position);   
        return !gs.isChecked(player);
    }

    /**
     *
     * Determines whether the king of the specified player is in check in the current game position.
     *
     * @param player the player whose king is being checked
     * @return true if the king of the specified player is in check, false otherwise
     */
    public boolean isChecked(int player){
        boolean checked = false;
        Piece king = (player == GameData.HUMAN)?human_king:computer_king;
        if(king == null) return false;
        checked = checkedByPawn(king);
        if(!checked) checked = checkedByKnight(king);
        if(!checked) checked = checkedByBishop(king);
        if(!checked) checked = checkedByRook(king);
        if(!checked) checked = checkedByQueen(king);
        if(!checked) checked = desSquareAttackedByKing(king);       
        return checked;
    }
    /**
     * Determines whether the specified king is in check by a pawn in the current game position.
     *
     * @param king the king that is being checked
     * @return true if the specified king is in check by a pawn, false otherwise
     */
    private boolean checkedByPawn(Piece king){
        boolean checked = false;   
        int location = king.location;
        if(king == human_king){
            int right_square = position.board[location-9];
            int left_square = position.board[location-11];
            if(right_square == GameData.ILLEGAL || left_square == GameData.ILLEGAL) return false;
            if(right_square<0 && position.computer_pieces[-right_square].value == Piece.PAWN)
                checked = true;
            if(left_square<0 && position.computer_pieces[-left_square].value == Piece.PAWN)
                checked = true;
        }else{
            int right_square = position.board[location+11];
            int left_square = position.board[location+9];
            if(right_square != GameData.ILLEGAL){
                if(right_square>0 && right_square != GameData.EMPTY && 
                        position.human_pieces[right_square].value == Piece.PAWN)
                    checked = true;
            }
            if(left_square != GameData.ILLEGAL){
                if(left_square>0 && left_square != GameData.EMPTY && 
                        position.human_pieces[left_square].value == Piece.PAWN)
                    checked = true;
            }
        }
        return checked;
    }
    /**
     * Determines whether the specified king is in check by a pawn in the current game position.
     *
     * @param king the king that is being checked
     * @return true if the specified king is in check by a pawn, false otherwise
     */
    private boolean checkedByKnight(Piece king){
        boolean checked = false;
        int location = king.location;
        int[] destinations = {location-21,location+21,location+19,location-19,
            location-12,location+12,location-8,location+8};
        for(int destination:destinations){
            int des_square = position.board[destination];
            if(des_square == GameData.ILLEGAL) continue;
            if(king == human_king){                
                if(des_square<0 && position.computer_pieces[-des_square].value == Piece.KNIGHT){
                    checked = true;
                    break;
                }
            }else{
                if(des_square>0 && des_square != GameData.EMPTY && 
                        position.human_pieces[des_square].value == Piece.KNIGHT){
                    checked = true;
                    break;
                }
            }
        }
        return checked;
    }
    /**
     * Determines if the opponent's king attacks a given position.
     *
     * @param king The player's king to check for the opponent's attack.
     * @return true if the opponent's king attacks a position, false otherwise.
     */
    private boolean desSquareAttackedByKing(Piece king){
        boolean checked = false;
        int location = king.location;
        int[] destinations = {location+1,location-1,location+10,location-10,
            location+11,location-11,location+9,location-9};
        for(int destination:destinations){
            int des_square = position.board[destination];
            if(des_square == GameData.ILLEGAL) continue;
            if(king == human_king){                
                if(des_square<0 && position.computer_pieces[-des_square].value == Piece.KING){
                    checked = true;
                    break;
                }
            }else{
                if(des_square>0 && des_square != GameData.EMPTY && 
                        position.human_pieces[des_square].value == Piece.KING){
                    checked = true;
                    break;
                }
            }
        }
        return checked;
    }

    /**
     * Determines if a king is being checked by a bishop.
     *
     * @param king the king to check for being checked
     * @return true if the king is being checked by a bishop, false otherwise
     */
    private boolean checkedByBishop(Piece king){
        boolean checked = false;
        int[] deltas = {11,-11,9,-9};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            while(true){
                int des_square = position.board[delta];
                if(des_square == GameData.ILLEGAL) {
                    checked = false;
                    break;
                }
                if(king == human_king){
                    if(des_square<0 && position.computer_pieces[-des_square].value == Piece.BISHOP){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }else if(king == computer_king){
                    if(des_square>0 && des_square != GameData.EMPTY && 
                            position.human_pieces[des_square].value == Piece.BISHOP){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }

    /**
     * Checks if the given king is under attack by a rook.
     *
     * @param king the king to check if is under attack
     * @return true if the king is under attack by a rook, false otherwise
     */
    private boolean checkedByRook(Piece king){
        boolean checked = false;
        int[] deltas = {1,-1,10,-10};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            while(true){
                int des_square = position.board[delta];
                if(des_square == GameData.ILLEGAL) {
                    checked = false;
                    break;
                }
                if(king == human_king){
                    if(des_square<0 && position.computer_pieces[-des_square].value == Piece.ROOK){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }else if(king == computer_king){
                    if(des_square>0 && des_square != GameData.EMPTY && 
                            position.human_pieces[des_square].value == Piece.ROOK){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }
    /**
     * This method checks if the given king is under attack by an opponent's queen piece.
     *
     * @param king the Piece object representing the king to be checked
     * @return true if the king is under attack by a queen piece, false otherwise
     */
    private boolean checkedByQueen(Piece king){
        boolean checked = false;
        int[] deltas = {1,-1,10,-10,11,-11,9,-9};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            while(true){
                int des_square = position.board[delta];
                if(des_square == GameData.ILLEGAL) {
                    checked = false;
                    break;
                }
                if(king == human_king){
                    if(des_square<0 && position.computer_pieces[-des_square].value == Piece.QUEEN){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }else if(king == computer_king){
                    if(des_square>0 && des_square != GameData.EMPTY && 
                            position.human_pieces[des_square].value == Piece.QUEEN){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }
}
