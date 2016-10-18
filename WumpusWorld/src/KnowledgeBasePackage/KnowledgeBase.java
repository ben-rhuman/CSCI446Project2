package KnowledgeBasePackage;

import WumpusWorld.*;
import InferenceEnginePackage.*;
import java.util.ArrayList;

public class KnowledgeBase {

    public KBRoom[][] KBMap;
    public ArrayList<Location> wump = new ArrayList<>();
    public ArrayList<Location> safe = new ArrayList<>();
    public ArrayList<Location> unvisited = new ArrayList<>();

    public KnowledgeBase(int worldSize) {
        KBMap = new KBRoom[worldSize][worldSize];
        for (int i = 0; i < KBMap.length; i++) {
            for (int j = 0; j < KBMap[i].length; j++) {
                KBMap[i][j] = new KBRoom();  //Initialized the KBMap to the starting state.
            }
        }
        
    }

    public void createUnvisited(){
        for (int i = 0; i < KBMap.length; i++) {
            for (int j = 0; j < KBMap[i].length; j++) {
                Location l = new Location(i,j);
                if(!KBMap[i][j].unknown){
                    if(!KBMap[i][j].visited && KBMap[i][j].kindaSafe){
                        unvisited.add(l);
                    }
                }
            }
        }
    }
    public int unvisitedIndexOf(Location l){
        int index = 0;
        for(int i = 0; i < unvisited.size(); i++){
            if (unvisited.get(i).i == l.i && unvisited.get(i).j == l.j){
                index = i;
                return index;
            }
        }
        return index;
    }
    
    public void setVisited(int x, int y) {
        try {
            KBMap[x][y].unknown = false;
            KBMap[x][y].visited = false;

            Location l = new Location(x, y);
            int index = unvisitedIndexOf(l);
            unvisited.remove(index);
            

        } catch (IndexOutOfBoundsException e) {
        }

    }

