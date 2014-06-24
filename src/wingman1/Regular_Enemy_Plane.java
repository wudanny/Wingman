/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wingman1;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Vector;

/**
 *
 * @author Danny
 */
public class Regular_Enemy_Plane extends Enemy_Mini {

    Regular_Enemy_Plane(Vector image, int x, int y, int health, int speed, int reward, int collidingDamage, GameEvents event) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.health = health;
        this.damageTaken = 0;
        this.speed = speed;
        this.collidingDamage = collidingDamage;
        this.reward = reward;
        this.show = true;
        this.events = event;
        this.imageCounter = 0;
    }
    public void update(int w, int h) {
        if (isOutOfBound(w, h) || damageTaken >= health) {
            show = false;
        }

        if (show) {
            y += speed;
        }
        imageCounter = (imageCounter + 1) % image.size();
    }
    public void draw(Graphics g, ImageObserver obs) {
        if (show) {
            g.drawImage((Image) image.elementAt(imageCounter), x, y, obs);
        }
    }
}
