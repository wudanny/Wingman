package wingman1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import wingman1.GameEvents;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Danny
 */
public class GameBoard implements Observer {

    Vector<Integer> playersHealth;
    Vector<Integer> playersDamageTaken;
    Vector<Integer> playersLives;
    Integer score;
    Vector<Image> livesImages;
    Image gameBoard;
    int x, y;
    int widthOfHealthBar, heightOfHealthBar;

    GameBoard(Vector playersHealth, Vector playersLives, int score, Vector livesImages, Image gameBoard, int x, int y) {
        this.playersHealth = playersHealth;
        this.playersDamageTaken = new Vector<Integer>();
        playersDamageTaken.add(0);
        playersDamageTaken.add(0);
        this.playersLives = playersLives;
        this.score = score;
        this.livesImages = livesImages;
        this.gameBoard = gameBoard;
        this.x = x;
        this.y = y;
        this.widthOfHealthBar = 100;
        this.heightOfHealthBar = 10;
    }

    public int getBoardImageHeight() {
        return gameBoard.getHeight(null);
    }

    public int getBoardImageWidth() {
        return gameBoard.getWidth(null);
    }
    public int getLifeImageHeight(int playerNumber) {
        return livesImages.elementAt(playerNumber).getHeight(null);
    }

    public int getLifeImageWidth(int playerNumber) {
        return livesImages.elementAt(playerNumber).getWidth(null);
    }
    public int getScore(){
        return score;
    }
    @Override
    public void update(Observable o, Object o1) {
        GameEvents ge = (GameEvents) o1;

        if (ge.getType() == 7) {
            Player_Plane player = (Player_Plane) ge.getEvent();
            if (player instanceof Player_Plane_1) {
                playersDamageTaken.set(0, player.getDamageTaken());
            } else if (player instanceof Player_Plane_2) {
                playersDamageTaken.set(1, player.getDamageTaken());
            }
        } else if (ge.getType() == 8) {
            Player_Plane player = (Player_Plane) ge.getEvent();
            if (player instanceof Player_Plane_1) {
                playersLives.set(0, player.getLife());
                playersDamageTaken.set(0, player.getDamageTaken());
            } else if (player instanceof Player_Plane_2) {
                playersLives.set(1, player.getLife());
                playersDamageTaken.set(1, player.getDamageTaken());
            }
        } else if (ge.getType() == 9) {
             Integer reward = (Integer) ge.getEvent();
             score+=reward;
        }
    }

    public void draw(Graphics g, ImageObserver obs) {
        int gapBetweenBars =-3+ this.getBoardImageHeight() / 4;
        int healthScale;
        g.drawImage(gameBoard, x, y, obs);
        
        for(int i=0;i<playersLives.elementAt(0);i++){
        g.drawImage(livesImages.elementAt(0), x+300+(i*getLifeImageWidth(0)), y + gapBetweenBars-10, obs);
    }
        for(int i=0;i<playersLives.elementAt(1);i++){
        g.drawImage(livesImages.elementAt(1), x+300+(i*getLifeImageWidth(1)), y + 3*gapBetweenBars-10, obs);
    }
        g.setColor(Color.ORANGE);
        g.drawString(score.toString(), x + 250, y + 25);
        g.setColor(Color.PINK);
        g.drawString("Player 1", x + 20, y + gapBetweenBars - 5);
        healthScale = widthOfHealthBar * (playersDamageTaken.elementAt(0)) / playersHealth.elementAt(0);
        g.drawRect(x + 20, y + gapBetweenBars, widthOfHealthBar, heightOfHealthBar);
        g.fillRect(x + 20, y + gapBetweenBars, widthOfHealthBar - healthScale, heightOfHealthBar);
        g.setColor(Color.BLUE);
        g.drawString("Player 2", x + 20, y + 3 * gapBetweenBars - 5);
        healthScale = widthOfHealthBar * (playersDamageTaken.elementAt(1)) / playersHealth.elementAt(1);
        g.drawRect(x + 20, y + 3 * gapBetweenBars, widthOfHealthBar, heightOfHealthBar);
        g.fillRect(x + 20, y + 3 * gapBetweenBars, widthOfHealthBar - healthScale, heightOfHealthBar);

    }
}