    public void setSafe(int x, int y) {

        try {
            KBMap[x][y].unknown = false;
            KBMap[x][y].safe = true;
            KBMap[x][y].kindaSafe = true;
            KBMap[x][y].possibleWumpus = false;
            KBMap[x][y].possiblePit = false;

            Location l = new Location(x, y);
            safe.add(l);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void setKnownPit(int x, int y) {
        try {
            KBMap[x][y].unknown = false;
            KBMap[x][y].knownPit = true;
            KBMap[x][y].possibleWumpus = false;
            KBMap[x][y].possiblePit = false;
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void setPossiblePit(int x, int y) {
        try {
            if (!KBMap[x][y].safe && !KBMap[x][y].kindaSafe && !KBMap[x][y].obstacle) {
                KBMap[x][y].kindaSafe = false;
                KBMap[x][y].unknown = false;
                KBMap[x][y].possiblePit = true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void setKnownWumpus(int x, int y) {
        try {
            KBMap[x][y].unknown = false;
            KBMap[x][y].knownWumpus = true;
            KBMap[x][y].possibleWumpus = false;
            KBMap[x][y].possiblePit = false;

            Location l = new Location(x, y);
            wump.add(l);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void setPossibleWumpus(int x, int y) {
        try {
            if (!KBMap[x][y].safe && !KBMap[x][y].kindaSafe && !KBMap[x][y].obstacle) {
                KBMap[x][y].kindaSafe = false;
                KBMap[x][y].unknown = false;
                KBMap[x][y].possibleWumpus = true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void setObstacle(int x, int y) {
        try {
            KBMap[x][y].unknown = false;
            KBMap[x][y].obstacle = true;
            KBMap[x][y].possibleWumpus = false;
            KBMap[x][y].possiblePit = false;
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void setKindaSafe(int x, int y) {
        try {
            KBMap[x][y].unknown = false;
            KBMap[x][y].kindaSafe = true;
            KBMap[x][y].possibleWumpus = false;
            KBMap[x][y].possiblePit = false;
        } catch (IndexOutOfBoundsException e) {
        }
    }
    
    /////////////////////////////// End Setting Methods /////////////////////////////////
    
    /////////////////////////////// Checking Methods /////////////////////////////////

    public boolean checkSafe(int x, int y) {
        try {
            return KBMap[x][y].safe;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public boolean checkUnknown(int x, int y) {
        try {
            return KBMap[x][y].unknown; //True if unknown
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    
    public KBRoom getRoomState(int x, int y) {
        return KBMap[x][y];
    }
/////////////////////////////// Checking Methods /////////////////////////////////
    public void removeWumpusStench(int x, int y) {

        try {
            KBMap[x + 1][y].stench = false;
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            KBMap[x - 1][y].stench = false;
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            KBMap[x][y + 1].stench = false;
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            KBMap[x][y - 1].stench = false;
        } catch (IndexOutOfBoundsException e) {

        }

    }

    

    public boolean listContainsLocation(ArrayList<Location> tempVisited, Location l) {
        for (int i = 0; i < tempVisited.size(); i++) {
            if (tempVisited.get(i).i == l.i && tempVisited.get(i).j == l.j) {
                return true;
            }
        }
        return false;
    }

    ///==================Checking Knowlege Base from Inference Engine====================
    public Location getClosestUnvisited(Location current){
        Location currentClosest = unvisited.get(0);
        double currentDistance = Double.MAX_VALUE;
        
        for(int i = 0; i < unvisited.size(); i++){
            
            double x1 = (double)current.i;
            double y1 = (double)current.j;
            
            double x2 = (double)unvisited.get(i).i;
            double y2 = (double)unvisited.get(i).j;
            
            double distance2 = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
            
            if(distance2 <= currentDistance){
                currentDistance = distance2;
                currentClosest = unvisited.get(i);
            }
            
        }
        return currentClosest;
    }
    
    public ArrayList orderDirection(ArrayList<Location> l, Location w, Location agent) {

        ArrayList<Location> ordered = new ArrayList<>();
        ArrayList<Location> temp = new ArrayList<>(); //to hold the "worse" adjacents to go to

        for (int i = 0; i < l.size(); i++) { //if it is increasing add it to the new list and remove it from the old 

            switch (l.get(i).dir) {
                case 1: //north
                    if ((w.j - (--agent.j)) > (w.j - agent.j)) {  //if the wumpus is north of the explorer then north is a progressive direction towards the wumpus
                        ordered.add(l.get(i));
                    } else {
                        temp.add(l.get(i));
                    }
                    break;
                case 2: //east
                    if ((w.i - (++agent.i)) < (w.i - agent.i)) {
                        ordered.add(l.get(i));
                    } else {
                        temp.add(l.get(i));
                    }
                    break;
                case 3: //south
                    if ((w.j - (++agent.j)) < (w.j - agent.j)) {
                        ordered.add(l.get(i));
                    } else {
                        temp.add(l.get(i));
                    }
                    break;
                default: //west
                    if ((w.i - (--agent.i)) > (w.i - agent.i)) {
                        ordered.add(l.get(i));
                    } else {
                        temp.add(l.get(i));
                    }
                    break;
            }
        }

        //add remaining non progressive adjacent spots to the list because we may still need them
        for (int i = 0; i < temp.size(); i++) {
            ordered.add(l.get(i));
        }

        return ordered;
    }

    public ArrayList returnSafeAdjacents(Location l, ArrayList<Location> tempVisited) { //or return visited ones, and not ones previously used for the plan
        ArrayList<Location> adjacents = new ArrayList<>();

        try {

            if (KBMap[l.i + 1][l.j].safe || KBMap[l.i + 1][l.j].visited) {

                Location adj = new Location(l.i + 1, l.j);
                if (!listContainsLocation(tempVisited, adj)) {
                    adj.dir = 2;
                    adjacents.add(adj);
                }

            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            if (KBMap[l.i - 1][l.j].safe || KBMap[l.i - 1][l.j].visited ) {
                Location adj = new Location(l.i - 1, l.j);
                if (!listContainsLocation(tempVisited, adj)) {
                    adj.dir = 4;
                    adjacents.add(adj);
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            if (KBMap[l.i][l.j + 1].safe || KBMap[l.i][l.j + 1].visited ) {
                Location adj = new Location(l.i, l.j + 1);
                if (!listContainsLocation(tempVisited, adj)) {
                    adj.dir = 3;
                    adjacents.add(adj);
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            if (KBMap[l.i][l.j - 1].safe || KBMap[l.i][l.j - 1].visited ) {
                Location adj = new Location(l.i, l.j - 1);
                if (!listContainsLocation(tempVisited, adj)) {
                    adj.dir = 1;
                    adjacents.add(adj);
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }

        return adjacents;
    }

    public boolean rightDirection(Location ag, int dir, Location w) { //check if the agent is pointing towards the wumpus 
        if (ag.i < w.i && ag.j == w.j) { //if agent is left of wumpus
            if (dir == 2) {
                return true;
            }
        } else if (ag.i > w.i && ag.j == w.j) { //if agent is to the right of wumpus
            if (dir == 4) {
                return true;
            }
        } else if (ag.j < w.j && ag.i == w.i) { //if agent is below wumpus
            if (dir == 1) {
                return true;
            }
        } else if (ag.j > w.j && ag.i == w.i) { // if agent is above wumpus
            if (dir == 3) {
                return true;
            }
        }
        return false;
    }

    public ArrayList getTurnPlan(int dir1, int dir2) { //returns a plan to go from one direction to another
        ArrayList<Integer> turnPlan = new ArrayList<>();
        if ((dir1 % 2 == 0) && (dir2 % 2 == 0)) { //if both even
            turnPlan.add(2);
            turnPlan.add(2);
        } else if ((dir1 % 2 != 0) && (dir2 % 2 != 0)) { //if both odd
            turnPlan.add(2);
            turnPlan.add(2);
        } else if (dir1 == 1) { //special case for 1
            if (dir2 == 4) {
                turnPlan.add(1);
            } else {
                turnPlan.add(2);
            }
        } else if (dir1 == 4) { //special case for 4
            if (dir2 == 1) {
                turnPlan.add(2);
            } else {
                turnPlan.add(1);
            }

        } else if (dir1 > dir2) {
            turnPlan.add(1);
        } else if (dir2 > dir1) {
            turnPlan.add(2);
        }

        return turnPlan;
    }

    public boolean obstacleInWay(Location ag, Location w) { //checks if theres an obstacle or unknown (potential obstacle) inbetween agent and wumpus

        if (ag.i < w.i && ag.j == w.j) { //if agent is left of wumpus
            for (int k = ag.i; k < w.i; k++) {//check spots
                if (getRoomState(k, ag.j).unknown || getRoomState(k, ag.j).obstacle) {
                    return true;
                }
            }
        } else if (ag.i > w.i && ag.j == w.j) { //if agent is to the right of wumpus
            for (int k = ag.i; k > w.i; k--) {
                if (getRoomState(k, ag.j).unknown || getRoomState(k, ag.j).obstacle) {
                    return true;
                }
            }
        } else if (ag.j < w.j && ag.i == w.i) { //if agent is below wumpus
            for (int k = ag.j; k < w.j; k++) {
                if (getRoomState(ag.i, k).unknown || getRoomState(ag.i, k).obstacle) {
                    return true;
                }
            }
        } else if (ag.j > w.j && ag.i == w.i) { // if agent is above wumpus
            for (int k = ag.j; k > w.j; k--) {
                if (getRoomState(ag.i, k).unknown || getRoomState(ag.i, k).obstacle) {
                    return true;
                }
            }
        }

        return false;
    }
}
