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
    
    private Location agentLocation;
    private int agentDirection;
    
    public void TELL(boolean[] percept, Location location, int direction){  // Allows tha agent to tell the IE about its percepts
      agentLocation = location;
      agentDirection = direction;
      
      updateKnowledgeBase(percept);
    }
    
    public ArrayList ASK(){  //Allows the IE to tell the agent a list of moves to take
        
        
    }
    
    
    private ArrayList PLAN(){
        
    }
    
    public void updateKnowledgeBase(boolean[] percept){
        
    }
    
    
}
