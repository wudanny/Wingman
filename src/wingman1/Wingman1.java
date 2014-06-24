
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package wingman;
package wingman1;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Vector;
import javax.swing.*;

/**
 *
 * @author Ilmi
 */
public class Wingman1 extends JApplet implements Runnable {
    //basic data for applet

    private Thread thread;
    //the bound of the screen
    int widthBound, heightBound;
    private int x = 0, move = 0;
    private BufferedImage bimg;
    int speed = 1;
    boolean gameOver, start;
    int numberOfPlayer;
    ImageObserver observer;
    //random generator to help create random events
    Random generator = new Random(1234567);
    Random events = new Random();
    //gameEvents controls the events of the game
    GameEvents gameEvents;
    //the background music to be looped during the game
    AudioClip backGroundMusic;
    //frame counter to keep track of the time in the game
    //alarm is for creating and setting up the boss after a given number of frame
    int frameCounter, bossAlarm1, bossAlarm2, level, framesPerSpawn, framesPerPowerUp;
    //is responsible for creating all game objects
    CreateInstance create;
    //creates the game board that holds the information about the game
    GameBoard board;
    //HighScore
    HighScore highscore;
    //data structure to hold all the game objects
    Vector<LinkedList> gameObjects;
    LinkedList environment;
    LinkedList players;
    LinkedList playerBullets;
    LinkedList enemy;
    LinkedList enemyBullets;
    LinkedList bosses;
    LinkedList explosion;
    LinkedList powerUp;
    
    //game over image
    Image gameoverImg = getSprite("Resources/gameOver.png");

    public void init() {
        setBackground(Color.white);
        gameOver = false;
        start = true;
        observer = this;
        highscore = new HighScore("highscore.txt", getSprite("Resources/score.png"), 9);
        //number of players playing
        numberOfPlayer = 2;
        //the frame counter and setting when should the boss arrive
        bossAlarm1 = 300;
        bossAlarm2 = 400;
        framesPerPowerUp = 100;
        gameEvents = new GameEvents();
        //create the create that creates all game objects
        create = new CreateInstance(10, 3);
        //create data structures for game objects
        environment = new LinkedList<Game_Object>();
        players = new LinkedList<Vehicle>();
        playerBullets = new LinkedList<Bullet>();
        enemyBullets = new LinkedList<Bullet>();
        enemy = new LinkedList<Enemy_Mini>();
        bosses = new LinkedList<Vehicle>();
        explosion = new LinkedList<Explosion>();
        powerUp = new LinkedList<PowerUp>();
        //adds to the Vector to better keep track of all the game objects
        gameObjects = new Vector<LinkedList>();
        gameObjects.add(environment);
        gameObjects.add(players);
        gameObjects.add(playerBullets);
        gameObjects.add(enemyBullets);
        gameObjects.add(enemy);
        gameObjects.add(bosses);
        gameObjects.add(explosion);
        gameObjects.add(powerUp);
        //set the background music
        backGroundMusic = getSound("Resources/background.wav");
        //add the create class to be an observer of gameevents
        gameEvents.addObserver(create);
        //allow key press
        KeyControl key = new KeyControl();
        addKeyListener(key);

        //allows the game window to be focused on when it opens
        setFocusable(true);
    }

    class CreateInstance implements Observer {
        //the bounds to prevents the players and boss from going out of

        //a random generator that chooses which player to target
        Random target;
        //data structures to hold all the images
        Vector<Vector<Image>> enemysImg, playersImg, bulletsImg, explosionsImg, bossImg, environmentImg, powerUpImg;
        //holds all the sounds of the explosion
        Vector<AudioClip> explosionSounds;
        //holds the icon for the number of lives a player has
        Vector<Image> livesImage;
        //the game board
        Image gameBoard;
        //the number of health and lives the player starts with
        int health, lives;
        int enemyHealth, enemyCollisionDamage, enemyBullletDamage, bossHealth, bossSpeed;

