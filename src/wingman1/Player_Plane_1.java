/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wingman1;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Vector;

/**
 *
 * @author Danny
 */
public class Player_Plane_1 extends Player_Plane {

    Player_Plane_1(Vector image, int x, int y, int speed, GameEvents event, int health, int gapBetweenFire, int life, int collidingDamage, int gapBetweenDeath, int heightBound, int widthBound) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.originalSpeed = speed;
        this.speed=originalSpeed;
        this.events = event;
        this.health = health;
        this.gapBetweenFire = gapBetweenFire;
        this.life = life;
        this.collidingDamage = collidingDamage;
        this.gapBetweenDeath = gapBetweenDeath;
        this.widthBound = widthBound;
        this.heightBound = heightBound;
        this.show = true;
        this.damageTaken = 0;
        this.waitForNextFire = 0;
        this.waitForSpawn = 0;
        this.imageCounter = 0;
        this.weaponUpgrade = 0;
    }

    @Override
    public void update(Observable o, Object o1) {
        GameEvents ge = (GameEvents) o1;
        if (show || this.waitForSpawn == 0) {
            if (ge.getType() == 1) {
                KeyEvent e = (KeyEvent) ge.event;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        action[left] = true;
                        break;
                    case KeyEvent.VK_D:
                        action[right] = true;
                        break;
                    case KeyEvent.VK_W:
                        action[up] = true;
                        break;
                    case KeyEvent.VK_S:
                        action[down] = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        action[fire] = true;
                        break;
                    default:
                        break;

                }
            } else if (ge.getType() == 2) {
                KeyEvent e = (KeyEvent) ge.event;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        action[left] = false;
                        break;
                    case KeyEvent.VK_D:
                        action[right] = false;
                        break;
                    case KeyEvent.VK_W:
                        action[up] = false;
                        break;
                    case KeyEvent.VK_S:
                        action[down] = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        action[fire] = false;
                        break;
                    default:
                        break;

                }
            }else if(ge.getType() == 11 && ge.player instanceof Player_Plane_1){
                PowerUp e = (PowerUp) ge.event;
                if(e.getTypeOfPower()==0){
                    weaponUpgrade+=1;
                }else if(e.getTypeOfPower()==1){
                    life+=1;
                    events.setPlayersLife(this);
                }else if(e.getTypeOfPower()==2){
                    speed+=1;
                }
                
            }
        }
    }
}
