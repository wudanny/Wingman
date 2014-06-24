/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wingman1;

/**
 *
 * @author Danny
 */
public abstract class Enemy_Mini extends Game_Object {

    int collidingDamage;
    int reward;
    int health;
    int damageTaken;

    public boolean isDestroyed() {
        return damageTaken >= health;
    }

    public void destroy() {
        show = false;
        events.setScore(reward);
        events.setCreateExplosion(this);
    }

    public int getHealth() {
        return health;
    }

    public void increaseDamageTaken(int damage) {
        damageTaken += damage;
    }

    public int getCollidingDamage() {
        return collidingDamage;
    }

    public int getReward() {
        return reward;
    }


}
