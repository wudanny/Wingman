/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wingman1;

import java.util.Vector;

/**
 *
 * @author Danny
 */
public class Enemy_Bullet extends Bullet{
    Enemy_Bullet(Vector image, int x, int y,int dx, int dy, int speed, int damage, GameEvents event){
        super(image,x,y,dx,dy,speed,damage,event);
    }
}
