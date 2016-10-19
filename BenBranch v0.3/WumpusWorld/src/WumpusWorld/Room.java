package WumpusWorld;


/**
 * Wumpus World using First Order Logic
 * Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */

public class Room {

    private char tile;  //g - gold, e - empty, w - wumpus, p - pit, o - obstacle
    private boolean breeze = false;
    private boolean stench = false;
    private boolean glitter = false;
    
    public Room() {
    }

    public void setRoom(char c) {
        tile = c;
    }
    
    public char getRoom(){
        return tile;
    }
    
    public void setGlitter(){
        glitter = true;
    }
    
    public void setBreeze(){
         breeze = true;
    }
    
    public void setStench(){
        stench = true;
    }
    public void removeStench(){
        stench = false;
    }
    
    public boolean getBreeze(){
        return breeze;
    }
    
    public boolean getStench(){
        return stench;
    }
    
    public boolean getGlitter(){
        return glitter;
    }
    
}