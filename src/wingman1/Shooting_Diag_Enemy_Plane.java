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
public class Shooting_Diag_Enemy_Plane extends Shooting_Enemy_Plane {

    Shooting_Diag_Enemy_Plane(Vector image, int x, int y,int health, int speed,int reward, int collidingDamage ,int probabiltyPercentage, GameEvents event){
        super(image,x,y,health, speed,reward, collidingDamage, probabiltyPercentage,event);
    }

}
