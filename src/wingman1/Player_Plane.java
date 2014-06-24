/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wingman1;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 *
 * @author Danny
 */
public abstract class Player_Plane extends Vehicle {

    int weaponUpgrade;
    int life;
    int originalSpeed;
    int gapBetweenDeath;
    int waitForSpawn;
    boolean action[] = new boolean[5];
    int left = 0, right = 1, up = 2, down = 3, fire = 4;

    public int getWeaponUpgrade() {
        return weaponUpgrade;
    }


    public boolean isShow() {
        return (show && waitForSpawn == 0);
    }

    public int getLife() {
        return life;
    }

    void moveLeft() {
        if (x - speed >= 0) {
            x -= speed;
        }
    }

    void moveRight() {
        if (x + speed <= widthBound) {
            x += speed;
        }
    }

    void moveUp() {
        if (y - speed >= 0) {
            y -= speed;
        }
    }

    void moveDown() {
        if (y + speed <= heightBound) {
            y += speed;
        }
    }

    void fire() {
        if (waitForNextFire == 0) {
            waitForNextFire = gapBetweenFire;
            events.setCreateBullet(this);
        }
    }
    
        public void increaseDamageTaken(int damage) {
        damageTaken += damage;
        events.setPlayersHealth(this);
    }

    public void update(int w, int h) {
        if (action[left]) {
            this.moveLeft();
        }
        if (action[right]) {
            this.moveRight();
        }
        if (action[up]) {
            this.moveUp();
        }
        if (action[down]) {
            this.moveDown();
        }
        if (action[fire]) {
            this.fire();
        }

        if (waitForNextFire > 0) {
            waitForNextFire--;
        }
        if (waitForSpawn > 0) {
            waitForSpawn--;
        }
        if (damageTaken >= health) {
            waitForSpawn = gapBetweenDeath;
            life--;
            damageTaken = 0;
            events.setPlayersLife(this);
            events.setCreateExplosion(this);
            this.weaponUpgrade=0;
            this.speed=this.originalSpeed;

        }
        if (life <= 0) {
            show = false;
        }
        imageCounter = (imageCounter + 1) % image.size();


    }

    public void draw(Graphics g, ImageObserver obs) {
        if (show && waitForSpawn == 0) {
            g.drawImage((Image) image.elementAt(imageCounter), x, y, obs);
        }
    }
}
