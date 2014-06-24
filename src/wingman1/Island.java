/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wingman1;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Danny
 */
public class Island extends Game_Object {

    int x, y, speed;
    Random gen;

    Island(Vector image, int x, int y, int speed, Random gen) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.gen = gen;
        this.show = true;
        this.imageCounter=0;
    }

    public void update(int w, int h) {
        if (show) {
            y += speed;
            if (y >= h) {
                y = -100;
                x = Math.abs(gen.nextInt() % (w - 30));
            }
            imageCounter=(imageCounter+1)%image.size();
        }
    }

    public void draw(Graphics g, ImageObserver obs) {
        if (show) {
            g.drawImage((Image)image.elementAt(imageCounter), x, y, obs);
        }
    }

}
