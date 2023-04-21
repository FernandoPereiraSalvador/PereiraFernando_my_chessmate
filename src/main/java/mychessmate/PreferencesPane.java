/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mychessmate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A class representing a JFrame window for setting game preferences such as level and color.
 *
 * @author Melvic
 * @author Documentation: Fernando Pereira
 */
public class PreferencesPane extends JFrame implements ActionListener{
     JSlider levelSlider;
     JRadioButton white_button;
     JRadioButton black_button;
     JButton ok;
     JButton cancel;
     final static int WHITE = 0;
     final static int BLACK = 1;
     MyChessmate chessmate;

    /**
     * This class creates a preferences pane that allows users to customize various game options.
     *
     * @param chessmate an instance of the MyChessmate class used for reference and manipulation of game settings.
     */
    public PreferencesPane(MyChessmate chessmate){
        super("Options");
        this.chessmate = chessmate;
        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.add(createLevelPane(),BorderLayout.NORTH);
        mainPane.add(createColorPane(),BorderLayout.CENTER);
        mainPane.add(createButtonPane(),BorderLayout.SOUTH);
        mainPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setContentPane(mainPane);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ok.addActionListener(this);
        cancel.addActionListener(this);
    }

    /**
     * This method creates a JPanel containing a JSlider that allows users to adjust the game difficulty level.
     *
     * @return a JPanel containing the level slider.
     */
    public JPanel createLevelPane(){
        levelSlider = new JSlider(JSlider.HORIZONTAL,1,5,4);
        JPanel levelPane = new JPanel();        
        levelSlider.setMajorTickSpacing(1);
        levelSlider.setPaintTicks(true);
        levelSlider.setPaintLabels(true);
        levelPane.add(levelSlider);
        levelPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5,5,5,5),
                BorderFactory.createTitledBorder("Level")));
        return levelPane;
    }

    /**
     * This method creates a JPanel object containing two JRadioButtons that allow users to select their preferred game piece color.
     *
     * @return JPanel object containing the color selector JRadioButtons
     */
    public JPanel createColorPane(){
        JPanel colorPane = new JPanel(new GridLayout(1,2));
        white_button = new JRadioButton("White",true);
        black_button = new JRadioButton("Black");
        ButtonGroup group = new ButtonGroup();
        group.add(white_button);
        group.add(black_button);
        colorPane.add(white_button);
        colorPane.add(black_button);
        colorPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5,5,5,5),
                BorderFactory.createTitledBorder("Color")));
        return colorPane;
    }

    /**
     * This method creates a JPanel object containing two JButtons for confirming or cancelling changes made in the preferences pane.
     *
     * @return JPanel object containing the confirm and cancel buttons.
     */
    public JPanel createButtonPane(){
        JPanel buttonPane = new JPanel(new BorderLayout());
        JPanel pane = new JPanel(new GridLayout(1,2,5,0));
        pane.add(ok = new JButton("OK"));
        pane.add(cancel = new JButton("Cancel"));
        buttonPane.add(pane,BorderLayout.EAST);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return buttonPane;
    }

    /**
     * This method is called when a user clicks on one of the buttons in the preferences pane. It either confirms the changes made and starts a new game, or cancels the changes and hides the preferences pane.
     *
     * @param e ActionEvent object representing the user's action.
     */
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == ok){
            chessmate.state = GameData.GAME_ENDED;
            chessmate.newGame();           
        }
        setVisible(false);
    }
}
