package KnowledgeBasePackage;

import WumpusWorld.*;
import InferenceEnginePackage.*;
import java.util.ArrayList;

public class KnowledgeBase {

    public KBRoom[][] KBMap;
    public ArrayList<Location> wump = new ArrayList<>();
    public ArrayList<Location> safe = new ArrayList<>();
    public ArrayList<Location> desiredSpots = new ArrayList<>();

    public KnowledgeBase(int worldSize) {
        KBMap = new KBRoom[worldSize][worldSize];
        for (int i = 0; i < KBMap.length; i++) {
            for (int j = 0; j < KBMap[i].length; j++) {
                KBMap[i][j] = new KBRoom();  //Initialized the KBMap to the starting state.
            }
        }

    }

    public void createDesiredSpots(int type, Location agent) {

        desiredSpots.clear();

        for (int i = 0; i < KBMap.length; i++) {
            for (int j = 0; j < KBMap[i].length; j++) {
                Location l = new Location(i, j);
                if (!KBMap[i][j].unknown) {
                    if (type == 1) {
                        if (!sameSpotAsCurrent(agent, i, j) && !KBMap[i][j].visited && KBMap[i][j].kindaSafe && !KBMap[i][j].obstacle) {
                            desiredSpots.add(l);
                        }
                    } else if (type == 2) {
                        if (!sameSpotAsCurrent(agent, i, j) && !KBMap[i][j].visited) {
                            desiredSpots.add(l);
                        }
                    }
                }
            }
        }
    }

