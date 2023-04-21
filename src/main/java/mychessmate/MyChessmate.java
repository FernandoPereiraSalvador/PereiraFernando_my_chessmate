/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mychessmate;

import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * The MyChessmate class represents the main class of the chess game.
 *
 * It extends the JFrame class to create the game window and handles the game logic.
 *
 * @author Melvic
 * @author Documentation: Fernando Pereira
 */
public class MyChessmate extends JFrame implements MouseListener{
    Position position;        
    ChessBoardPane board_pane;  
    HistoryBoardPane history_pane;
    JPanel east_pane;
    Resource resource = new Resource();
    Map<Integer,Image> images = new HashMap<Integer,Image>();
    Map<Integer,Icon> icon_images = new HashMap<Integer,Icon>();
    Move move = new Move();
    boolean piece_selected;
    boolean is_white;
    int state;
    MoveSearcher move_searcher;
    Game game;    
    JLabel new_game,quit,about,history,first,prev,next,last;    
    JPanel main_pane = new JPanel(new BorderLayout());
    PreferencesPane play_options;
    boolean castling;
    PromotionPane promotion_pane;
    List<Position> history_positions = new ArrayList<Position>();
    int history_count;
    Color bg_color = Color.decode("#efd39c");

    /**
     * Creates a new instance of MyChessmate.
     *
     * Initializes the main frame with the appropriate title, content pane, and event listener.
     *
     * Loads menu icons and board images.
     *
     * Creates the chess board pane, adds it to the main panel, and sets the background color.
     *
     * Creates the east pane using the createEastPane method.
     *
     * Sets the size of the frame to a fixed height and the packed width.
     *
     * Adds a window listener to the frame to handle closing events.
     */
    public MyChessmate() {
        super("MyChessmate " + GameData.VERSION);
        setContentPane(main_pane);
        position = new Position();
        promotion_pane = new PromotionPane(this);

        loadMenuIcons();
        loadBoardImages();
        
        board_pane = new ChessBoardPane();                                             
        
        main_pane.add(createMenuPane(),BorderLayout.WEST);
        main_pane.add(board_pane,BorderLayout.CENTER);  
        main_pane.setBackground(bg_color);      
        createEastPane();
        
        pack();
        Dimension size = getSize();
        size.height = 523;
        setSize(size);
        
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                quit();
            }
        });
    }               

    /**
     *
     * The createMenuPane method is used to create the menu panel for the MyChessmate game.
     *
     * @return a JPanel object representing the menu panel
     */
    public JPanel createMenuPane() {
        new_game = new JLabel(icon_images.get(GameData.NEW_BUTTON));
        about = new JLabel(icon_images.get(GameData.ABOUT_BUTTON));
        history = new JLabel(icon_images.get(GameData.HISTORY_BUTTON));
        quit = new JLabel(icon_images.get(GameData.QUIT_BUTTON));

        new_game.addMouseListener(this);
        about.addMouseListener(this);
        history.addMouseListener(this);
        quit.addMouseListener(this);
        
        JPanel pane = new JPanel(new GridLayout(4,1));
        pane.add(new_game);        
        pane.add(history);
        pane.add(about);
        pane.add(quit);             
        pane.setBackground(bg_color);
        JPanel menu_pane = new JPanel(new BorderLayout());
        menu_pane.setBackground(bg_color);
        menu_pane.add(pane,BorderLayout.SOUTH);
        menu_pane.setBorder(BorderFactory.createEmptyBorder(0,20,20,0));
        return menu_pane;
    }

    /**
     * The createEastPane method is used to create the east panel for the MyChessmate game.
     *
     * The panel contains a history board pane and navigation buttons for the move history.
     */
    public void createEastPane() {
        east_pane = new JPanel(new BorderLayout());
        history_pane = new HistoryBoardPane();

        JPanel pane = new JPanel(new GridLayout(1,4));        
        first = new JLabel(icon_images.get(GameData.FIRST_BUTTON));
        prev = new JLabel(icon_images.get(GameData.PREV_BUTTON));
        next = new JLabel(icon_images.get(GameData.NEXT_BUTTON));
        last = new JLabel(icon_images.get(GameData.LAST_BUTTON));
        
        pane.add(first);
        pane.add(prev);
        pane.add(next);
        pane.add(last);
        
        JPanel pane2 = new JPanel();
        pane2.setLayout(new BoxLayout(pane2,BoxLayout.Y_AXIS));
        pane2.add(history_pane);
        pane2.add(pane);
        
        east_pane.add(pane2,BorderLayout.SOUTH);
        east_pane.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        east_pane.setBackground(bg_color);        
        east_pane.setVisible(false);
        main_pane.add(east_pane,BorderLayout.EAST);
        
        pane.setBorder(BorderFactory.createEmptyBorder(0,14,0,14));
        pane.setBackground(bg_color);
        
        first.addMouseListener(this);
        prev.addMouseListener(this);
        next.addMouseListener(this);
        last.addMouseListener(this);
    }    

    /**
     * Starts a new game, initializing the position, game, and game state. 
     * Loads the piece images and sets the promotion icons for the current player. 
     * Clears the history and resets the move searcher level. 
     * Begins the game by calling play().
     */
    public void newGame(){                
        if(!east_pane.isVisible()){
            east_pane.setVisible(true);
            pack();
            setLocationRelativeTo(null);
        }        
        is_white = play_options.white_button.isSelected();
        move.source_location = -1;
        move.destination = -1;
        position = new Position();
        position.initialize(is_white);
        game = new Game(position);               
        loadPieceImages();
        promotion_pane.setIcons(is_white);
        board_pane.repaint();
        if(is_white) state = GameData.HUMAN_MOVE;
        else state = GameData.COMPUTER_MOVE;
        castling = false;
        history_positions.clear();
        history_count = 0;
        newHistoryPosition();
        move_searcher.level = play_options.levelSlider.getValue();
        play();
    }   

    /**
     * This method starts the chess game and continuously checks the current state of the game to determine the next action.
     * It runs on a separate thread to prevent the GUI from freezing.
     */
    public void play(){
        Thread t = new Thread(){
            public void run(){
                while(true){           
                    switch(state){
                        case GameData.HUMAN_MOVE:    
                            break;
                        case GameData.COMPUTER_MOVE:             
                            if(gameEnded(GameData.COMPUTER)){                                
                                state = GameData.GAME_ENDED;
                                break;
                            }
                            move = move_searcher.alphaBeta(GameData.COMPUTER, position, 
                                    Integer.MIN_VALUE, Integer.MAX_VALUE, play_options.levelSlider.getValue()).last_move;    
                            state = GameData.PREPARE_ANIMATION;                            
                            break;
                        case GameData.PREPARE_ANIMATION:
                            prepareAnimation();
                            break;
                        case GameData.ANIMATING:
                            animate();
                            break;                        
                        case GameData.GAME_ENDED: return;
                    }
                    try{                        
                        Thread.sleep(3);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    /**
     *
     * Check if the game has ended for the specified player.
     *
     * @param player The player to check, either GameData.COMPUTER or GameData.HUMAN.
     * @return True if the game has ended, false otherwise.
     */
    public boolean gameEnded(int player){
        int result = game.getResult(player);
        boolean end_game = false;
        String color ="";
        if(player == GameData.COMPUTER){
            color = (is_white)?"White":"Black";
        }else color = (is_white)?"Black":"White";
        if(result == GameData.CHECKMATE){
            showEndGameResult(color+" wins by CHECKMATE");
            end_game = true;
        }else if(result == GameData.DRAW){
            showEndGameResult("DRAW");
            end_game = true;
        }
        return end_game;
    }

    /**
     * Displays a dialog with the end game result message and two options: "Play again" and "Cancel". 
     * If the "Play again" option is selected, the PlayOptions dialog is shown.
     *
     * @param message the end game result message to be displayed in the dialog
     */
    public void showEndGameResult(String message) {
        int option = JOptionPane.showOptionDialog(null,
                message,"Game Over",0,JOptionPane.PLAIN_MESSAGE,
                null,new Object[]{"Play again","Cancel"},"Play again");
        if(option == 0){
            play_options.setVisible(true);
        }
    }

    /**
     * Displays a warning message when attempting to start a new game when the player's move has not yet been completed.
     */
    public void showNewGameWarning() {
        JOptionPane.showMessageDialog(null,
                "Start a new game after I made my move.\n",
                "Message", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     *
     * This method is called when a mouse button is clicked on a component.
     *
     * It handles the different actions depending on the clicked component: quitting the game, starting a new game, showing information about the game, showing the game history, moving to the first, previous, next or last position of the history.
     *
     * @param e the MouseEvent representing the mouse click
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source == quit) {
            quit();
        } else if (source == new_game) {
            if (state == GameData.COMPUTER_MOVE) {
                showNewGameWarning();
                return;
            }
            if(play_options == null) {
                play_options = new PreferencesPane(this);
                move_searcher = new MoveSearcher(this);
            }
            play_options.setVisible(true);
        }else if(source == about){
            AboutPane.createAndShowUI();
        }else if(source == history){
            east_pane.setVisible(!east_pane.isVisible());
            pack();
            setLocationRelativeTo(null);
        }else if(source == first){
            history_count = 0;
            history_pane.repaint();            
        }else if(source == prev){
            if(history_count>0){
                history_count--;
                history_pane.repaint();
            }
        }else if(source == next){
            if(history_count<history_positions.size()-1){
                history_count++;
                history_pane.repaint();
            }
        }else if(source == last){
            history_count = history_positions.size()-1;
            history_pane.repaint();
        }
    }    

    /**
     *
     * This method is called when the mouse enters a component. 
     * 
     * It changes the image of the component's icon to the highlighted version.
     *
     * @param e The mouse event
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        Object source = e.getSource();
        if(source == new_game){
            new_game.setIcon(icon_images.get(GameData.NEW_BUTTON2));
        }else if(source == about){
            about.setIcon(icon_images.get(GameData.ABOUT_BUTTON2));
        }else if(source == history){
            history.setIcon(icon_images.get(GameData.HISTORY_BUTTON2));
        }else if(source == quit){
            quit.setIcon(icon_images.get(GameData.QUIT_BUTTON2));
        }else if(source == first){
            first.setIcon(icon_images.get(GameData.FIRST_BUTTON2));
        }else if(source == prev){
            prev.setIcon(icon_images.get(GameData.PREV_BUTTON2));
        }else if(source == next){
            next.setIcon(icon_images.get(GameData.NEXT_BUTTON2));
        }else if(source == last){
            last.setIcon(icon_images.get(GameData.LAST_BUTTON2));
        }
    }

    /**
     *
     * This method is called when the mouse exits a component. 
     * 
     * It changes the icon of the component to its default icon.
     *
     * @param e the MouseEvent representing the mouse exit event
     */
    @Override
    public void mouseExited(MouseEvent e) {
        Object source = e.getSource();
        if (source == new_game) {
            new_game.setIcon(icon_images.get(GameData.NEW_BUTTON));
        }else if(source == about){
            about.setIcon(icon_images.get(GameData.ABOUT_BUTTON));
        }else if(source == history){
            history.setIcon(icon_images.get(GameData.HISTORY_BUTTON));
        }else if(source == quit){
            quit.setIcon(icon_images.get(GameData.QUIT_BUTTON));
        }else if(source == first){
            first.setIcon(icon_images.get(GameData.FIRST_BUTTON));
        }else if(source == prev){
            prev.setIcon(icon_images.get(GameData.PREV_BUTTON));
        }else if(source == next){
            next.setIcon(icon_images.get(GameData.NEXT_BUTTON));
        }else if(source == last){
            last.setIcon(icon_images.get(GameData.LAST_BUTTON));
        }
    }

    /**
     *
     * This method is called when a mouse button is pressed on the chessboard but does not perform any action.
     *
     * @param e The MouseEvent object that contains information about the event.
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
     * This method is called when a mouse button is released but does not perform any action.
     *
     * @param e The MouseEvent object that contains information about the event.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * ChessBoardPane is a class that extends JPanel and implements MouseListener to visually represent a chessboard.
     */
    public class ChessBoardPane extends JPanel implements MouseListener {

        Image animating_image;
        int movingX, movingY, desX, desY, deltaX, deltaY;

        /**
         * ChessBoardPane class constructor. 
         * 
         * Sets the preferred size of the pane and its background color. 
         * 
         * dds this object as a mouse event listener.
         */
        public ChessBoardPane() {
            setPreferredSize(new Dimension(450, 495));
            setBackground(bg_color);
            addMouseListener(this);
        }

        /**
         *
         * This method is used to paint the graphical representation of the chess board
         *
         * @param g The graphics context to paint on
         */
        @Override
        public void paintComponent(Graphics g){
            if(position.board == null) return;
            super.paintComponent(g);  
            g.drawImage(images.get(GameData.MYCHESSMATE),20,36,this);
            g.drawImage(images.get(GameData.BOARD_IMAGE),20,65,this);       
            for (int i = 0; i < position.board.length-11; i++) {
                if (position.board[i] == GameData.ILLEGAL) continue;                                                                
                int x = i%10;
                int y = (i-x)/10;
                
                if (piece_selected && i == move.source_location) {                
                    g.drawImage(images.get(GameData.GLOW), x * 45, y * 45,this);                    
                }else if(!piece_selected && move.destination == i && 
                        (position.board[i]==GameData.EMPTY || position.board[i]<0)){
                    g.drawImage(images.get(GameData.GLOW2), x * 45, y * 45, this);                                        
                }
                
                if (position.board[i] == GameData.EMPTY) continue;
                
                if(state == GameData.ANIMATING && i==move.source_location) continue;
                if (position.board[i] > 0) {          
                    int piece = position.human_pieces[position.board[i]].value;
                    g.drawImage(images.get(piece),x*45,y*45,this);
                }else{
                    int piece = position.computer_pieces[-position.board[i]].value;
                    g.drawImage(images.get(-piece),x*45,y*45,this);
                }               
            }  
            if(state == GameData.ANIMATING){
                g.drawImage(animating_image,movingX,movingY,this);
            }
        }

        /**
         *
         * This method is called when a mouse button has been clicked on the board
         *
         * @param e The MouseEvent representing the click event
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if(state != GameData.HUMAN_MOVE) return;
            int location = boardValue(e.getY())*10+boardValue(e.getX());              
            if(position.board[location] == GameData.ILLEGAL) return;
            if((!piece_selected || position.board[location]>0) && position.board[location] != GameData.EMPTY){
                if(position.board[location]>0){
                    piece_selected = true;
                    move.source_location = location;
                }
            }else if(piece_selected && validMove(location)){
                piece_selected = false;
                move.destination = location;     
                state = GameData.PREPARE_ANIMATION;
            }
            repaint();
        }

        /**
         *
         * This method is called when a mouse button is pressed on the board, but before it is released.
         *
         * @param e The MouseEvent representing the press event
         */
        @Override
        public void mousePressed(MouseEvent e) { }

        /**
         *
         * This method is called when a mouse button is released on the board, after being pressed.
         *
         * @param e The MouseEvent representing the release event
         */
        @Override
        public void mouseReleased(MouseEvent e) { }

        /**
         *
         * This method is called when the mouse enters the board.
         *
         * @param e The MouseEvent representing the enter event
         */
        @Override
        public void mouseEntered(MouseEvent e) { }

        /**
         *
         * This method is called when the mouse exits the board.
         *
         * @param e The MouseEvent representing the exit event
         */
        @Override
        public void mouseExited(MouseEvent e) { }
    }

    /**
     * This class represents a JPanel that displays the history of moves made during a chess game.
     *
     * Extends the JPanel class.
     */
    public class HistoryBoardPane extends JPanel{

        /**
         * Constructs a new instance of the HistoryBoardPane class with the preferred size of 300x330 and the background color
         * set to the value of the 'bg_color' variable.
         */
        public HistoryBoardPane(){
            setBackground(bg_color);
            setPreferredSize(new Dimension(300,330));            
        }

        /**
         *
         * Overrides the paintComponent method of the JPanel class to draw the move history board graphical representation. 
         * 
         * Draws the board image and title, as well as the pieces for the current position in the history if available.
         *
         * @param g The Graphics object used for drawing the components.
         */
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(images.get(GameData.HISTORY_TITLE),20,15,this);
            g.drawImage(images.get(GameData.BOARD_IMAGE2),14,44,this);
            if(history_positions.size()<=0) return;
            Position _position = history_positions.get(history_count);
            for(int i=0; i<_position.board.length -11; i++){
                if(_position.board[i] == GameData.EMPTY) continue;
                if(_position.board[i] == GameData.ILLEGAL) continue;
                int x = i%10;
                int y = (i-x)/10;
                if (_position.board[i] > 0) {          
                    int piece = _position.human_pieces[_position.board[i]].value;
                    g.drawImage(images.get(piece+10),x*30,y*30,this);
                }else{
                    int piece = _position.computer_pieces[-_position.board[i]].value;
                    g.drawImage(images.get(-piece+10),x*30,y*30,this);
                }
            }
        }
    }

    /**
     *
     * This method checks if a move is valid given the destination square. 
     * It takes the source and destination squares as parameters and returns true if the move is valid, false otherwise.
     *
     * @param destination the destination square
     * @return true if the move is valid, false otherwise
     */
    public boolean validMove(int destination){        
        int source = move.source_location;
        int destination_square = position.board[destination];
        if(destination_square == GameData.ILLEGAL) return false;
        if(!game.safeMove(GameData.HUMAN,source,destination)) return false;
        boolean valid = false;
        int piece_value = position.human_pieces[position.board[source]].value;                        
        switch(piece_value){
            case Piece.PAWN:
                if(destination == source-10 && destination_square == GameData.EMPTY) valid = true;
                if(destination == source-20 && position.board[source-10] == GameData.EMPTY &&
                        destination_square == GameData.EMPTY && source>80) valid = true;
                if(destination == source-9 && destination_square<0) valid = true;
                if(destination == source-11 && destination_square<0) valid = true;
                break;
            case Piece.KNIGHT:
            case Piece.KING:
                if(piece_value == Piece.KING) valid = checkCastling(destination);
                int[] destinations = null;
                if(piece_value == Piece.KNIGHT) destinations = new int[]{source-21,source+21,source+19,source-19,                    
                    source-12,source+12,source-8,source+8};
                else destinations = new int[]{source+1,source-1,source+10,source-10,
                    source+11,source-11,source+9,source-9};
                for(int i=0; i<destinations.length; i++){
                    if(destinations[i] == destination){
                        if(destination_square == GameData.EMPTY || destination_square<0){
                            valid = true;
                            break;
                        }
                    }
                }                
                break;
            case Piece.BISHOP:
            case Piece.ROOK:
            case Piece.QUEEN:
                int[] deltas = null;
                if(piece_value == Piece.BISHOP) deltas = new int[]{11,-11,9,-9};
                if(piece_value == Piece.ROOK) deltas = new int[]{1,-1,10,-10};
                if(piece_value == Piece.QUEEN) deltas = new int[]{1,-1,10,-10,11,-11,9,-9};
                for (int i = 0; i < deltas.length; i++) {
                    int des = source + deltas[i]; 
                    valid = true;
                    while (destination != des) { 
                        destination_square = position.board[des];  
                        if(destination_square != GameData.EMPTY){
                            valid = false;
                            break;
                        }                        
                        des += deltas[i];
                    }
                    if(valid) break;
                }
                break;
        }        
        return valid;
    }

    /**
     *
     * This method checks if castling is a valid move for the current player, based on their position and the current state of the game.
     *
     * @param destination the destination square of the move
     *
     * @return true if castling is a valid move, false otherwise
     */
    public boolean checkCastling(int destination){        
        Piece king = position.human_pieces[8];
        Piece right_rook = position.human_pieces[6];
        Piece left_rook = position.human_pieces[5];
        
        if(king.has_moved) return false;              
        int source = move.source_location;
        
        if(right_rook == null && left_rook == null) return false;
        if(right_rook != null && right_rook.has_moved && 
                left_rook != null && left_rook.has_moved) return false;
            
        if(is_white){            
            if(source != 95) return false;            
            if(destination != 97 && destination != 93) return false;
            if(destination == 97){
                if(position.board[96] != GameData.EMPTY) return false;
                if(position.board[97] != GameData.EMPTY) return false;
                if(!game.safeMove(GameData.HUMAN,source,96)) return false;
                if(!game.safeMove(GameData.HUMAN,source,97)) return false;
            }else if(destination == 93){
                if(position.board[94] != GameData.EMPTY) return false;
                if(position.board[93] != GameData.EMPTY) return false;
                if(!game.safeMove(GameData.HUMAN,source,94)) return false;
                if(!game.safeMove(GameData.HUMAN,source,93)) return false;
            }
        }else{
            if(source != 94) return false;            
            if(destination != 92 && destination != 96) return false;
            if(destination == 92){
                if(position.board[93] != GameData.EMPTY) return false;
                if(position.board[92] != GameData.EMPTY) return false;
                if(!game.safeMove(GameData.HUMAN,source,93)) return false;
                if(!game.safeMove(GameData.HUMAN,source,92)) return false;
            }else if(destination == 96){
                if(position.board[95] != GameData.EMPTY) return false;
                if(position.board[96] != GameData.EMPTY) return false;
                if(!game.safeMove(GameData.HUMAN,source,95)) return false;
                if(!game.safeMove(GameData.HUMAN,source,96)) return false;
            }
        }        
        return castling=true;
    }

    /**
     *
     * Returns the board value of a given piece value.
     *
     * @param value the piece value to calculate the board value from
     * @return the board value of the given piece value
     */
    public int boardValue(int value){
        return value/45;
    }

    /**
     * Prepares the animation for the move. Sets the image to animate, calculates the source and destination locations, 
     * calculates the movement direction and sets the animation state.
     */
    public void prepareAnimation(){
        int animating_image_key = 0;
        if(position.board[move.source_location]>0){
            animating_image_key = position.human_pieces[position.board[move.source_location]].value;
        }else {
            animating_image_key = -position.computer_pieces[-position.board[move.source_location]].value;
        }        
        board_pane.animating_image = images.get(animating_image_key);
        int x = move.source_location%10;        
        int y = (move.source_location-x)/10;
        board_pane.desX = move.destination%10;
        board_pane.desY = (move.destination-board_pane.desX)/10;
        int dX = board_pane.desX-x;
        int dY = board_pane.desY-y;           
        board_pane.movingX = x*45;
        board_pane.movingY = y*45;
        if(Math.abs(dX)>Math.abs(dY)){
            if(dY == 0){
                board_pane.deltaX = (dX>0)?1:-1;
                board_pane.deltaY = 0;
            }else{
                board_pane.deltaX = (dX>0)?Math.abs(dX/dY):-(Math.abs(dX/dY));
                board_pane.deltaY = (dY>0)?1:-1;
            }
        }else{
            if(dX == 0){
                board_pane.deltaY = (dY>0)?1:-1;
                board_pane.deltaX = 0;
            }else{
                board_pane.deltaX = (dX>0)?1:-1;
                board_pane.deltaY = (dY>0)?Math.abs(dY/dX):-(Math.abs(dY/dX));
            }
        }          
        state = GameData.ANIMATING;
    }

    /**
     * Method to animate the movement of the pieces on the board.
     */
    public void animate(){
        if (board_pane.movingX == board_pane.desX * 45 && board_pane.movingY == board_pane.desY * 45) {                                           
            board_pane.repaint();            
            int source_square = position.board[move.source_location];            
            if(source_square>0){                
                state = GameData.COMPUTER_MOVE;                                               
            }else {
                if(move.destination > 90 && move.destination<98 
                        && position.computer_pieces[-source_square].value == Piece.PAWN)
                    promoteComputerPawn();
                //if(gameEnded(GameData.HUMAN)) state = GameData.GAME_ENDED; 
                state = GameData.HUMAN_MOVE;
            }                        
            position.update(move);       
          /*  int k = 0;
            for(int i=0; i<12; i++){
                for(int j=0; j<10; j++){
                    System.out.print(position.board[k]+" ");
                    k++;
                }
                System.out.println();                
            }*/
            if(source_square>0){
                if(castling){   
                    prepareCastlingAnimation();
                      state = GameData.PREPARE_ANIMATION;
                }else if(move.destination > 20 && move.destination < 29 && 
                        position.human_pieces[source_square].value == Piece.PAWN){
                    promoteHumanPawn();                    
                }
            }else{
                if (gameEnded(GameData.HUMAN)) {
                    state = GameData.GAME_ENDED;
                    return;
                }
            }
            if(!castling && state != GameData.PROMOTING) 
                newHistoryPosition();
            if(castling) castling = false;
            //if(state != GameData.ANIMATING){                
            //}
         /*   for(int i=0; i<position.human_pieces.length; i++){
                                if(position.human_pieces[i] == null) continue;
                                System.out.print(position.human_pieces[i].value+" ");
                            }
                            System.out.println();*/            
        }
        board_pane.movingX += board_pane.deltaX;
        board_pane.movingY += board_pane.deltaY;
        board_pane.repaint();
    }

    /**
     * Method to promote a human pawn to another piece.
     */
    public void promoteHumanPawn(){        
        promotion_pane.location = move.destination;
        promotion_pane.index = position.board[move.destination];
        promotion_pane.setVisible(true);
        state = GameData.PROMOTING;
    }

    /**
     * Method to promote a computer pawn to another piece.
     */
    public void promoteComputerPawn(){
        int piece_index = position.board[move.source_location];
        position.computer_pieces[-piece_index] = new Piece(Piece.QUEEN,move.destination);
    }

    /**
     * Method to prepare the castling animation on the board.
     */
    public void prepareCastlingAnimation() {
        if (move.destination == 97 || move.destination == 96) {
            move.source_location = 98;
            move.destination -= 1;
        } else if (move.destination == 92 || move.destination == 93) {
            move.source_location = 91;
            move.destination += 1;
        }
    }

    /**
     * Method to add a new position to the movement history.
     */
    public void newHistoryPosition() {
        history_positions.add(new Position(position));
        history_count = history_positions.size() - 1;
        history_pane.repaint();
    }

    /**
     * Loads the images of the chess pieces for both players and stores them in the "images" HashMap.
     *
     * The resources are loaded from the "resource" object, which provides the path of each image file.
     *
     * Each piece image is stored with its corresponding integer value as key in the "images" HashMap.
     *
     * If the piece is a pawn, the key is added 10, indicating that it is a pawn on the second rank.
     *
     * The piece color is determined by the "is_white" boolean attribute.
     */
    public void loadPieceImages(){
        char[] resource_keys = {'p','n','b','r','q','k'};
        int[] images_keys = {Piece.PAWN,Piece.KNIGHT,Piece.BISHOP,Piece.ROOK,Piece.QUEEN,Piece.KING};
        try{
            for(int i=0; i<resource_keys.length; i++){             
                images.put(images_keys[i],ImageIO.read(resource.getResource((is_white?"w":"b")+resource_keys[i])));
                images.put(-images_keys[i],ImageIO.read(resource.getResource((is_white?"b":"w")+resource_keys[i])));   
                images.put(images_keys[i]+10,ImageIO.read(resource.getResource((is_white?"w":"b")+resource_keys[i]+'2')));
                images.put(-images_keys[i]+10,ImageIO.read(resource.getResource((is_white?"b":"w")+resource_keys[i]+'2'))); 
            }               
        }catch(IOException ex){
            ex.printStackTrace();
        }        
    }

    /**
     * Loads the board images from the resources and stores them in a HashMap with the corresponding key.
     *
     * The images are stored with the following keys: 
     * GameData.BOARD_IMAGE - the chessboard image 
     * GameData.BOARD_IMAGE2 - the history board image 
     * GameData.GLOW - the glow image used to highlight a selected piece 
     * GameData.GLOW2 - the glow image used to highlight a possible move 
     * GameData.HISTORY_TITLE - the history title image 
     * GameData.MYCHESSMATE - the MyChessMate logo image
     *
     */
    public void loadBoardImages(){
        try{ 
            images.put(GameData.BOARD_IMAGE,ImageIO.read(resource.getResource("chessboard")));
            images.put(GameData.BOARD_IMAGE2,ImageIO.read(resource.getResource("history_board")));
            images.put(GameData.GLOW,ImageIO.read(resource.getResource("glow")));
            images.put(GameData.GLOW2,ImageIO.read(resource.getResource("glow2")));            
            images.put(GameData.HISTORY_TITLE,ImageIO.read(resource.getResource("history_title")));
            images.put(GameData.MYCHESSMATE,ImageIO.read(resource.getResource("mychessmate")));
        }catch(IOException ex){
            ex.printStackTrace();
        }        
    }

    /**
     * Loads the menu icons into the program.
     *
     * The icons are stored in a hash map named icon_images using their associated keys.
     *
     */
    public void loadMenuIcons(){
        icon_images.put(GameData.NEW_BUTTON,new ImageIcon(resource.getResource("new_game")));
        icon_images.put(GameData.NEW_BUTTON2,new ImageIcon(resource.getResource("new_game_hover")));
        icon_images.put(GameData.QUIT_BUTTON,new ImageIcon(resource.getResource("quit")));
        icon_images.put(GameData.QUIT_BUTTON2,new ImageIcon(resource.getResource("quit_hover")));
        icon_images.put(GameData.HISTORY_BUTTON,new ImageIcon(resource.getResource("history")));
        icon_images.put(GameData.HISTORY_BUTTON2,new ImageIcon(resource.getResource("history_hover")));
        icon_images.put(GameData.ABOUT_BUTTON,new ImageIcon(resource.getResource("about")));
        icon_images.put(GameData.ABOUT_BUTTON2,new ImageIcon(resource.getResource("about_hover")));
        
        icon_images.put(GameData.FIRST_BUTTON,new ImageIcon(resource.getResource("first")));
        icon_images.put(GameData.FIRST_BUTTON2,new ImageIcon(resource.getResource("first_hover")));
        icon_images.put(GameData.NEXT_BUTTON,new ImageIcon(resource.getResource("next")));
        icon_images.put(GameData.NEXT_BUTTON2,new ImageIcon(resource.getResource("next_hover")));
        icon_images.put(GameData.PREV_BUTTON,new ImageIcon(resource.getResource("previous")));
        icon_images.put(GameData.PREV_BUTTON2,new ImageIcon(resource.getResource("previous_hover")));
        icon_images.put(GameData.LAST_BUTTON,new ImageIcon(resource.getResource("last")));
        icon_images.put(GameData.LAST_BUTTON2,new ImageIcon(resource.getResource("last_hover")));
    }

    /**
     * Displays a confirmation dialog asking the user if they want to quit the application. 
     * If the user clicks "Yes", the application will be terminated. 
     * If the user clicks "No", the dialog will be closed. 
     * If the user clicks the close button, the dialog will also be closed.
     */
    public void quit(){
        int option = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?", 
                    "MyChessmate1.1", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(option == JOptionPane.YES_OPTION)
            System.exit(0);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    /**
     * This method is the main entry point of the application. 
     * It sets the Look and Feel of the user interface to Nimbus, if it is installed on the system, 
     * and creates and displays an instance of the MyChessmate class. If Nimbus is not installed, 
     * it displays a warning message asking the user if they want to proceed without it.
     *
     * @param args An array of command-line arguments.
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean nimbusFound = false;
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if (info.getName().equals("Nimbus")) {
                            UIManager.setLookAndFeel(info.getClassName());
                            nimbusFound = true;
                            break;
                        }
                    }
                    if (!nimbusFound) {
                        int option = JOptionPane.showConfirmDialog(null,
                                "Nimbus Look And Feel not found\n"
                                + "Do you want to proceed?",
                                "Warning", JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                        if (option == JOptionPane.NO_OPTION) {
                            System.exit(0);
                        }
                    }
                    MyChessmate mcg = new MyChessmate();
                    // mcg.pack();
                    mcg.setLocationRelativeTo(null);
                    mcg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mcg.setResizable(false);
                    mcg.setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getStackTrace());
                    e.printStackTrace();
                }
            }
        });
    }
}
