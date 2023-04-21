package mychessmate;


import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Resource class that loads properties file for MyChessmate game
 *
 * @author Melvic
 * @author Documentation: Fernando Pereira
 */
public class Resource {

    /**
     * The Resource class provides access to the resource bundle containing the strings used in the MyChessmate game.
     */
    protected static ResourceBundle resources;
    static{
        try{
            resources = ResourceBundle.getBundle("mychessmate.res.MyChessmateProperties",Locale.getDefault());
        }catch(Exception e){
            System.out.println("Mychessmate properties not found");
            javax.swing.JOptionPane.showMessageDialog(null,
                    "MyChessmate properties not found",
                    "Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * This method returns a string representation of the resource identified by the given key.
     *
     * @param key the key that identifies the desired resource.
     * @return a string representation of the resource identified by the given key, or null if the resource was not found.
     */
    public String getResourceString(String key){
        String str;
        try{
            str = resources.getString(key);
        }catch(Exception e){
            str = null;
        }
        return str;
    }

    /**
     * Returns the URL of the resource with the given key.
     *
     * @param key the key of the resource
     * @return the URL of the resource, or null if the resource is not found
     */
    protected URL getResource(String key){
        String name = getResourceString(key);
        if(name != null){
            URL url = this.getClass().getResource(name);
            return url;
        }
        return null;
    }
}
