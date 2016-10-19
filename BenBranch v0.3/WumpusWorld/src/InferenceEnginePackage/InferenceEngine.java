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
    private int arrowCount;

    public InferenceEngine(int size) {
        KB = new KnowledgeBase(size);
    }

    public void TELL(boolean[] percept, Location location, int direction, int arrowCount) {  // Allows tha agent to tell the IE about its percepts
        agentLocation = location;
        agentDirection = direction;
        this.arrowCount = arrowCount;

        updateKnowledgeBase(percept);
        for (int i = 0; i < 5; i++) { // 4 is our arbitrary amount of inference cycles
            LogicalMapDeduction();
        }
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
            if (arrowCount != 0) {
                Location w = new Location(KB.wump.get(0).i, KB.wump.get(0).j);

                if (planToHunt(w, agentDirection, agentLocation)) { //if we can make a plan to hunt the wumpus

                    KB.wump.remove(0); //remove it from the front of the list
                    return;
                } else {
                    System.out.println("this shouldnt happen");
                    plan.clear();
                }
            }
        }
        // ** iterate through the entire list of potential spots?
        KB.createDesiredSpots(1); //spots that are unvisited and kindaSafe
        Location closestDesiredSpot = KB.getClosestDesiredSpot(agentLocation);

        if (planToExplore(closestDesiredSpot, agentDirection, agentLocation)) {
            return;
        } else {
            plan.clear();
            KB.createDesiredSpots(2); //spots that are unvisited
            Location closestDesiredSpot2 = KB.getClosestDesiredSpot(agentLocation);

            if (planToExplore(closestDesiredSpot2, agentDirection, agentLocation)) {
                return;
            } else {
                System.out.println("cant get to unvisited spot/trapped?");
            }
        }

        //potentially shoot if there is no other choice?
        //make sure after it carries out the plan the adgents spot is updated
        //other ways to infer pit or wumpus?
    }

    private boolean planToExplore(Location closest, int currentDir, Location currentLocation) { //find a path to the nearest desiredSpots spot

        ArrayList<Location> adjacents;  //create list of adjacents safe/visited
        ArrayList<Integer> turnPlan;

        //update list of spots we have already been
        tempVisited.add(currentLocation);

        adjacents = KB.returnSafeAdjacents(currentLocation, tempVisited);

        //order that list to have the directions that move closer to the wumpus first
        adjacents = KB.orderDirection(adjacents, closest, currentLocation);

        if ((currentLocation.i == closest.i && currentLocation.j == closest.j)) { //base case if can shoot wumpus

            return true;

        } else if (!adjacents.isEmpty()) { //the list is not empty

            for (int i = 0; i < adjacents.size(); i++) {

                //setup the direction and current location for the next potential move
                currentLocation.setLocation(adjacents.get(i).i, adjacents.get(i).j);

                turnPlan = KB.getTurnPlan(currentDir, adjacents.get(i).dir);

                for (int j = 0; j < turnPlan.size(); j++) { //add these turns to the plan
                    plan.add(turnPlan.get(j));
                }
                plan.add(3); //move forward 

                currentDir = adjacents.get(i).dir; //set up curDir after it was used

                if (planToExplore(closest, currentDir, adjacents.get(i))) {
                    return true;

                } else { //remove the actions if the plan failed
                    for (int j = 0; j <= turnPlan.size(); j++) { //extra iteration for single forward action
                        plan.remove(plan.size() - 1);
                    }
                    tempVisited.remove(plan.size() - 1);
                }

            }

        }

        return false;
    }

    private boolean planToHunt(Location w, int currentDir, Location currentLocation) { //recursively find a path to the wumpus

        ArrayList<Location> adjacents;  //create list of adjacents safe/visited
        ArrayList<Integer> turnPlan;

        //update list of spots we have already been
        tempVisited.add(currentLocation);

        adjacents = KB.returnSafeAdjacents(currentLocation, tempVisited);

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

                turnPlan = KB.getTurnPlan(currentDir, adjacents.get(i).dir);

                for (int j = 0; j < turnPlan.size(); j++) { //add these turns to the plan
                    plan.add(turnPlan.get(j));
                }
                plan.add(3); //move forward 

                currentDir = adjacents.get(i).dir; //set up curDir after it was used

                if (planToHunt(w, currentDir, adjacents.get(i))) {
                    return true;

                } else { //remove the actions if the plan failed
                    for (int j = 0; j <= turnPlan.size(); j++) { //extra iteration for single forward action
                        plan.remove(plan.size() - 1);
                    }
                    tempVisited.remove(plan.size() - 1);
                }

            }

        }

        return false;
    }

