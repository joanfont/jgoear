/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author joanfont
 */
public class Play implements Runnable {
    
    private final BufferedInputStream bis;
    private final Player player;
    
    public Play(URL url) throws IOException, JavaLayerException{
        this.bis = new BufferedInputStream(url.openStream());
        this.player = new Player(this.bis);
        
    }
    
    private void play() throws JavaLayerException{
        this.player.play();
    }
    
    public void stop(){
        this.player.close();
    }

    @Override
    public void run() {
        try {
            this.play();
        } catch (JavaLayerException ex) {
        }
    }
    
}