    public boolean sameSpotAsCurrent(Location agent, int i, int j){
        if(agent.i == i && agent.j == j){
            return true;
        }
        return false;
    }
    public int desiredSpotsIndexOf(Location l) {
        int index = 0;
        for (int i = 0; i < desiredSpots.size(); i++) {
            if (desiredSpots.get(i).i == l.i && desiredSpots.get(i).j == l.j) {
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
            //int index = desiredSpotsIndexOf(l);
            //desiredSpots.remove(index);

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
                KBMap[x][y].unknown = false;
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
                KBMap[x][y].unknown = false;
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
    public Location getClosestDesiredSpot(Location current) {
        if(!desiredSpots.isEmpty()){
        Location currentClosest = desiredSpots.get(0);
        double currentDistance = Double.MAX_VALUE;

        for (int i = 0; i < desiredSpots.size(); i++) {

            double x1 = (double) current.i;
            double y1 = (double) current.j;

            double x2 = (double) desiredSpots.get(i).i;
            double y2 = (double) desiredSpots.get(i).j;

            double distance2 = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

            if (distance2 <= currentDistance) {
                currentDistance = distance2;
                currentClosest = desiredSpots.get(i);
            }

        }
        
        return currentClosest;
        }
        return null;
    }

    public ArrayList orderDirection(ArrayList<Location> l, Location w, Location agent) {

        ArrayList<Location> ordered = new ArrayList<>();
        ArrayList<Location> temp = new ArrayList<>(); //to hold the "worse" adjacents to go to

        for (int i = 0; i < l.size(); i++) { //if it is increasing add it to the new list and remove it from the old 

            switch (l.get(i).dir) {
                case 1: //north
                    if ((w.j - (agent.j - 1)) > (w.j - agent.j)) {  //if the wumpus is north of the explorer then north is a progressive direction towards the wumpus
                        ordered.add(l.get(i));
                    } else {
                        temp.add(l.get(i));
                    }
                    break;
                case 2: //east
                    if ((w.i - (agent.i + 1)) < (w.i - agent.i)) {
                        ordered.add(l.get(i));
                    } else {
                        temp.add(l.get(i));
                    }
                    break;
                case 3: //south
                    if ((w.j - (agent.j + 1)) < (w.j - agent.j)) {
                        ordered.add(l.get(i));
                    } else {
                        temp.add(l.get(i));
                    }
                    break;
                default: //west
                    if ((w.i - (agent.i - 1)) > (w.i - agent.i)) {
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
            if (!KBMap[l.i + 1][l.j].knownWumpus && !KBMap[l.i + 1][l.j].knownPit) {
                if (KBMap[l.i + 1][l.j].safe ||  KBMap[l.i + 1][l.j].kindaSafe) {
                    if(KBMap[l.i + 1][l.j].visited){
                    Location adj = new Location(l.i + 1, l.j);
                    if (!listContainsLocation(tempVisited, adj)) {
                        adj.dir = 2;
                        adjacents.add(adj);
                    }
                }
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            if (!KBMap[l.i - 1][l.j].knownWumpus && !KBMap[l.i - 1][l.j].knownPit) {
                if (KBMap[l.i - 1][l.j].safe ||  KBMap[l.i + 1][l.j].kindaSafe) {
                    if(KBMap[l.i - 1][l.j].visited){
                    Location adj = new Location(l.i - 1, l.j);
                    if (!listContainsLocation(tempVisited, adj)) {
                        adj.dir = 4;
                        adjacents.add(adj);
                    }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            if (!KBMap[l.i][l.j + 1].knownWumpus && !KBMap[l.i][l.j + 1].knownPit) {
                if (KBMap[l.i][l.j + 1].safe ||  KBMap[l.i + 1][l.j].kindaSafe) {
                    if(KBMap[l.i][l.j + 1].visited){
                    Location adj = new Location(l.i, l.j + 1);
                    if (!listContainsLocation(tempVisited, adj)) {
                        adj.dir = 3;
                        adjacents.add(adj);
                    }
                }
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            if (!KBMap[l.i][l.j - 1].knownWumpus && !KBMap[l.i][l.j - 1].knownPit) {
                if (KBMap[l.i][l.j - 1].safe ||  KBMap[l.i + 1][l.j].kindaSafe) {
                   if (KBMap[l.i][l.j - 1].visited){
                    Location adj = new Location(l.i, l.j - 1);
                    if (!listContainsLocation(tempVisited, adj)) {
                        adj.dir = 1;
                        adjacents.add(adj);
                    }
                   }
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }

        return adjacents;
    }

    public boolean rightDirection(Location ag, int dir, Location spot) { //check if the agent is pointing towards the spot
        
        if (ag.i < spot.i && ag.j == spot.j) { //if agent is left of spot
            if (dir == 2) {
                return true;
            }
        } else if (ag.i > spot.i && ag.j == spot.j) { //if agent is to the right of spot
            if (dir == 4) {
                return true;
            }
        } else if (ag.j < spot.j && ag.i == spot.i) { //if agent is below spot
            if (dir == 3) {
                return true;
            }
        } else if (ag.j > spot.j && ag.i == spot.i) { // if agent is above spot
            if (dir == 1) {
                return true;
            }
        }
        return false;
    }

    public ArrayList getTurnPlan(int dir1, int dir2) { //returns a plan to go from one direction to another
        ArrayList<Integer> turnPlan = new ArrayList<>();
        if(dir1 == dir2){
            return turnPlan;
        }else if ((dir1 % 2 == 0) && (dir2 % 2 == 0)) { //if both even
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
    
    public boolean rightNextToSpot(Location current, Location spot){
        if(current.i == spot.i && current.j == spot.j){
            return true;
        }
        try{
            if((current.i - 1) == spot.i && current.j == spot.j){
                return true;
            }
        }catch (IndexOutOfBoundsException e) {
        }
        
        try{
            if((current.i + 1) == spot.i && current.j == spot.j){
                return true;
            }
        }catch (IndexOutOfBoundsException e) {
        }
        
        try{
            if(current.i == spot.i && (current.j - 1) == spot.j){
                return true;
            }
        }catch (IndexOutOfBoundsException e) {
        }
        
        try{
            if(current.i == spot.i && (current.j + 1) == spot.j){
                return true;
            }
        }catch (IndexOutOfBoundsException e) {
        }
        
        return false;
    }
}