//(0)Move suceeded (True) move failed/wall (False)
//(1)New room obstacle
//(2)New room is breezy room 
//(3)New room is stinky room
//(4)New room is pit
//(5)New room is wumpus
//(6)New room is gold
    //(7) Wumpus Scream
    public void updateKnowledgeBase(boolean[] percept) {

        //Used for code readablity
        boolean obstacle = percept[1];
        boolean breezy = percept[2];
        boolean stinky = percept[3];
        boolean pit = percept[4];
        boolean wumpus = percept[5];
        boolean glitter = percept[6];
        boolean scream = percept[7];
        int x = agentLocation.i;
        int y = agentLocation.j;

        KB.KBMap[x][y].unknown = false;

        if (scream) {
            killWumpus(x, y);
        }

        if (!KB.KBMap[x][y].visited || !percept[0]) { //if previosuly visited we dont need to reupdate these, i think...

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
                KB.setObstacle(x + xMod, y + yMod);
            } else if (pit) {
                KB.setKnownPit(x, y);   //Agent is dead, but we still need to update the map
            } else if (wumpus) {
                KB.setKnownWumpus(x, y);   //Agent is dead, still need to update map.
            }

            if (breezy) {
                KB.KBMap[x][y].breeze = true;
                KB.KBMap[x][y].kindaSafe = true;
            } else if (stinky) {
                KB.KBMap[x][y].stench = true;
                KB.KBMap[x][y].kindaSafe = true;
            } else if (!pit && !wumpus && !obstacle) { //Determines whether or not space can be marked as safe.
                KB.setSafe(x, y);
            }

            if (!pit && !wumpus && !obstacle) {
                KB.setKindaSafe(x, y);
            }

            if (glitter) {
                KB.KBMap[x][y].glitter = true;
            }
        }
        KB.KBMap[x][y].visited = true;
    }

    private void killWumpus(int x, int y) {
        if (x >= 0 && x < KB.KBMap.length && y >= 0 && y < KB.KBMap.length) {
            switch (agentDirection) {
                case 1:
                    if (KB.KBMap[x][y].knownWumpus || KB.KBMap[x][y].possibleWumpus) {
                        KB.KBMap[x][y].knownWumpus = false;
                        KB.KBMap[x][y].possibleWumpus = false;
                        KB.setKindaSafe(x, y);
                    } else {
                        y--;
                        killWumpus(x, y);
                    }
                    break;
                case 2:
                    if (KB.KBMap[x][y].knownWumpus || KB.KBMap[x][y].possibleWumpus) {
                        KB.KBMap[x][y].knownWumpus = false;
                        KB.KBMap[x][y].possibleWumpus = false;
                        KB.setKindaSafe(x, y);
                    } else {
                        x++;
                        killWumpus(x, y);
                    }
                    break;
                case 3:
                    if (KB.KBMap[x][y].knownWumpus || KB.KBMap[x][y].possibleWumpus) {
                        KB.KBMap[x][y].knownWumpus = false;
                        KB.KBMap[x][y].possibleWumpus = false;
                        KB.setKindaSafe(x, y);
                    } else {
                        y++;
                        killWumpus(x, y);
                    }
                    break;
                case 4:
                    if (KB.KBMap[x][y].knownWumpus || KB.KBMap[x][y].possibleWumpus) {
                        KB.KBMap[x][y].knownWumpus = false;
                        KB.KBMap[x][y].possibleWumpus = false;
                        KB.setKindaSafe(x, y);
                    } else {
                        x--;
                        killWumpus(x, y);
                    }
                    break;
                default:
                    break;
            }
            return;
        } else {
            System.out.println("Failed to determine where wumpus was in the Knowledge Base.");
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
            if (KB.KBMap[i][j].safe) {  // ForAll Room1, Safe(Room1) => KindaSafe(Room2)
                KB.setKindaSafe(i, j - 1); //Modifies places adjacent to kindaSafe
                KB.setKindaSafe(i, j + 1);
                KB.setKindaSafe(i - 1, j);
                KB.setKindaSafe(i + 1, j);
            } 
            
//            else if (KB.KBMap[i][j].breeze && KB.KBMap[i][j].stench) {   //ForAll Room1, (Stinky(Room1) && Breezy(Room1)) && !KindaSafe(Room2) => PossibleWumpus(Room2) && PossiblePit(Room2)
//                KB.setPossiblePit(i, j - 1); //Marks possible wumpus/pit if nto already marked kindaSafe
//                KB.setPossibleWumpus(i, j - 1);
//                KB.setPossiblePit(i, j + 1);
//                KB.setPossibleWumpus(i, j + 1);
//                KB.setPossiblePit(i - 1, j);
//                KB.setPossibleWumpus(i - 1, j);
//                KB.setPossiblePit(i + 1, j);
//                KB.setPossibleWumpus(i + 1, j);
//            } 
            
            if (KB.KBMap[i][j].breeze) {  // ForAll Room1, Breezy(Room1) && !KindaSafe(Room2) =>  PossiblePit(Room2)
                KB.setPossiblePit(i, j - 1); //Marks possible pit
                KB.setPossiblePit(i, j + 1);
                KB.setPossiblePit(i - 1, j);
                KB.setPossiblePit(i + 1, j);
            }
            
            if (KB.KBMap[i][j].stench) {  // ForAll Room1, Stinky(Room1) && !KindaSafe(Room2) =>  Stinky(Room2)
                KB.setPossibleWumpus(i, j - 1); //Marks possible wumpus
                KB.setPossibleWumpus(i, j + 1);
                KB.setPossibleWumpus(i - 1, j);//if you set the data you won't be able to infer the pit/wumpus till the next round
                KB.setPossibleWumpus(i + 1, j);//the one below should be an if statement?
            }
            
            if (KB.KBMap[i][j].possiblePit) {  //Try to debunk or confirm possible pit
                if (KB.checkSafe(i, j - 1) || KB.checkSafe(i, j + 1) || KB.checkSafe(i - 1, j) || KB.checkSafe(i + 1, j)) { //For All Room1 possiblePit(Room1) && (Safe(RoomNorth) || Safe(RoomEast) || Safe(RoomSouth) || Safe(RoomWest)) => kindSafe(Room1)
                    KB.setKindaSafe(i, j);
                } else if (KB.checkBreeze(i, j - 1) && KB.checkBreeze(i, j + 1) && KB.checkBreeze(i - 1, j) && KB.checkBreeze(i + 1, j)) {  //For All Room1 possiblePit(Room1) && !(Safe(RoomNorth) || Safe(RoomEast) || Safe(RoomSouth) || Safe(RoomWest)) && (Breeze(RoomNorth) && Breeze(RoomEast) && Breeze(RoomSouth) && Breeze(RoomWest)) => Pit(Room1)
                    KB.setKnownPit(i, j);
                }
            }
            
            if (KB.KBMap[i][j].possibleWumpus) {
                if (KB.checkSafe(i, j - 1) || KB.checkSafe(i, j + 1) || KB.checkSafe(i - 1, j) || KB.checkSafe(i + 1, j)) {   //For All Room1 possibleWumpus(Room1) && (Safe(RoomNorth) || Safe(RoomEast) || Safe(RoomSouth) || Safe(RoomWest)) => kindSafe(Room1)
                    KB.setKindaSafe(i, j);
                } else if (KB.checkStench(i, j - 1) && KB.checkStench(i, j + 1) && KB.checkStench(i - 1, j) && KB.checkStench(i + 1, j)) {  //For All Room1 possibleWumpus(Room1) && !(Safe(RoomNorth) || Safe(RoomEast) || Safe(RoomSouth) || Safe(RoomWest)) && (Stench(RoomNorth) && Stench(RoomEast) && Stench(RoomSouth) && Stench(RoomWest)) => Pit(Room1)
                    KB.setKnownWumpus(i, j);
                }
            }

            if (KB.KBMap[i][j].breeze) {
                if (KB.checkKindaSafe(i, j - 1) && KB.checkKindaSafe(i + 1, j) && KB.checkKindaSafe(i, j + 1)) { //NES
                    KB.setKnownPit(i - 1, j);  //For All Room1 Breeze(Room1) && (KindaSafe(RoomNorth) && KindaSafe(RoomEast) && KindaSafe(RoomSouth)) => Pit(RoomWest)
                } else if (KB.checkKindaSafe(i, j - 1) && KB.checkKindaSafe(i + 1, j) && KB.checkKindaSafe(i - 1, j)) { //NEW
                    KB.setKnownPit(i, j + 1);  //For All Room1 Breeze(Room1) && (KindaSafe(RoomNorth) && KindaSafe(RoomEast) && KindaSafe(RoomWest)) => Pit(RoomSouth)
                } else if (KB.checkKindaSafe(i, j - 1) && KB.checkKindaSafe(i, j + 1) && KB.checkKindaSafe(i - 1, j)) { //NSW
                    KB.setKnownPit(i + 1, j);  //For All Room1 Breeze(Room1) && (KindaSafe(RoomNorth) && KindaSafe(RoomWest) && KindaSafe(RoomSouth)) => Pit(RoomEast)
                } else if (KB.checkKindaSafe(i, j + 1) && KB.checkKindaSafe(i + 1, j) && KB.checkKindaSafe(i - 1, j)) { //SEW
                    KB.setKnownPit(i, j - 1);  //For All Room1 Breeze(Room1) && (KindaSafe(RoomWest) && KindaSafe(RoomEast) && KindaSafe(RoomSouth)) => Pit(RoomNorth)
                }
            }

            if (KB.KBMap[i][j].stench) {
                if (KB.checkKindaSafe(i, j - 1) && KB.checkKindaSafe(i + 1, j) && KB.checkKindaSafe(i, j + 1)) { //NES
                    KB.setKnownWumpus(i - 1, j);  //For All Room1 Stench(Room1) && (KindaSafe(RoomNorth) && KindaSafe(RoomEast) && KindaSafe(RoomSouth)) => Wumpus(RoomWest)
                } else if (KB.checkKindaSafe(i, j - 1) && KB.checkKindaSafe(i + 1, j) && KB.checkKindaSafe(i - 1, j)) { //NEW
                    KB.setKnownWumpus(i, j + 1);  //For All Room1 Stench(Room1) && (KindaSafe(RoomNorth) && KindaSafe(RoomEast) && KindaSafe(RoomWest)) => Wumpus(RoomSouth)
                } else if (KB.checkKindaSafe(i, j - 1) && KB.checkKindaSafe(i, j + 1) && KB.checkKindaSafe(i - 1, j)) { //NSW
                    KB.setKnownWumpus(i + 1, j);  //For All Room1 Stench(Room1) && (KindaSafe(RoomNorth) && KindaSafe(RoomWest) && KindaSafe(RoomSouth)) => Wumpus(RoomEast)
                } else if (KB.checkKindaSafe(i, j + 1) && KB.checkKindaSafe(i + 1, j) && KB.checkKindaSafe(i - 1, j)) { //SEW
                    KB.setKnownWumpus(i, j - 1);  //For All Room1 Stench(Room1) && (KindaSafe(RoomWest) && KindaSafe(RoomEast) && KindaSafe(RoomSouth)) => Wumpus(RoomNorth)
                }
            }

        }
    }
}
