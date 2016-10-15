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
    
    KBRoom[][] KBMap;
    
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
    
    public KBRoom getRoomState(int x, int y){
        return KBMap[x][y];
    }
    
}
