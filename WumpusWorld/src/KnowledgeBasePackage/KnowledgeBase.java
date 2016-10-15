
package KnowledgeBasePackage;
import WumpusWorld.*;
import InferenceEnginePackage.*;


/**
 *
 * @author benrhuman
 */
        

public class KnowledgeBase {
    
    KBRoom[][] KBMap;
    
    public KnowledgeBase(int worldSize){
        
    }
    
    public void updateKnowledgeBase(boolean[] percept, Location location){
        
    }
    
    public void setSafe(int x, int y){
        KBMap[x][y].unknown = false;
        KBMap[x][y].safe = true;
        KBMap[x][y].possibleWumpus = false;
        KBMap[x][y].possiblePit = false;
    }
    public void setKnownPit(int x, int y){
        KBMap[x][y].unknown = false;
        KBMap[x][y].knownPit = true;
        KBMap[x][y].possibleWumpus = false;
        KBMap[x][y].possiblePit = false;
    }
    public void setKnownWumpus(int x, int y){
        KBMap[x][y].unknown = false;
        KBMap[x][y].knownWumpus = true;
        KBMap[x][y].possibleWumpus = false;
        KBMap[x][y].possiblePit = false;
    }
    public void setObstacle(int x, int y){
        KBMap[x][y].unknown = false;
        KBMap[x][y].obstacle = true;
        KBMap[x][y].possibleWumpus = false;
        KBMap[x][y].possiblePit = false;
    }
    public void removeWumpusStench(int x, int y){
        
        try {
            KBMap[x + 1][y].stench = false;
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            KBMap[x - 1][y].stench = false;
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            KBMap[x][y+1].stench = false;
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            KBMap[x][y-1].stench = false;
        } catch (IndexOutOfBoundsException e) {

        }

    }
    
}
