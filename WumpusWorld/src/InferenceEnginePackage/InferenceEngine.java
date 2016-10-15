/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InferenceEnginePackage;
import WumpusWorld.*;
import KnowledgeBasePackage.*;
import java.util.ArrayList;

/**
 *
 * @author benrhuman
 */
public class InferenceEngine {
    
    private KnowledgeBase KB;
    private Location agentLocation;
    private int agentDirection;
    
    public InferenceEngine(int size){
        KB = new KnowledgeBase(size);
    }
    
    public void TELL(boolean[] percept, Location location, int direction){  // Allows tha agent to tell the IE about its percepts
      agentLocation = location;
      agentDirection = direction;
      
      updateKnowledgeBase(percept);
    }
    
    public ArrayList ASK(){  //Allows the IE to tell the agent a list of moves to take
        
        
    }
    
    
    private ArrayList PLAN(){
        
    }
    
    
//(0)Move suceeded (True) move failed/wall (False)
//(1)New room obstacle
//(2)New room is breezy room 
//(3)New room is stinky room
//(4)New room is pit
//(5)New room is wumpus
//(6)New room is gold
    
    public void updateKnowledgeBase(boolean[] percept){
        boolean obstacle = percept[1];
        boolean breezy = percept[2];
        boolean stinky = percept[3];
        boolean pit = percept[4];
        boolean wumpus = percept[5];
        boolean glitter = percept[6];
        
        
        if(obstacle){
            KB.setObstacle(agentLocation.i, agentLocation.j);
        }else if(!breezy && !stinky && !pit && !wumpus){ //Determines whether or not space can be marked as safe.
            KB.setSafe(agentLocation.i, agentLocation.j);
        }
    }
    
    
}
