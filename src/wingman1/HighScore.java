package wingman1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.swing.JApplet;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Danny
 */
public class HighScore extends JApplet implements Runnable {

    private LinkedList<Score> highscores;
    private Image background;
    int numberOfHighScore;
    private String fileName;
    private BufferedReader readHighScoreList;
    private PrintWriter writeHighScoreTable;
    Thread thread;
    private BufferedImage bimg;
    private String currentName, currentScore;
    ImageObserver observer;

    HighScore(String fileName, Image background, int numberOfHighScore) {
        this.numberOfHighScore = numberOfHighScore;
        highscores = new LinkedList<Score>();
        this.fileName = fileName;
        this.background = background;
        try {
            readHighScoreList = new BufferedReader(new FileReader(fileName));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void initialize() {

        observer = this;
        for(int i=0;i<numberOfHighScore;i++){
            try {
                getNextToken();
            } catch (IOException e) {
                break;
            }
            highscores.add(new Score(new Integer(currentScore),currentName));
        }
        
        try {
            readHighScoreList.close();
        } catch (Exception e) {
        }

    }

     public void getNextToken() throws IOException {
         try {
                StringTokenizer st = new StringTokenizer(readHighScoreList.readLine());
                currentName = st.nextToken();
                currentScore = st.nextToken();
            } catch (NoSuchElementException e) {
            System.out.println("***tokens file does not have 2 strings per line***");
            System.exit(1);
        } catch (NullPointerException ne) {
            // attempt to build new StringTokenizer when at end of file
            throw new IOException("***End of File***");
        }

        }
     
    public int addToHighScore(String name, int newScore) {
        Score current;
        int insertIndex = 0;
        ListIterator iterator = highscores.listIterator();
        while (iterator.hasNext()) {
            current = (Score) iterator.next();
            if (current.getScore() >= newScore) {
                insertIndex = iterator.nextIndex();
            }
        }
        highscores.add(insertIndex, new Score(newScore, name));
        return insertIndex;
    }

    public int getImageHeight() {
        return background.getHeight(null);
    }

    public int getImageWidth() {
        return background.getWidth(null);
    }

    public ListIterator scoreAt() {
        return highscores.listIterator();
    }

    public ListIterator scoreAt(int index) {
        return highscores.listIterator(index);
    }

    public void saveHighScore() {
        ListIterator highScoreList;
        Score currentScore;
        try {
            writeHighScoreTable = new PrintWriter(new FileOutputStream(fileName));
        } catch (Exception e) {
            System.out.println(e);
        }
        highScoreList = highscores.listIterator();
        while (highScoreList.hasNext()) {
            currentScore = (Score) highScoreList.next();
            writeHighScoreTable.println(currentScore.toString());
        }
        try {
            writeHighScoreTable.close();
        } catch (Exception e) {
        }

    }

    public void draw(Graphics g, ImageObserver obs) {
        int heightGap = getImageHeight() / (highscores.size() + 2);
        int counter = 0;
        Score curentscore;
        g.drawImage(background, 0, 0, obs);
        g.setColor(Color.yellow);
        g.drawString("HighScore Table",125, heightGap / 2);
        ListIterator scores = highscores.listIterator();
        while (scores.hasNext()) {
            curentscore = (Score) scores.next();
            g.drawString((counter + 1) + ". " + curentscore.toString(), 15, (counter + 1) * heightGap);
            counter++;
        }

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
        draw(g2, observer);
        g2.dispose();
        g.drawImage(bimg, 0, 0, this);
    }

    public void start() {
        this.requestFocusInWindow();

        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        Dimension d = getSize();
        //get the bounds for the screen
        thread.start();
    }

    public void run() {
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
}
