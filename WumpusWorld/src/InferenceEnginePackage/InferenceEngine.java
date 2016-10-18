package InferenceEnginePackage;

import WumpusWorld.*;
import KnowledgeBasePackage.*;
import java.util.ArrayList;

public class InferenceEngine {

    private KnowledgeBase KB;
    private Location agentLocation;
    private int agentDirection;
    private ArrayList<Integer> plan = new ArrayList<>();
    ArrayList<Location> tempVisited = new ArrayList<>();

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

            if (planToHunt(w, agentDirection, agentLocation)) { //if we can make a plan to hunt the wumpus

                KB.wump.remove(0); //remove it from the front of the list
                return;
            } else {
                KB.wump.add(KB.wump.get(0)); //move that wumpus to the back of the list
                KB.wump.remove(0);           //** probably dont need all this
                plan.clear();
            }
        }
        
        

        //potentially shoot if there is no other choice?
        //otherwise we plan to move to an unvisited spot
        
        //know when to update a spot to visited
        //make sure after it carries out the plan the adgents spot is updated
        
        //if there is a smell/breeze we can check if there is only one possible choice left for the true pit/wumpus
        //other ways to infer pit or wumpus?        
        //set map breeze to false???/?
        //if theres a pit/wumpus move the agent back to its previous spot
        //check if we move into a wall
        //if we move into a wall?/obstacle, make sure don't move
        return;
    }

    private boolean planToHunt(Location w, int currentDir, Location currentLocation) { //recursively find a path to the wumpus

        ArrayList<Location> adjacents;  //create list of adjacents safe/visited
        ArrayList<Integer> turnPlan;

        //update list of spots we have already been
        tempVisited.add(currentLocation);
        
        adjacents = KB.returnSafeAdjacents(currentLocation,tempVisited);

        //order that list to have the directions that move closer to the wumpus first
        adjacents = KB.orderDirection(adjacents, w, currentLocation);
        
        if ((currentLocation.i == w.i || currentLocation.j == w.j) && !KB.obstacleInWay(currentLocation, w)) { //base case if can shoot wumpus

            while (!KB.rightDirection(currentLocation, currentDir, w)) { //fix the direction so the agent can point towards the wumpus
                currentDir++;
                plan.add(2);
            }
            plan.add(5);
            return true;
            
        } else if (!adjacents.isEmpty()) { //the list is not empty

            for (int i = 0; i < adjacents.size(); i++) {
                
                //setup the direction and current location for the next potential move
                currentLocation.setLocation(adjacents.get(i).i, adjacents.get(i).j);
                
                
                turnPlan = KB.getTurnPlan(currentDir,adjacents.get(i).dir);
                
                for(int j = 0; j < turnPlan.size(); j++){ //add these turns to the plan
                    plan.add(turnPlan.get(j));
                }
                plan.add(3); //move forward 
                
                currentDir = adjacents.get(i).dir; //set up curDir after it was used
                //** keep track of profit
                
                if (planToHunt(w,currentDir,adjacents.get(i))) {
                    return true;
                    
                } else{ //remove the actions if the plan failed
                    for(int j = 0; j <= turnPlan.size(); j++){ //extra iteration for single forward action
                    plan.remove(plan.size()-1);
                    }
                    tempVisited.remove(plan.size()-1);
                  //** put back profit
                }
                
            }
            
        }
        
        return false;
    }
   

    private ArrayList planToExplore(int x, int y) {
        ArrayList<Integer> plan = new ArrayList<>();
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

        KB.KBMap[agentLocation.i][agentLocation.j].unknown = false;

        //Used for code readablity
        boolean obstacle = percept[1];
        boolean breezy = percept[2];
        boolean stinky = percept[3];
        boolean pit = percept[4];
        boolean wumpus = percept[5];
        boolean glitter = percept[6];

        //the agent cannot be in the same spot as the obstacle
        //are we going to call updateKnowledgeBase when we walk towards an obstacle
        if (obstacle) {   //If the agent hits an obstacle we need to correct for the fact that that the agentLocation is different from the location of the space we our updating
            int xMod = 0;
            int yMod = 0;
            if (agentDirection == 1) {
                yMod = -1;
            } else if (agentDirection == 2) {
                xMod = 1;
            } else if (agentDirection == 3) {
                yMod = 1;
            } else {
                xMod = -1;
            }
            KB.KBMap[agentLocation.i + xMod][agentLocation.j + yMod].breeze = false;
            KB.setObstacle(agentLocation.i + xMod, agentLocation.j + yMod);
        } else if (pit) {
            KB.setKnownPit(agentLocation.i, agentLocation.j);   //Agent is dead, but we still need to update the map
        } else if (wumpus) {
            KB.setKnownWumpus(agentLocation.i, agentLocation.j);   //Agent is dead, still need to update map.
        } else if (breezy && stinky) {
            KB.KBMap[agentLocation.i][agentLocation.j].breeze = true;
            KB.KBMap[agentLocation.i][agentLocation.j].stench = true;
            KB.KBMap[agentLocation.i][agentLocation.j].kindaSafe = true;
        } else if (breezy) {
            KB.KBMap[agentLocation.i][agentLocation.j].breeze = true;
            KB.KBMap[agentLocation.i][agentLocation.j].kindaSafe = true;
        } else if (stinky) {
            KB.KBMap[agentLocation.i][agentLocation.j].stench = true;
            KB.KBMap[agentLocation.i][agentLocation.j].kindaSafe = true;
        } else { //Determines whether or not space can be marked as safe.
            KB.setSafe(agentLocation.i, agentLocation.j);
        }

        if (glitter) {
            KB.KBMap[agentLocation.i][agentLocation.j].glitter = true;
        }
    }

    private void LogicalMapDeduction() {
        for (int i = 0; i < KB.KBMap.length; i++) {
            for (int j = 0; j < KB.KBMap[i].length; j++) {
                inferNewFacts(i, j);
            }
        }
    }

    private void inferNewFacts(int i, int j) {
        if (KB.KBMap[i][j].unknown == false) {
            if (KB.KBMap[i][j].safe) {  //Modifies places adjacent to kindaSafe
                KB.setKindaSafe(i, j - 1);
                KB.setKindaSafe(i, j + 1);
                KB.setKindaSafe(i - 1, j);
                KB.setKindaSafe(i + 1, j);
            } else if (KB.KBMap[i][j].breeze && KB.KBMap[i][j].stench) {  //Marks possible wumpus/pit if nto already marked kindaSafe
                KB.setPossiblePit(i, j - 1);
                KB.setPossibleWumpus(i, j - 1);
                KB.setPossiblePit(i, j + 1);
                KB.setPossibleWumpus(i, j + 1);
                KB.setPossiblePit(i - 1, j);
                KB.setPossibleWumpus(i - 1, j);
                KB.setPossiblePit(i + 1, j);
                KB.setPossibleWumpus(i + 1, j);
            } else if (KB.KBMap[i][j].breeze) { //Marks possible pit
                KB.setPossiblePit(i, j - 1);
                KB.setPossiblePit(i, j + 1);
                KB.setPossiblePit(i - 1, j);
                KB.setPossiblePit(i + 1, j);
            } else if (KB.KBMap[i][j].stench) { //Marks possible wumpus
                KB.setPossibleWumpus(i, j - 1);
                KB.setPossibleWumpus(i, j + 1);
                KB.setPossibleWumpus(i - 1, j);
                KB.setPossibleWumpus(i + 1, j);
            } else if (KB.KBMap[i][j].possiblePit) { //Try to debunk or confirm possible pit
                if (KB.checkSafe(i, j - 1) || KB.checkSafe(i, j + 1) || KB.checkSafe(i - 1, j) || KB.checkSafe(i + 1, j)) {
                    KB.setKindaSafe(i, j);
                } else if (!KB.checkUnknown(i, j - 1) || !KB.checkUnknown(i, j + 1) || !KB.checkUnknown(i - 1, j) || !KB.checkUnknown(i + 1, j)) {
                    KB.setKnownPit(i, j);
                    //make sure to set all other booleans to false?

                }
            } else if (KB.KBMap[i][j].possibleWumpus) {
                if (KB.checkSafe(i, j - 1) || KB.checkSafe(i, j + 1) || KB.checkSafe(i - 1, j) || KB.checkSafe(i + 1, j)) {
                    KB.setKindaSafe(i, j);
                } else if (!KB.checkUnknown(i, j - 1) || !KB.checkUnknown(i, j + 1) || !KB.checkUnknown(i - 1, j) || !KB.checkUnknown(i + 1, j)) {
                    KB.setKnownWumpus(i, j);
                }
            }
        }
    }

}
