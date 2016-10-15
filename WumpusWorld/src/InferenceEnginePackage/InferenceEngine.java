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
        
        //Used for code readablity
        boolean obstacle = percept[1];
        boolean breezy = percept[2];
        boolean stinky = percept[3];
        boolean pit = percept[4];
        boolean wumpus = percept[5];
        boolean glitter = percept[6];
        
        
        if(obstacle){   //If the agent hits an obstacle we need to correct for the fact that that the agentLocation is deferent from the location of the space we our updating
            KB.setObstacle(agentLocation.i, agentLocation.j);
        }else if(pit){
            KB.setKnownPit(agentLocation.i, agentLocation.j);   //Agent is dead, but we still need to update the map
        }else if(wumpus){
            KB.setKnownWumpus(agentLocation.i, agentLocation.j);   //Agent is dead, still need to update map.
        }else if(breezy && stinky){
            KB.KBMap[agentLocation.i][agentLocation.j].breeze = true;
            KB.KBMap[agentLocation.i][agentLocation.j].stench = true;
        } else if(breezy){
            KB.KBMap[agentLocation.i][agentLocation.j].breeze = true;
        }else if(stinky){
            KB.KBMap[agentLocation.i][agentLocation.j].stench = true;
        }else{ //Determines whether or not space can be marked as safe.
            KB.setSafe(agentLocation.i, agentLocation.j);
        }
        
        if(glitter){
            KB.KBMap[agentLocation.i][agentLocation.j].glitter = true;
        }
    }
    
    
}
