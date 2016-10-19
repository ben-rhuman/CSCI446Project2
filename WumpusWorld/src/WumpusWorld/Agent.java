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
    private boolean done;
    private boolean[] percept;
//(0)Move suceeded (True) move failed/wall (False)
//(1)New room obstacle
//(2)New room is breezy room 
//(3)New room is stinky room
//(4)New room is pit
//(5)New room is wumpus
//(6)New room is gold

    public Agent(int x, int y, int numGold, int direction, Map worldMap) {  //Direction in our case should always be 2 (i.e. East) at start.
       currentLocation = new Location(x,y);
        this.payOff = numGold;
        this.direction = direction;
        this.worldMap = worldMap;

        IE = new InferenceEngine(worldMap.size);

        percept = worldMap.checkPerceptAtLocation(currentLocation);
        
        playGame();
    }

////////////////////////// Game Execution Functions ////////////////////////////
    private void playGame() {
        done = false;
        ArrayList<Integer> plan;
        while (done != true) {
            TELL();
            plan = ASK();
            executePlan(plan);
        }
    }

    private boolean executePlan(ArrayList<Integer> plan) {
        if (plan.isEmpty()) {
            return true;
        }

        switch ((int) plan.get(0)) {
            case 0: //if trapped
                System.out.println("trapped son");
                done = true;
                break;
            case 1: //(1) turn left 
                turnLeft();
                break;
            case 2: //(2) turn right
                turnRight();
                break;
            case 3: //(3) forward
                percept = move();
                if (!percept[0] || percept[1]) { //If move failed (0) or obstacle (1) 
                    if(percept[4]){ //
                        System.out.println("Fell in a pit.");
                        payOff -= 100;
                    }
                    if(percept[5]){
                        System.out.println("Eaten by a wumpus.");
                        payOff -= 100;
                    }
                    if (percept[1]){
                        System.out.println("Hit an obstacle.");
                    }
                    //Leave location alone
                } else {
                    updateLocation();
                }
                break;
            case 4: //(4) grab
                if (grab()) {
                    win();
                }
                break;
            case 5: //(5) shoot
                percept[7] = shootArrow();  //Updates the scream percept if a wumpus is killed
                break;
        }

        plan.remove(0);
        return executePlan(plan);
    }

    private void updateLocation() {
        if (direction == 1) {
            currentLocation.j--;
        } else if (direction == 2) {
            currentLocation.i++;
        } else if (direction == 3) {
            currentLocation.j++;
        } else {
            currentLocation.i--;
        }
    }

    private void win() {
        System.out.println("The Agent Found The Gold And Won!");
        printStats();
        done = true;
    }

    private void printStats() {

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
        payOff -= 10; //shooting an arrow
        System.out.println(moveCounter + ": Shot an arrow.");
        if (worldMap.shootArrow(currentLocation, direction)) {
            payOff += 10; //killing a wumpus
            return true;
        } else {
            return false;
        }
    }

    private boolean grab() {
        moveCounter++;
        if (percept[6]) {
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
        printPercepts(percept, currentLocation);
        
    
    }

    private ArrayList ASK() {
        return IE.ASK();  //Asks the IE what move it should take. 
    }
    
    public void printPercepts(boolean[] percept,Location currentLocation){
        for(int i = 0; i < percept.length; i++){
            switch(i){
                case 0: System.out.println("Move suceeded/failed: " + percept[i]);
                    break;
                case 1: System.out.println("New room obstacle: " + percept[i]);
                    break;
                case 2: System.out.println("New room is breezy: " + percept[i]);
                    break;
                case 3: System.out.println("New room is stinky: " + percept[i]);
                    break;
                case 4: System.out.println("New room has pit: " + percept[i]);
                    break;
                case 5: System.out.println("New room has wumpus: " + percept[i]);
                    break;
                case 6: System.out.println("New room has gold: " + percept[i]);
                    break;
                case 7: System.out.println("Hear wumpus scream: " + percept[i]);
                    break;
                
        }
        }
        System.out.println("currentLocation" + currentLocation.i + " " + currentLocation.j);
    }
}
