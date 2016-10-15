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
        try{
        KBMap[x][y].unknown = false;
        KBMap[x][y].safe = true;
        KBMap[x][y].kindaSafe = true;
        KBMap[x][y].possibleWumpus = false;
        KBMap[x][y].possiblePit = false;
        KBMap[x][y].knownWumpus = false;
        KBMap[x][y].knownPit = false;
        }catch(IndexOutOfBoundsException e){}
    }
    
    public void setKnownPit(int x, int y){
        try{
        KBMap[x][y].unknown = false;
        KBMap[x][y].knownPit = true;
        KBMap[x][y].possibleWumpus = false;
        KBMap[x][y].possiblePit = false;
        }catch(IndexOutOfBoundsException e){}
    }
    
    public void setPossiblePit(int x, int y){
        try{
        if(!KBMap[x][y].safe && !KBMap[x][y].kindaSafe && !KBMap[x][y].obstacle){    
        KBMap[x][y].unknown = false;
        KBMap[x][y].possiblePit = true;
        }
        }catch(IndexOutOfBoundsException e){}
    }
    
    public void setKnownWumpus(int x, int y){
        try{
        KBMap[x][y].unknown = false;
        KBMap[x][y].knownWumpus = true;
        KBMap[x][y].possibleWumpus = false;
        KBMap[x][y].possiblePit = false;
        }catch(IndexOutOfBoundsException e){}
    }
    
    public void setPossibleWumpus(int x, int y){
        try{
        if(!KBMap[x][y].safe && !KBMap[x][y].kindaSafe && !KBMap[x][y].obstacle){    
        KBMap[x][y].unknown = false;
        KBMap[x][y].possibleWumpus = true;
        }
        }catch(IndexOutOfBoundsException e){}
    }
    
    public void setObstacle(int x, int y){
        try{
        KBMap[x][y].unknown = false;
        KBMap[x][y].obstacle = true;
        KBMap[x][y].possibleWumpus = false;
        KBMap[x][y].possiblePit = false;
        }catch(IndexOutOfBoundsException e){}
    }
    
    public void setKindaSafe(int x, int y){
        try{
            setSafe(x,y);
            KBMap[x][y].kindaSafe = true;
            KBMap[x][y].safe = false;
        }catch(IndexOutOfBoundsException e){}
    }
    
    public boolean checkSafe(int x, int y){
        try{
        return KBMap[x][y].safe;
        }catch(IndexOutOfBoundsException e){
            return false;
        }
    }
    
    public boolean checkUnknown(int x, int y){
        try{
        return KBMap[x][y].unknown; //True if unknown
        }catch(IndexOutOfBoundsException e){
            return false;
        }
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
