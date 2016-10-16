
package InferenceEnginePackage;

import WumpusWorld.*;
import KnowledgeBasePackage.*;
import java.util.ArrayList;


public class InferenceEngine {

    private KnowledgeBase KB;
    private Location agentLocation;
    private int agentDirection;
    private ArrayList<Integer> plan = new ArrayList<Integer>();

    public InferenceEngine(int size) {
        KB = new KnowledgeBase(size);
    }

    public void TELL(boolean[] percept, Location location, int direction) {  // Allows tha agent to tell the IE about its percepts
        agentLocation = location;
        agentDirection = direction;

        updateKnowledgeBase(percept);
    }

    public ArrayList ASK() {  //Allows the IE to tell the agent a list of moves to take
        plan.clear();
        PLAN(); //formulates the list of actions
        return plan;
    }

    private void PLAN() { //gives a list of actions for the agent to do 

        //(1) turn left 
        //(2) turn right
        //(3) forward
        //(4) grab
        //(5) shoot
        
        if (KB.getRoomState(agentLocation.i, agentLocation.j).glitter) { //if the agent is near gold
            plan.add(4);
            return;
        }

        if (!KB.wump.isEmpty()) { //if there is a known wumpus build a plan to kill it
            Location w = new Location(KB.wump.get(0).i, KB.wump.get(0).j);
            
            if (planToHunt(w)) { //if we can make a plan to hunt the wumpus
                
                KB.wump.add(KB.wump.get(0)); //move that wumpus to the back of the list
                KB.wump.remove(0); //remove it from the front
                return;
            } else {
                plan.clear();
            }
        }
        
        //otherwise we plan to move to an unvisited spot
        
        return;
    }
    

    private boolean planToHunt(Location w) {

        if ((agentLocation.i == w.i || agentLocation.j == w.j) && !KB.obstacleInWay(agentLocation, w)) { //base case if can shoot wumpus

            int dir = agentDirection;

            while (!KB.rightDirection(agentLocation, dir, w)) { //while the agent is not pointing towards the wumpus
                dir++;
                plan.add(2);
            }
            plan.add(5);
            return true;
        }
        
        
        //build the path towards that wumpus
        //return false?
        return false;
    }

    private ArrayList planToExplore(int x, int y) {
        ArrayList<Integer> plan = new ArrayList<Integer>();
        return plan;
    }

//(0)Move suceeded (True) move failed/wall (False)
//(1)New room obstacle
//(2)New room is breezy room 
//(3)New room is stinky room
//(4)New room is pit
//(5)New room is wumpus
//(6)New room is gold
    
    public void updateKnowledgeBase(boolean[] percept) {
        boolean obstacle = percept[1];
        boolean breezy = percept[2];
        boolean stinky = percept[3];
        boolean pit = percept[4];
        boolean wumpus = percept[5];
        boolean glitter = percept[6];

        if (obstacle) {
            KB.setObstacle(agentLocation.i, agentLocation.j);
        } else if (!breezy && !stinky && !pit && !wumpus) { //Determines whether or not space can be marked as safe.
            KB.setSafe(agentLocation.i, agentLocation.j);
        }
    }

}
