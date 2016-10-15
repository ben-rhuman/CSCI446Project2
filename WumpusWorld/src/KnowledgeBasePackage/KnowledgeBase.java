/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KnowledgeBasePackage;
import WumpusWorld.*;
import InferenceEnginePackage.*;


/**
 *
 * @author benrhuman
 */
        

public class KnowledgeBase {
    
    public KBRoom[][] KBMap;
    
    public KnowledgeBase(int worldSize){
        KBMap = new KBRoom[worldSize][worldSize];
        for(int i = 0; i < KBMap.length; i++){
            for(int j = 0; j < KBMap[i].length; j++){
                KBMap[i][j] = new KBRoom();  //Initialized the KBMap to the starting state.
            }
        }
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
    
    public KBRoom getRoomState(int x, int y){
        return KBMap[x][y];
    }
    
}