        CreateInstance(int health, int lives) {
            //set game objects health, damage, etc
            this.health = health;
            this.lives = lives;
            this.bossHealth = 30;
            this.enemyBullletDamage = 1;
            this.enemyCollisionDamage = 1;
            this.bossSpeed = 1;
            this.enemyHealth = 1;

            //random generator to help choose which side to shoot
            target = new Random();

            //initialize the vectors of images for each game object
            gameBoard = getSprite("Resources/bottom.png");
            enemysImg = new Vector<Vector<Image>>();
            playersImg = new Vector<Vector<Image>>();
            bulletsImg = new Vector<Vector<Image>>();
            explosionsImg = new Vector<Vector<Image>>();
            bossImg = new Vector<Vector<Image>>();
            environmentImg = new Vector<Vector<Image>>();
            explosionSounds = new Vector<AudioClip>();
            livesImage = new Vector<Image>();
            powerUpImg = new Vector<Vector<Image>>();
            //gets the images for all game object
            enemysImg.add(new Vector<Image>());
            enemysImg.elementAt(0).add(getSprite("Resources/enemy1_1.png"));
            enemysImg.elementAt(0).add(getSprite("Resources/enemy1_2.png"));
            enemysImg.elementAt(0).add(getSprite("Resources/enemy1_3.png"));
            enemysImg.add(new Vector<Image>());
            enemysImg.elementAt(1).add(getSprite("Resources/enemy2_1.png"));
            enemysImg.elementAt(1).add(getSprite("Resources/enemy2_2.png"));
            enemysImg.elementAt(1).add(getSprite("Resources/enemy2_3.png"));
            enemysImg.add(new Vector<Image>());
            enemysImg.elementAt(2).add(getSprite("Resources/enemy3_1.png"));
            enemysImg.elementAt(2).add(getSprite("Resources/enemy3_2.png"));
            enemysImg.elementAt(2).add(getSprite("Resources/enemy3_3.png"));
            enemysImg.add(new Vector<Image>());
            enemysImg.elementAt(3).add(getSprite("Resources/enemy4_1.png"));
            enemysImg.elementAt(3).add(getSprite("Resources/enemy4_2.png"));
            enemysImg.elementAt(3).add(getSprite("Resources/enemy4_3.png"));
            playersImg.add(new Vector<Image>());
            playersImg.elementAt(0).add(getSprite("Resources/myplane_1.png"));
            playersImg.elementAt(0).add(getSprite("Resources/myplane_2.png"));
            playersImg.elementAt(0).add(getSprite("Resources/myplane_3.png"));
            bulletsImg.add(new Vector<Image>());
            bulletsImg.elementAt(0).add(getSprite("Resources/bullet.png"));
            bulletsImg.add(new Vector<Image>());
            bulletsImg.elementAt(1).add(getSprite("Resources/bulletLeft.png"));
            bulletsImg.add(new Vector<Image>());
            bulletsImg.elementAt(2).add(getSprite("Resources/bulletright.png"));
            bulletsImg.add(new Vector<Image>());
            bulletsImg.elementAt(3).add(getSprite("Resources/enemybullet1.png"));
            bulletsImg.add(new Vector<Image>());
            bulletsImg.elementAt(4).add(getSprite("Resources/enemybullet2.png"));
            bossImg.add(new Vector<Image>());
            bossImg.elementAt(0).add(getSprite("Resources/boss.png"));
            explosionsImg.add(new Vector<Image>());
            explosionsImg.elementAt(0).add(getSprite("Resources/explosion1_1.png"));
            explosionsImg.elementAt(0).add(getSprite("Resources/explosion1_2.png"));
            explosionsImg.elementAt(0).add(getSprite("Resources/explosion1_3.png"));
            explosionsImg.elementAt(0).add(getSprite("Resources/explosion1_4.png"));
            explosionsImg.elementAt(0).add(getSprite("Resources/explosion1_5.png"));
            explosionsImg.elementAt(0).add(getSprite("Resources/explosion1_5.png"));
            explosionsImg.add(new Vector<Image>());
            explosionsImg.elementAt(1).add(getSprite("Resources/explosion2_1.png"));
            explosionsImg.elementAt(1).add(getSprite("Resources/explosion2_2.png"));
            explosionsImg.elementAt(1).add(getSprite("Resources/explosion2_3.png"));
            explosionsImg.elementAt(1).add(getSprite("Resources/explosion2_4.png"));
            explosionsImg.elementAt(1).add(getSprite("Resources/explosion2_5.png"));
            explosionsImg.elementAt(1).add(getSprite("Resources/explosion2_5.png"));
            environmentImg.add(new Vector<Image>());
            environmentImg.elementAt(0).add(getSprite("Resources/island1.png"));
            environmentImg.add(new Vector<Image>());
            environmentImg.elementAt(1).add(getSprite("Resources/island2.png"));
            environmentImg.add(new Vector<Image>());
            environmentImg.elementAt(2).add(getSprite("Resources/island3.png"));
            powerUpImg.add(new Vector<Image>());
            powerUpImg.elementAt(0).add(getSprite("Resources/powerup.png"));
            powerUpImg.add(new Vector<Image>());
            powerUpImg.elementAt(1).add(getSprite("Resources/healthbooast.png"));
            powerUpImg.add(new Vector<Image>());
            powerUpImg.elementAt(2).add(getSprite("Resources/speedBooast.png"));
            explosionSounds.add(getSound("Resources/snd_explosion1.wav"));
            explosionSounds.add(getSound("Resources/snd_explosion2.wav"));

            livesImage.add(getSprite("Resources/life.png"));
            livesImage.add(getSprite("Resources/life.png"));


        }

