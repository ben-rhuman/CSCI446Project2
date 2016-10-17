package KnowledgeBasePackage;

import WumpusWorld.*;
import InferenceEnginePackage.*;
import java.util.ArrayList;

public class KnowledgeBase {

    public KBRoom[][] KBMap;
    public ArrayList<Location> wump;
    public ArrayList<Location> safe;
    public ArrayList<Location> visited;

    public KnowledgeBase(int worldSize) {
        KBMap = new KBRoom[worldSize][worldSize];
        for (int i = 0; i < KBMap.length; i++) {
            for (int j = 0; j < KBMap[i].length; j++) {
                KBMap[i][j] = new KBRoom();  //Initialized the KBMap to the starting state.
            }
        }
    }
    public void updateKnowledgeBase(boolean[] percept, Location location) {

    }
    
    public void setVisited(int x, int y){
        try{
        KBMap[x][y].unknown = false;
        KBMap[x][y].visited = false;
        
        Location l = new Location(x,y);
        visited.add(l);
        
        }catch (IndexOutOfBoundsException e){
        }
        
    }
    
    public void setSafe(int x, int y) {

        try {
            KBMap[x][y].unknown = false;
            KBMap[x][y].safe = true;
            KBMap[x][y].kindaSafe = true;
            KBMap[x][y].possibleWumpus = false;
            KBMap[x][y].possiblePit = false;
            
            Location l = new Location(x,y);
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
            setSafe(x, y); ///????????????????
            KBMap[x][y].kindaSafe = true;
            KBMap[x][y].safe = false;
        } catch (IndexOutOfBoundsException e) {
        }
    }

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

    public KBRoom getRoomState(int x, int y) {
        return KBMap[x][y];
    }

    ///==================Checking Knowlege Base for Inference Engine====================
    
    public ArrayList returnSafeAdjacents(Location l){ //or return visited ones
        ArrayList<Location> adjacents = new ArrayList<Location>();
        
        try {
            
            if (KBMap[l.i + 1][l.j].safe || KBMap[l.i + 1][l.j].visited){
                Location adj = new Location(l.i + 1, l.j);
                adjacents.add(adj);
                
            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            if (KBMap[l.i - 1][l.j].safe || KBMap[l.i - 1][l.j].visited){
                Location adj = new Location(l.i - 1, l.j);
                adjacents.add(adj);
            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            if (KBMap[l.i][l.j + 1].safe || KBMap[l.i][l.j + 1].visited){
                 Location adj = new Location(l.i, l.j + 1);
                adjacents.add(adj);
            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            if (KBMap[l.i][l.j - 1].safe || KBMap[l.i][l.j - 1].visited){
                 Location adj = new Location(l.i, l.j - 1);
                adjacents.add(adj);
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
    
    public int whichDirection (Location ag, int dir, Location spot){
        
        return dir;
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
