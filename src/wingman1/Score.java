package wingman1;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Danny
 */
public class Score {
    int score;
    String name;
    
    public int getScore(){
        return score;
        
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String newName){
        this.name=newName;
    }
    Score(int score, String name){
        this.score=score;
        this.name=name;
    }
    
    public String toString(){
        return name +" "+score;
    }
}
