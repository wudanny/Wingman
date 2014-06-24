/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wingman1;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 *
 * @author Danny
 */
public class GameEvents extends Observable {

    int type;
    Object player;
    Object event;

    public void setGamePlay() {
        type = 0; // let's assume this mean key input. Should use CONSTANT value for this
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setMovementForPlayer(KeyEvent e) {
        type = 1; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setStopMovementForPlayer(KeyEvent e) {
        type = 2; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setUpBoss(String e) {
        type = 3; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setCreateExplosion(Game_Object e) {
        type = 4; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);

    }

    public void setCreateBullet(Game_Object e) {
        type = 5; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setCreateEnemy(Integer e) {
        type = 6; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setPlayersHealth(Game_Object e) {
        type = 7; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setPlayersLife(Game_Object e) {
        type = 8; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setScore(Integer e) {
        type = 9; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setCreatePowerUp(Integer e) {
        type = 10; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setApplyPowerUp(Game_Object e, Game_Object player) {
        type = 11; // let's assume this mean key input. Should use CONSTANT value for this
        this.player=player;
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }
    
    
    public void setIncreaseLevel(Integer e) {
        type = 12; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }
    
    public int getType() {
        return type;
    }

    public Object getEvent() {
        return event;
    }
}
