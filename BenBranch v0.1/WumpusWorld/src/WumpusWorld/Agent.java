package WumpusWorld;

import WumpusWorld.Location;
import WumpusWorld.Map;
import static java.lang.Math.abs;
import KnowledgeBasePackage.KnowledgeBase;
import InferenceEnginePackage.InferenceEngine;
import java.util.ArrayList;

/*
 * Wumpus World using First Order Logic
 * Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */

public class Agent {

    private int payOff;
    private int direction; //1 = North, 2 = East, 3 = South, 4 = West
    private int moveCounter;
    private Location currentLocation;
    private int arrowCount;
    private Map worldMap;
    private InferenceEngine IE;
    boolean[] percept;
//(0)Move suceeded (True) move failed/wall (False)
//(1)New room obstacle
//(2)New room is breezy room 
//(3)New room is stinky room
//(4)New room is pit
//(5)New room is wumpus
//(6)New room is gold
    
    public Agent(int x, int y, int numGold, int direction, Map worldMap) {  //Direction in our case should always be 2 (i.e. East) at start.
        currentLocation.i = x;
        currentLocation.j = y;
        this.payOff = numGold;
        this.direction = direction;
        this.worldMap = worldMap;
        
        IE = new InferenceEngine(worldMap.size);

        percept = worldMap.checkPerceptAtLocation(currentLocation);
    }
    
////////////////////////// Game Execution Functions ////////////////////////////
    
    public void playGame(){
        boolean done = false;
        ArrayList<Integer> plan;
        while(done != true){
            TELL();
            plan = ASK();
            executePlan(plan);
        }
    }
    
    public boolean executePlan(ArrayList<Integer> plan){
        if(plan.isEmpty()){
            return true;
        }

        switch ((int) plan.get(0)){
            case 1: //(1) turn left 
                turnLeft();
                break;
            case 2: //(2) turn right
                turnRight();
                break;
            case 3: //(3) forward
                move(); //THIS IS NOT ALL THAT IT DOES RIGHT HERE. JUST A PLACE HOLDER FOR NOW
                break;
            case 4: //(4) grab
                grab(); //THIS IS NOT ALL THAT IT DOES RIGHT HERE. JUST A PLACE HOLDER FOR NOW
                break;
            case 5: //(5) shoot
                shootArrow(); //THIS IS NOT ALL THAT IT DOES RIGHT HERE. JUST A PLACE HOLDER FOR NOW
                break;
        }
    }
    
////////////////////////// End Game Execution Functions ////////////////////////////
    
    
//////////////////////// Agent Action Methods //////////////////////////////////
    private boolean[] move() { //Moves the agent one space in the direction its facing.
        moveCounter++;
        System.out.println(moveCounter + ": Moved forward.");
        return worldMap.move(currentLocation, direction);  //Returns a list of 7 percepts
    }

    private void turnLeft() { // Rotates the agent 90 degrees to the left                  
        if (direction == 1) {
            direction = 4;
        } else {
            direction--;
        }
        payOff -= 10;
        moveCounter++;
        System.out.println(moveCounter + ": Turned left.");
    }

    private void turnRight() { // Rotates the agent 90 degrees to the right
        if (direction == 4) {
            direction = 1;
        } else {
            direction++;
        }
        payOff -= 10;
        moveCounter++;
        System.out.println(moveCounter + ": Turned right.");
    }

    private boolean shootArrow() {  //Shoots an arrow in the direction the agent is facing
        moveCounter++;
        if (arrowCount <= 0) {
            System.out.println(moveCounter + ": Could not shoot an arrow.");
            return false;
        }
        arrowCount--;
        System.out.println(moveCounter + ": Shot an arrow.");
        return worldMap.shootArrow(currentLocation, direction);
    }

    private boolean grab(){
        moveCounter++;
        if(percept[6]){
            System.out.println(moveCounter + ": Grab gold.");
            return true;
        }
        System.out.println(moveCounter + ": Grabbed but no gold.");
        return false;
    }
        //////////////////////// End Agent Action Methods //////////////////////////////////
    
    
    
    //////////////////////////// Agent - Inference Engine Methods ////////////////////////////
    
    private void TELL() {
        IE.TELL(percept, currentLocation, direction, arrowCount);   // Tells the IE what the current percepts are
    }
    
    private ArrayList ASK(){
        return IE.ASK();  //Asks the IE what move it should take. 
    }
}