        @Override
        public void update(Observable o, Object o1) {
            GameEvents ge = (GameEvents) o1;

            if (ge.getType() == 0) {
                widthBound = (int) getSize().getWidth();
                heightBound = (int) getSize().getHeight() - gameBoard.getHeight(null) + 10;
                highscore.initialize();
                //set the height bound to be above the gameboard
                //creates the game objects to start the game with
                Vector playersHealth = new Vector<Integer>();
                playersHealth.add(health);
                playersHealth.add(health);
                Vector playersLives = new Vector<Integer>();
                playersLives.add(lives);
                playersLives.add(lives);
                //create the bottom board
                board = new GameBoard(playersHealth, playersLives, 0, livesImage, gameBoard, 0, heightBound);
                gameEvents.addObserver(board);
                //create the island
                environment.add(new Island(environmentImg.elementAt(0), 100, 100, speed, generator));
                environment.add(new Island(environmentImg.elementAt(1), 200, 400, speed, generator));
                environment.add(new Island(environmentImg.elementAt(2), 300, 200, speed, generator));
                //create the two player
                Player_Plane player1 = new Player_Plane_1(playersImg.elementAt(0),
                        150, heightBound - 60, 3, gameEvents, health, 30, lives, 10, 90, heightBound - 53, widthBound - playersImg.elementAt(0).lastElement().getWidth(null));
                players.add(player1);
                gameEvents.addObserver(player1);
                Player_Plane player2 = new Player_Plane_2(playersImg.elementAt(0),
                        450, heightBound - 60, 3, gameEvents, health, 30, lives, 10, 90, heightBound - 53, widthBound - playersImg.elementAt(0).lastElement().getWidth(null));
                players.add(player2);
                gameEvents.addObserver(player2);


            } else if (ge.getType() == 3) {
                //creates the boss object
                String action = (String) ge.getEvent();
                if (action.equals("Create")) {
                    Enemy_Boss boss = new Enemy_Boss(bossImg.elementAt(0), 300, -100, bossSpeed, gameEvents, bossHealth, 200, 100, 4, 1, 4, heightBound, widthBound);
                    bosses.add(boss);
                    gameEvents.addObserver(boss);

                }

            } else if (ge.getType() == 4) {
                //creates the explosions relative to each class
                Game_Object event = (Game_Object) ge.getEvent();
                if (event instanceof Enemy_Mini) {
                    //for non boss enemy
                    explosion.add(new Explosion(explosionsImg.elementAt(0), event.getX(), event.getY(), (AudioClip) explosionSounds.elementAt(0)));
                } else if (event instanceof Player_Plane) {
                    //for player 
                    explosion.add(new Explosion(explosionsImg.elementAt(1), event.getX(), event.getY(), (AudioClip) explosionSounds.elementAt(1)));

                } else if (event instanceof Enemy_Boss) {
                    //for boss
                    explosion.add(new Explosion(explosionsImg.elementAt(1), event.getX(), event.getY(), (AudioClip) explosionSounds.elementAt(1)));

                }


            } else if (ge.getType() == 5) {
                //creates each bullet relative to each class
                Game_Object plane = (Game_Object) ge.getEvent();
                if (plane instanceof Player_Plane) {
                    Player_Plane player = (Player_Plane) plane;
                    //depending on the upgrade or powerup the player gets, shoots the bullet the location of the player
                    if (player.getWeaponUpgrade() == 0) {
                        playerBullets.add(new Player_Bullet(bulletsImg.elementAt(0), (plane.getX() + (plane.getImageWidth() / 4)), plane.getY(), 0, -1, 4, 1, gameEvents));
                    } else if (player.getWeaponUpgrade() == 1) {
                        playerBullets.add(new Player_Bullet(bulletsImg.elementAt(0), plane.getX(), plane.getY(), 0, -1, 5, 2, gameEvents));
                        playerBullets.add(new Player_Bullet(bulletsImg.elementAt(0), (plane.getX() + (plane.getImageWidth()) / 2), plane.getY(), 0, -1, 5, 2, gameEvents));
                    } else if (player.getWeaponUpgrade() == 2) {
                        playerBullets.add(new Player_Bullet(bulletsImg.elementAt(1), plane.getX(), plane.getY(), -1, -1, 5, 2, gameEvents));
                        playerBullets.add(new Player_Bullet(bulletsImg.elementAt(0), (plane.getX() + (plane.getImageWidth() / 4)), plane.getY(), 0, -1, 5, 2, gameEvents));
                        playerBullets.add(new Player_Bullet(bulletsImg.elementAt(2), (plane.getX() + (plane.getImageWidth() / 2)), plane.getY(), 1, -1, 5, 2, gameEvents));
                    } else {
                        playerBullets.add(new Player_Bullet(bulletsImg.elementAt(1), plane.getX(), plane.getY(), -1, -1, 5, 2, gameEvents));
                        playerBullets.add(new Player_Bullet(bulletsImg.elementAt(0), (plane.getX()), plane.getY(), 0, -1, 5, 2, gameEvents));
                        playerBullets.add(new Player_Bullet(bulletsImg.elementAt(0), (plane.getX() + (plane.getImageWidth() / 4)), plane.getY(), 0, -1, 5, 2, gameEvents));
                        playerBullets.add(new Player_Bullet(bulletsImg.elementAt(2), (plane.getX() + (plane.getImageWidth() / 2)), plane.getY(), 1, -1, 5, 2, gameEvents));
                    }
                } else if (plane instanceof Shooting_Diag_Enemy_Plane) {
                    //bullets for the diagonally shooting enemy chooses between left diagonal or right diagonal
                    if (target.nextBoolean()) {
                        enemyBullets.add(new Enemy_Bullet(bulletsImg.elementAt(3), plane.getX(), plane.getY(), 1, 1, 3, enemyBullletDamage, gameEvents));
                    } else {
                        enemyBullets.add(new Enemy_Bullet(bulletsImg.elementAt(3), plane.getX(), plane.getY(), -1, 1, 3, enemyBullletDamage, gameEvents));
                    }
                } else if (plane instanceof Shooting_Enemy_Plane) {
                    //bullets for striaght shooting enemy
                    enemyBullets.add(new Enemy_Bullet(bulletsImg.elementAt(4), plane.getX(), plane.getY(), 0, 1, 3, enemyBullletDamage, gameEvents));
                } else if (plane instanceof Enemy_Boss) {
                    //bullets for the boss
                    if (target.nextBoolean()) {
                        enemyBullets.add(new Enemy_Bullet(bulletsImg.elementAt(4), plane.getX() + plane.getImageWidth() / 2, plane.getY() + plane.getImageHeight(), 0, 1, 3, enemyBullletDamage, gameEvents));
                    }
                    enemyBullets.add(new Enemy_Bullet(bulletsImg.elementAt(4), plane.getX() + plane.getImageWidth() / 2, plane.getY() + plane.getImageHeight(), 1, 1, 3, enemyBullletDamage, gameEvents));
                    enemyBullets.add(new Enemy_Bullet(bulletsImg.elementAt(4), plane.getX() + plane.getImageWidth() / 2, plane.getY() + plane.getImageHeight(), -1, 1, 3, enemyBullletDamage, gameEvents));
                    enemyBullets.add(new Enemy_Bullet(bulletsImg.elementAt(4), plane.getX() + plane.getImageWidth(), plane.getY() + plane.getImageHeight(), 0, 1, 3, enemyBullletDamage, gameEvents));
                    enemyBullets.add(new Enemy_Bullet(bulletsImg.elementAt(4), plane.getX(), plane.getY() + plane.getImageHeight(), 0, 1, 3, enemyBullletDamage, gameEvents));
                }

            } else if (ge.getType() == 6) {
                //creates the enemy planes 
                switch ((Integer) ge.getEvent()) {
                    case 0:
                        enemy.add(new Regular_Enemy_Plane(enemysImg.elementAt(0), 50, -50, enemyHealth, 1, 10, enemyCollisionDamage, gameEvents));
                        enemy.add(new Regular_Enemy_Plane(enemysImg.elementAt(0), 150, -50, enemyHealth, 1, 10, enemyCollisionDamage, gameEvents));
                        enemy.add(new Regular_Enemy_Plane(enemysImg.elementAt(0), 300, -50, enemyHealth, 1, 10, enemyCollisionDamage, gameEvents));
                        enemy.add(new Regular_Enemy_Plane(enemysImg.elementAt(0), 450, -50, enemyHealth, 1, 10, enemyCollisionDamage, gameEvents));
                        enemy.add(new Regular_Enemy_Plane(enemysImg.elementAt(0), 550, -50, enemyHealth, 1, 10, enemyCollisionDamage, gameEvents));
                        break;
                    case 1:
                        enemy.add(new Backward_Enemy_Plane(enemysImg.elementAt(3), 100, heightBound + 25, enemyHealth, 1, 10, enemyCollisionDamage + 1, gameEvents));
                        enemy.add(new Backward_Enemy_Plane(enemysImg.elementAt(3), 300, heightBound + 25, enemyHealth, 1, 10, enemyCollisionDamage + 1, gameEvents));
                        enemy.add(new Backward_Enemy_Plane(enemysImg.elementAt(3), 500, heightBound + 25, enemyHealth, 1, 10, enemyCollisionDamage + 1, gameEvents));
                        break;
                    case 2:
                        enemy.add(new Shooting_Enemy_Plane(enemysImg.elementAt(1), 100, -50, enemyHealth, 1, 10, 3, enemyCollisionDamage, gameEvents));
                        enemy.add(new Shooting_Enemy_Plane(enemysImg.elementAt(1), 200, -50, enemyHealth, 1, 10, 3, enemyCollisionDamage, gameEvents));
                        enemy.add(new Shooting_Enemy_Plane(enemysImg.elementAt(1), 300, -50, enemyHealth, 1, 10, 3, enemyCollisionDamage, gameEvents));
                        enemy.add(new Shooting_Enemy_Plane(enemysImg.elementAt(1), 400, -50, enemyHealth, 1, 10, 3, enemyCollisionDamage, gameEvents));
                        enemy.add(new Shooting_Enemy_Plane(enemysImg.elementAt(1), 500, -50, enemyHealth, 1, 10, 3, enemyCollisionDamage, gameEvents));
                        break;
                    case 3:
                        enemy.add(new Shooting_Diag_Enemy_Plane(enemysImg.elementAt(2), 300, -75, enemyHealth, 1, 10, 3, enemyCollisionDamage, gameEvents));
                        enemy.add(new Shooting_Diag_Enemy_Plane(enemysImg.elementAt(2), 300, -45, enemyHealth, 1, 10, 3, enemyCollisionDamage, gameEvents));
                        enemy.add(new Shooting_Diag_Enemy_Plane(enemysImg.elementAt(2), 300, -15, enemyHealth, 1, 10, 3, enemyCollisionDamage, gameEvents));
                        enemy.add(new Shooting_Diag_Enemy_Plane(enemysImg.elementAt(2), 300, 15, enemyHealth, 1, 10, 3, enemyCollisionDamage, gameEvents));
                        break;
                    default:
                        break;
                }


            } else if (ge.getType() == 10) {
                Integer powerUpType = (Integer) ge.getEvent();
                //creates powerup at random x coord and -50 y 
                if (powerUpType == 0) {
                    powerUp.add(new PowerUp(powerUpImg.elementAt(0), target.nextInt(widthBound), -50, 1, 0, -1, gameEvents));
                } else if (powerUpType == 1) {
                    powerUp.add(new PowerUp(powerUpImg.elementAt(1), target.nextInt(widthBound), -50, 1, 1, -1, gameEvents));
                } else if (powerUpType == 2) {
                    powerUp.add(new PowerUp(powerUpImg.elementAt(2), target.nextInt(widthBound), -50, 1, 2, -1, gameEvents));
                }
            } else if (ge.getType() == 12) {
                //each level increases the difficulty of the game either by increasing the number of enemy spawn, boss health/damage, enemy health/damage
                Integer difficuty = (Integer) ge.getEvent();
                switch (difficuty) {
                    case 1:
                        this.bossHealth = 30;
                        this.enemyBullletDamage = 1;
                        this.enemyCollisionDamage = 1;
                        this.bossSpeed = 1;
                        this.enemyHealth = 1;
                        framesPerSpawn = 350;
                        break;
                    case 2:
                        bossHealth += 10;
                        framesPerSpawn -= 15;
                        break;
                    case 3:
                        enemyHealth += 1;
                        enemyCollisionDamage += 1;
                        bossHealth += 10;
                        bossSpeed += 1;
                        break;
                    case 4:

                        bossHealth += 10;
                        framesPerSpawn -= 45;
                        break;
                    case 5:
                        bossHealth += 20;
                        framesPerSpawn -= 25;
                        bossSpeed += 1;
                        break;
                    case 6:
                        bossHealth += 20;
                        break;
                    case 7:
                        bossHealth += 30;
                        bossSpeed += 1;
                        framesPerSpawn -= 10;
                        break;
                    case 8:
                        enemyBullletDamage = +1;
                        enemyHealth += 1;
                        enemyCollisionDamage += 1;
                        bossHealth += 40;
                        break;
                    case 9:
                        bossHealth += bossHealth / 2;
                        bossSpeed += 1;
                        framesPerSpawn -= 45;
                        break;
                    case 10:
                        bossHealth += 60;
                        bossSpeed += 1;
                        framesPerSpawn -= 35;
                    case 11:
                        gameOver = true;
                        break;
                    default:
                        break;
                }

            }
        }
    }

