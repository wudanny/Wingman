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
public class Shooting_Enemy_Plane extends Enemy_Mini {

    Random shooting;
    int probabiltyPercentage;

    Shooting_Enemy_Plane(Vector image, int x, int y, int health, int speed, int reward, int collidingDamage, int probabiltyPercentage, GameEvents event) {
        shooting = new Random();
        this.image = image;
        this.x = x;
        this.y = y;
        this.health = health;
        this.damageTaken = 0;
        this.speed = speed;
        this.collidingDamage = collidingDamage;
        this.reward = reward;
        this.probabiltyPercentage = probabiltyPercentage;
        this.show = true;
        this.events = event;
        this.imageCounter = 0;
    }

    @Override
    public void update(int w, int h) {
        if (isOutOfBound(w, h)) {
            show = false;
        }

        if (show) {
            y += speed;
        }

        if (shooting.nextInt(100) < probabiltyPercentage) {
            events.setCreateBullet(this);
        }

        imageCounter = (imageCounter + 1) % image.size();
    }

    public void draw(Graphics g, ImageObserver obs) {
        if (show) {
            g.drawImage((Image) image.elementAt(imageCounter), x, y, obs);
        }
    }
}
