/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mychessmate;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Dialog box to allow user to select a promotion piece when a pawn reaches the opposite end of the board.
 *
 * @author Melvic
 */
public class PromotionPane extends JDialog implements ActionListener{
    int index;
    int location;
    JPanel main_pane;
    MyChessmate chessmate;

    /**
     * A dialog box for promoting a pawn to a new piece type in a chess game.
     *
     * @param chessmate the MyChessmate object representing the current game.
     */
    public PromotionPane(MyChessmate chessmate){
        setTitle("New Piece");
        this.chessmate = chessmate;
        main_pane = new JPanel(new GridLayout(1,4,10,0));
        Resource resource = new Resource();

        int[] cmdActions = {
            Piece.QUEEN,Piece.ROOK,Piece.BISHOP,Piece.KNIGHT
        };        
        for(int i=0; i<cmdActions.length; i++){
            JButton button = new JButton();
            button.addActionListener(this);
            button.setActionCommand(cmdActions[i]+"");
            main_pane.add(button);
        }
        setContentPane(main_pane);        
        setResizable(false);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                resumeGame(Piece.QUEEN);
            }
        });
    }

    /**
     * Sets the icons of the buttons in this PromotionPane to the corresponding images for the specified color of piece.
     *
     * @param white true if the promoted piece is white, false if it is black.
     */
    public void setIcons(boolean white){
        Component[] components = main_pane.getComponents();
        Resource resource = new Resource();
        String[] resourceStrings = {"q","r","b","n"};
        for(int i=0; i<components.length; i++){
            JButton button = (JButton)components[i];
            button.setIcon(new ImageIcon(
                    resource.getResource((white?"w":"b")+resourceStrings[i])));
        }
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Responds to a button press by hiding this PromotionPane and resuming the game with the specified piece type.
     *
     * @param e the ActionEvent representing the button press.
     */
    public void actionPerformed(ActionEvent e){
        int promotion_piece = Integer.parseInt(e.getActionCommand());
        setVisible(false);
        resumeGame(promotion_piece);
    }

    /**
     * Resumes the chess game by promoting the pawn to the specified piece type, updating the game state, and initiating the computer's move.
     *
     * @param promotion_piece the integer representing the type of piece to which the pawn is promoted.
     */
    public void resumeGame(int promotion_piece){  
        chessmate.position.human_pieces[index] = new Piece(promotion_piece,location);
        chessmate.newHistoryPosition();
        chessmate.board_pane.repaint();
        chessmate.state = GameData.COMPUTER_MOVE;
    }
}