    public class KeyControl extends KeyAdapter {
        //for keys that are held down

        public void keyPressed(KeyEvent e) {
            gameEvents.setMovementForPlayer(e);
        }
        //for keys that are released

        public void keyReleased(KeyEvent e) {
            gameEvents.setStopMovementForPlayer(e);
        }
    }

    public Image getSprite(String name) {
        URL url = Wingman1.class
                .getResource(name);
        Image img = getToolkit().getImage(url);


        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        return img;
    }
// generates a new color with the specified hue

    public void drawBackGroundWithTileImage(int w, int h, Graphics2D g2) {
        Image sea;
        sea = getSprite("Resources/water.png");
        int TileWidth = sea.getWidth(this);
        int TileHeight = sea.getHeight(this);

        int NumberX = (int) (w / TileWidth);
        int NumberY = (int) (h / TileHeight);

        Image Buffer = createImage(NumberX * TileWidth, NumberY * TileHeight);
        //Graphics BufferG = Buffer.getGraphics();



        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                g2.drawImage(sea, j * TileWidth, i * TileHeight + (move % TileHeight), TileWidth, TileHeight, this);
            }
        }
        move += speed;
    }

    public void gamePlay(int w, int h, Graphics2D g2) {
        //if the game just started
        if (start) {
            frameCounter = 0;
            level = 1;
            gameEvents.setIncreaseLevel(level);
            //create starting objects
            gameEvents.setGamePlay();
            start = false;
        } else if (!gameOver) {
            //draw all the objects
            drawObject(w, h, g2);
            //updates all the objects
            updateObject(w, h);
            //while there is no boss
            if (bosses.size() == 0) {
                //spawn enemy every framesPerSpawn frames
                if (frameCounter % framesPerSpawn == 0) {
                    gameEvents.setCreateEnemy(events.nextInt(4));

                }
            }
            //randomly create powerup in game
            if (events.nextInt(framesPerPowerUp) == 0) {
                gameEvents.setCreatePowerUp(events.nextInt(3));
            }
            //checks the collison of objects
            collision();
            //delete any objects that is no showing
            deleteObject();

            //the alarm for the boss
            if (frameCounter >= bossAlarm1) {
                //create boss
                if (frameCounter == bossAlarm1) {
                    gameEvents.setUpBoss("Create");
                }
                //allows the boss to start moving vertically and starts shoooting
                if (frameCounter == bossAlarm2) {
                    gameEvents.setUpBoss("Attack");
                }
                //if the boss dies, increase level and reset counter
                if (bosses.size() == 0) {
                    frameCounter = 0;
                    level += 1;
                    gameEvents.setIncreaseLevel(level);
                }
            }
            //if one player dies, the game is over
            if (players.size() < numberOfPlayer) {
                frameCounter = 0;
                highscore.addToHighScore("Player", board.getScore());
                displayHighScore();
                gameOver = true;
            }
        } else {
            g2.drawImage(gameoverImg, 0, 0, observer);
            if (frameCounter >= 300) {

                this.restart();
            }
        }

        frameCounter++;
    }

    void updateObject(int w, int h) {
        Game_Object currentObject;
        ListIterator currentIterator;
        //goes through all the objects and call their update method
        for (int i = 0; i < gameObjects.size(); i++) {
            currentIterator = gameObjects.elementAt(i).listIterator();
            while (currentIterator.hasNext()) {
                currentObject = (Game_Object) currentIterator.next();
                currentObject.update(w, h);
            }
        }

    }

    void drawObject(int w, int h, Graphics2D g2) {
        Game_Object currentObject;
        ListIterator currentIterator;
        //draw the background water images
        drawBackGroundWithTileImage(w, h, g2);

        //goes through all the objects and draw them
        for (int i = 0; i < gameObjects.size(); i++) {
            currentIterator = gameObjects.elementAt(i).listIterator();
            while (currentIterator.hasNext()) {
                currentObject = (Game_Object) currentIterator.next();
                currentObject.draw(g2, observer);
            }
        }
        //draw the board last so no objects can be drawn on top of the board
        board.draw(g2, observer);
    }

    void deleteObject() {
        Game_Object currentObject;
        ListIterator currentIterator;
        //goes through all the objects and check if it is showing, if not remove them from their list
        for (int i = 0; i < gameObjects.size(); i++) {
            currentIterator = gameObjects.elementAt(i).listIterator();
            while (currentIterator.hasNext()) {
                currentObject = (Game_Object) currentIterator.next();
                if (!currentObject.show) {
                    currentIterator.remove();
                }
            }
        }
    }

    void collision() {
        Player_Plane playersObject;
        Bullet bulletsObject;
        Enemy_Mini enemyObject;
        Enemy_Boss bossObject;
        PowerUp powerUpObject;
        ListIterator currentIterator;
        ListIterator collisionIterator;
        Rectangle currentRec, collisionRec;

        //checks collison between enemyplane and player bullets
        currentIterator = enemy.listIterator();
        while (currentIterator.hasNext()) {
            collisionIterator = playerBullets.listIterator();
            enemyObject = (Enemy_Mini) currentIterator.next();
            currentRec = enemyObject.boundary();
            while (collisionIterator.hasNext()) {
                bulletsObject = (Bullet) collisionIterator.next();
                collisionRec = bulletsObject.boundary();
                if (bulletsObject.isShow() && currentRec.intersects(collisionRec)) {
                    enemyObject.increaseDamageTaken(bulletsObject.getDamage());
                    bulletsObject.setShow(false);
                    if (enemyObject.isDestroyed()) {
                        enemyObject.destroy();
                    }
                }
            }
        }

        //enemyplane and playerplane
        currentIterator = enemy.listIterator();
        while (currentIterator.hasNext()) {
            collisionIterator = players.listIterator();
            enemyObject = (Enemy_Mini) currentIterator.next();
            currentRec = enemyObject.boundary();
            while (collisionIterator.hasNext()) {
                playersObject = (Player_Plane) collisionIterator.next();
                collisionRec = playersObject.boundary();
                if (playersObject.isShow() && currentRec.intersects(collisionRec)) {
                    enemyObject.increaseDamageTaken(playersObject.getCollidingDamage());
                    playersObject.increaseDamageTaken(enemyObject.getCollidingDamage());
                    if (enemyObject.isDestroyed()) {
                        enemyObject.destroy();
                    }
                }
            }
        }
        //players and enemy bullet
        currentIterator = players.listIterator();
        while (currentIterator.hasNext()) {
            collisionIterator = enemyBullets.listIterator();
            playersObject = (Player_Plane) currentIterator.next();
            currentRec = playersObject.boundary();
            while (collisionIterator.hasNext()) {
                bulletsObject = (Bullet) collisionIterator.next();
                collisionRec = bulletsObject.boundary();
                if (bulletsObject.isShow() && currentRec.intersects(collisionRec)) {
                    bulletsObject.setShow(false);
                    playersObject.increaseDamageTaken(bulletsObject.getDamage());
                }
            }
        }
        //players and powerup
        currentIterator = players.listIterator();
        while (currentIterator.hasNext()) {
            collisionIterator = powerUp.listIterator();
            playersObject = (Player_Plane) currentIterator.next();
            currentRec = playersObject.boundary();
            while (collisionIterator.hasNext()) {
                powerUpObject = (PowerUp) collisionIterator.next();
                collisionRec = powerUpObject.boundary();
                if (powerUpObject.isShow() && currentRec.intersects(collisionRec)) {
                    powerUpObject.setShow(false);
                    gameEvents.setApplyPowerUp(powerUpObject, playersObject);
                }
            }
        }

        //boss and player bullets
        currentIterator = bosses.listIterator();
        while (currentIterator.hasNext()) {
            collisionIterator = playerBullets.listIterator();
            bossObject = (Enemy_Boss) currentIterator.next();
            currentRec = bossObject.boundary();
            while (collisionIterator.hasNext()) {
                bulletsObject = (Bullet) collisionIterator.next();
                collisionRec = bulletsObject.boundary();
                if (bulletsObject.isShow() && currentRec.intersects(collisionRec)) {
                    bossObject.increaseDamageTaken(bulletsObject.getDamage());
                    bulletsObject.setShow(false);
                }
            }
        }
        //boss and players
        currentIterator = bosses.listIterator();
        while (currentIterator.hasNext()) {
            collisionIterator = players.listIterator();
            bossObject = (Enemy_Boss) currentIterator.next();
            currentRec = bossObject.boundary();
            while (collisionIterator.hasNext()) {
                playersObject = (Player_Plane) collisionIterator.next();
                collisionRec = playersObject.boundary();
                if (playersObject.isShow() && currentRec.intersects(collisionRec)) {
                    playersObject.increaseDamageTaken(bossObject.getCollidingDamage());
                    bossObject.increaseDamageTaken(playersObject.getCollidingDamage());

                }
            }
        }
    }

    //displays the highscore and saves it
    public void displayHighScore() {
        JFrame f = new JFrame("HighScore");
        f.addWindowListener(new WindowAdapter() {
        });
        f.getContentPane().add("Center", highscore);
        f.pack();
        f.setSize(new Dimension(highscore.getImageWidth(), highscore.getImageHeight()));
        f.setVisible(true);
        f.setResizable(false);
        highscore.start();
        highscore.saveHighScore();
    }

    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        Graphics2D g2 = createGraphics2D(d.width, d.height);
        gamePlay(d.width, d.height, g2);
        g2.dispose();
        g.drawImage(bimg, 0, 0, this);
    }

    public void start() {
        this.requestFocusInWindow();
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public void run() {
        backGroundMusic.loop();
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();

            try {
                thread.sleep(21);
            } catch (InterruptedException e) {
                break;
            }

        }


        // thread = null;
    }

    //delete all objects and restart from level 1
    public void restart() {
        level = 1;
        gameEvents.setIncreaseLevel(level);
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.elementAt(i).clear();
        }
        start = true;
        gameOver = false;
        frameCounter = 0;
    }

    public AudioClip getSound(String source) {
        URL url = getClass().getResource(source);
        return Applet.newAudioClip(url);
    }

    public static void main(String argv[]) {
        final Wingman1 demo = new Wingman1();
        demo.init();
        JFrame f = new JFrame("Scrolling Shooter");
        f.addWindowListener(new WindowAdapter() {
        });
        f.getContentPane().add("Center", demo);
        f.pack();
        f.setSize(new Dimension(640, 480));
        f.setVisible(true);
        f.setResizable(false);
        demo.start();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
