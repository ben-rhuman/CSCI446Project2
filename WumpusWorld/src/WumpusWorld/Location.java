package WumpusWorld;

 /*
 * Wumpus World using First Order Logic
 * Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */  


public class Location {  //Class used for keeping track of Agent and wumpus locations

    public int i;
    public int j;
    public int dir; //used only for adjacent squares and finding which direction a previous spot came from
    
    public Location(int i, int j) {
        this.i = i;
        this.j = j;
    }
    
    public void setLocation(int i, int j){
        this.i = i;
        this.j = j;
    }
    
    public void setDirection(int d){
     dir = d;   
    }

}