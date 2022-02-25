/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 
package target;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.net.*;

public class ImagePanel extends JFrame{

    private BufferedImage image;

    /**
     *
     * @param s
     */
    public ImagePanel(String s) {
       try {
          image = ImageIO.read(new URL(s));
       } catch (IOException ex) {
            // handle exception...
       }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null); 
    }
   // see javadoc for more info on the parameters
}

