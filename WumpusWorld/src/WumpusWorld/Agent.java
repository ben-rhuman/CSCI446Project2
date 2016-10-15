package WumpusWorld;

import WumpusWorld.Location;
import WumpusWorld.Map;
import static java.lang.Math.abs;
import KnowledgeBasePackage.KnowledgeBase;
import InferenceEnginePackage.InferenceEngine;
import java.util.ArrayList;

/*
 * Wumpus World using First Order Logic
 * Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */

public class Agent {

    private int payOff;
    private int direction; //1 = North, 2 = East, 3 = South, 4 = West
    private int moveCounter;
    private Location currentLocation;
    //InferenceEngine iEngine;
    private int arrowCount;
    private Map worldMap;
    private KnowledgeBase KB;
    private InferenceEngine IE;

    public Agent(int x, int y, int numGold, int direction, Map worldMap) {  //Direction in our case should always be 2 (i.e. East) at start.
        currentLocation.i = x;
        currentLocation.j = y;
        this.payOff = numGold;
        this.direction = direction;
        this.worldMap = worldMap;
        KB = new KnowledgeBase(worldMap.size);
        IE = new InferenceEngine();
        boolean[] percept;
        percept = worldMap.checkPerceptAtLocation(currentLocation);
    }

        //////////////////////// Agent Action Methods //////////////////////////////////
    private boolean[] move() { //Moves the agent one space in the direction its facing.
        return worldMap.move(currentLocation, direction);  //Returns a list of 7 percepts
    }

    private void turnLeft() { // Rotates the agent 90 degrees to the left                  
        if (direction == 1) {
            direction = 4;
        } else {
            direction--;
        }
        payOff += 10;
    }

    private void turnRight() { // Rotates the agent 90 degrees to the right
        if (direction == 4) {
            direction = 1;
        } else {
            direction++;
        }
        payOff += 10;
    }

    private boolean shootArrow() {  //Shoots an arrow in the direction the agent is facing
        if (arrowCount <= 0) {
            return false;
        }
        arrowCount--;
        return worldMap.shootArrow(currentLocation, direction);
    }

        //////////////////////// End Agent Action Methods //////////////////////////////////
    
    
    
    //////////////////////////// Agent - Inference Engine Methods ////////////////////////////
    
    private void TELL(boolean[] percept) {
        IE.TELL(percept, currentLocation, direction);   // Tells the IE what the current percepts are
    }
    
    private ArrayList ASK(){
        return IE.ASK();  //Asks the IE what move it should take. 
    }
}
