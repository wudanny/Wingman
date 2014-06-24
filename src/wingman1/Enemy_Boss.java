/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wingman1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Danny
 */
public class Enemy_Boss extends Vehicle {

    Random enemySpawn;
    Random typeOfEnemy;
    Random shooting;
    int probabiltyPercentageShoot;
    int probabiltyPercentageSpawn;
    int numberOfEnemy;
    int deltaX;
    int deltaY;
    boolean ableToAttack;
    int widthOfHealthBar, heightOfHealthBar;

    Enemy_Boss(Vector image, int x, int y, int speed, GameEvents event, int health, int collidingDamage, int gapBetweenFire, int probabiltyPercentageShoot, int probabiltyPercentageSpawn, int numberOfEnemy, int heightBound, int widthBound) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.events = event;
        this.health = health;
        this.collidingDamage=collidingDamage;
        this.gapBetweenFire = gapBetweenFire;
        this.probabiltyPercentageShoot = probabiltyPercentageShoot;
        this.probabiltyPercentageSpawn = probabiltyPercentageSpawn;
        this.numberOfEnemy = numberOfEnemy;
        this.widthBound = widthBound;
        this.heightBound = heightBound;
        this.show = true;
        this.damageTaken = 0;
        this.waitForNextFire = 0;
        this.imageCounter = 0;
        this.deltaX = 0;
        this.deltaY = 1;
        this.ableToAttack = false;
        this.widthOfHealthBar = 2;
        this.heightOfHealthBar = 3;
        this.typeOfEnemy=new Random();
         this.enemySpawn=new Random();
          this.shooting=new Random();
    }

    void spawnEnemy(Integer type) {
        events.setCreateEnemy(type);
    }

    @Override
    public void update(int w, int h) {
        if (x + this.getImageWidth() >= widthBound || x <= 0) {
            deltaX = -deltaX;
        }

        x += (speed * deltaX);

        if (y <= heightBound && y >= -200) {
            y +=  deltaY;
        }

        if (damageTaken >= health) {
            events.setCreateExplosion(this);
            show = false;
        }
        if(ableToAttack){
            if(waitForNextFire>0){
                waitForNextFire--;
            }else if(shooting.nextInt(100)<probabiltyPercentageShoot){
            
                events.setCreateBullet(this);
            }
            
            if(enemySpawn.nextInt(500)<probabiltyPercentageSpawn){
                events.setCreateEnemy(typeOfEnemy.nextInt(numberOfEnemy));
            }
        }
        imageCounter = (imageCounter + 1) % image.size();
    }


    @Override
    public void update(Observable o, Object o1) {
        GameEvents ge = (GameEvents) o1;
        if (ge.getType() == 3) {

            String action = (String) ge.getEvent();
            if (action.equals("Attack")) {
                this.deltaY = 0;
                this.deltaX = 1;
                ableToAttack=true;
            }
        }
    }

    public void draw(Graphics g, ImageObserver obs) {
        if (show) {
            g.drawImage((Image) image.elementAt(imageCounter), x, y, obs);
            g.setColor(Color.MAGENTA);
            g.drawRect(x+(this.getImageWidth()/3), y, widthOfHealthBar*health, heightOfHealthBar);
            g.fillRect(x+(this.getImageWidth()/3), y, widthOfHealthBar*(health-damageTaken), heightOfHealthBar);
        }
    }
}
