/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mychessmate;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the About Pane of MyChessmate.
 *
 * It displays information about the program, such as its version, category, author, and date created.
 *
 * @author melvicYbanez
 */
public class AboutPane extends JPanel{

    /**
     * Class representing a panel displaying information about the project.
     */
    public AboutPane(){
        setLayout(new BorderLayout());
        JPanel northPane = new NorthPane();       
        JPanel centerPane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();        
        c.insets = new Insets(4,4,4,4);
        c.fill = GridBagConstraints.HORIZONTAL;        

        String[][] values = new String[][]{
            {"Project","MyChessmate "+GameData.VERSION},
            {"Category","Game"},
            {"Author","Melvic C. Ybañez"},
            {"Date created","June 2011"}
        };
        for(int i=0; i<values.length; i++){
            JLabel header = new JLabel(values[i][0]+": ");
            header.setFont(new Font(header.getFont().getName(),Font.BOLD,13));
            JLabel data = new JLabel(values[i][1]);
            c.gridx = 0;
            c.gridy = i;
            centerPane.add(header,c);
            c.gridx = 1;
            centerPane.add(data,c);
        }
        centerPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(northPane,BorderLayout.NORTH);
        add(centerPane,BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }    

    /**
     * Method that creates and displays the graphical user interface of the "About" window. 
     * The window displays information about the MyChessmate project.
     */
    public static void createAndShowUI(){                   
        JFrame f = new JFrame("AboutBox");
        AboutPane ap = new AboutPane();
        f.getContentPane().add(ap);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    /**
     * Panel showing a header in the "About" window of the MyChessmate application.
     */
    class NorthPane extends JPanel{
        NorthPane(){          
            JLabel label = new JLabel("About MyChessmate",JLabel.LEFT);
            label.setFont(new Font(label.getFont().getName(),Font.BOLD,15));
            label.setForeground(Color.decode("#9900AF"));
            add(label);
        }
        /**
         * Overrides the paintComponent method to draw a horizontal line at the bottom of the panel
         *
         * @param g the Graphics object used for painting
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = this.getWidth()-5;
            int height = this.getHeight() - 1;
            g.setColor(Color.decode("#9900FF"));
            g.drawLine(0, height, width, height);
        }
    }
}
