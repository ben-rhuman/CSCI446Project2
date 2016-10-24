package WumpusWorld;

import WumpusWorld.Location;
import WumpusWorld.Map;
import static java.lang.Math.abs;
import static java.lang.Math.random;
import KnowledgeBasePackage.KnowledgeBase;
import InferenceEnginePackage.InferenceEngine;
import java.util.ArrayList;

/*
 * Wumpus World using First Order Logic
 * Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */

public class ReactiveAgent {

    public int payOff;
    public int movesAllowed = 0;
    public int trappedOrNoMoves = 0;
    private int direction; //1 = North, 2 = East, 3 = South, 4 = West
    public int moveCounter = 0;
    public int wumpusesKilled = 0;
    public int deathByWump = 0;
    public int deathByPit = 0;
    public int obstacleBump = 0;
    public int uniqueVisitedSpots;
    private Location currentLocation;
    private int arrowCount;
    private Map worldMap;
    private boolean done;
    private boolean[] percept;
    private KnowledgeBase KB;
    

//(0)Move suceeded (True) move failed/wall (False)
//(1)New room obstacle
//(2)New room is breezy room 
//(3)New room is stinky room
//(4)New room is pit
//(5)New room is wumpus
//(6)New room is gold
    public ReactiveAgent(int x, int y, int numGold, int direction,int numMoves, Map worldMap) {  //Direction in our case should always be 2 (i.e. East) at start.

        KB = new KnowledgeBase(worldMap.size);
        currentLocation = new Location(x, y);
        this.payOff = numGold;
        this.direction = direction;
        this.worldMap = worldMap;
        arrowCount = worldMap.getNumberWumpus();
        movesAllowed = numMoves;

        //System.out.println("Initial Map State : " + worldMap.size + " X " + worldMap.size);
        //worldMap.print(currentLocation, direction);

        percept = worldMap.checkPerceptAtLocation(currentLocation);
        playGame();
    }

////////////////////////// Game Execution Functions ////////////////////////////
    private void playGame() {
        done = false;

        for (int i = 0; i < worldMap.size * movesAllowed; i++) {
            if (done == true) {
                break;
            }

            if (!KB.KBMap[currentLocation.i][currentLocation.j].visited) {
                KB.KBMap[currentLocation.i][currentLocation.j].visited = true;
                uniqueVisitedSpots++;
            }
            percept = worldMap.checkPerceptAtLocation(currentLocation);

            reactiveMove();

        }

        if (done != true) {
            trappedOrNoMoves++;
            //System.out.println("\nEnd Result: Got trapped/out of moves!");
        }

        //System.out.println(" ");
        //printStats();

    }

    private void resetPercepts() {
        for (int i = 0; i < percept.length; i++) {
            percept[i] = false;
        }
    }

    private void reactiveMove() {

        if (percept[6]) {		//New room is Gold
            if (grab()) {
                win();
            }
        } else if (percept[1]) {		//New room obstacle

            updateLocationBackward();

        } else if (percept[3]) {		//Sense a wumpus
            if (arrowCount == 0) {
                if (moveRand()) {
                    updateLocationForward();
                }
            } else {
                boolean scream = shootArrow();
                if (scream) {
                    wumpusesKilled++;
                }
                turnRight();

            }
        } else if (percept[2]) {		//Sense a breeze
            if (moveRand()) {
                updateLocationForward();
            }
        } else if (percept[4]) {		//New room is Pit

            //System.out.println("Fell in a pit.");
        } else if (percept[5]) {		//New room is Wumpus

            //System.out.println("Eaten by a Wumpus.");
        } else if (moveRand()) {
            updateLocationForward();
        }

    }

    private boolean moveRand() {

        double dir = Math.random();
        if (dir <= .25) {
            turnRight();
            percept = move();
        } else if (dir > .25 && dir <= .50) {
            turnLeft();
            percept = move();
        } else if (dir > .50 && dir <= .75) {
            turnRight();
            turnRight();
            percept = move();
        } else if (dir > .75) {
            percept = move();
        }

        if (percept[1]) {
            obstacleBump++;
        }
        if (percept[4]) {
            payOff -= 100;
            deathByPit++;
        }
        if (percept[5]) {
            payOff -= 100;
            deathByWump++;
        }
        return percept[0];
    }

    private void updateLocationForward() {
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

    private void updateLocationBackward() {
        if (direction == 1) {
            currentLocation.j++;
        } else if (direction == 2) {
            currentLocation.i--;
        } else if (direction == 3) {
            currentLocation.j--;
        } else {
            currentLocation.i++;
        }
    }

    private void win() {
        //System.out.println("End Result: The Agent Found The Gold And Won!");
        payOff += 1000;

        done = true;
    }

    private void printStats() {
        System.out.println("Final Map");
        worldMap.print(currentLocation, direction);
        System.out.println("Statistics:");
        System.out.println("    Final Gold: " + payOff);
        System.out.println("    Total number of moves: " + moveCounter);
        System.out.println("    Unique rooms explored: " + uniqueVisitedSpots);
        System.out.println("    Wumpuses Killed: " + wumpusesKilled);
        System.out.println("    Number of pits: " + worldMap.numPits);
        System.out.println("    Times died by pit: " + deathByPit);
        System.out.println("    Number of wumpus: " + worldMap.numWumpus);
        System.out.println("    Times died by wumpus: " + deathByWump);
        System.out.println("    Number of obstacles: " + worldMap.numObstacles);
        System.out.println("    Number of obstacles bumped: " + obstacleBump);
        System.out.println("");
    }

////////////////////////// End Game Execution Functions ////////////////////////////
//////////////////////// Agent Action Methods //////////////////////////////////
    private boolean[] move() { //Moves the agent one space in the direction its facing.
        moveCounter++;
        payOff -= 1;
        //System.out.println(moveCounter + ": Moved forward.");
        return worldMap.move(currentLocation, direction);  //Returns a list of 7 percepts
    }

    private void turnLeft() { // Rotates the agent 90 degrees to the left                  
        if (direction == 1) {
            direction = 4;
        } else {
            direction--;
        }
        payOff -= 1;
        moveCounter++;
        //System.out.println(moveCounter + ": Turned left.");
    }

    private void turnRight() { // Rotates the agent 90 degrees to the right
        if (direction == 4) {
            direction = 1;
        } else {
            direction++;
        }
        payOff -= 1;
        moveCounter++;
        //System.out.println(moveCounter + ": Turned right.");
    }

    private boolean shootArrow() {  //Shoots an arrow in the direction the agent is facing
        moveCounter++;
        if (arrowCount <= 0) {
            //System.out.println(moveCounter + ": Could not shoot an arrow.");
            return false;
        }

        arrowCount--;
        //System.out.println(arrowCount + " arrows");
        payOff -= 10; //shooting an arrow
        //System.out.println(moveCounter + ": Shot an arrow.");
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
            //System.out.println(moveCounter + ": Grab gold.");
            return true;
        }
        System.out.println(moveCounter + ": Grabbed but no gold.");
        return false;
    }

    //////////////////////// End Agent Action Methods //////////////////////////////////
    public void printPercepts(boolean[] percept, Location currentLocation) {
        for (int i = 0; i < percept.length; i++) {
            switch (i) {
                case 0:
                    System.out.println("Move suceeded/failed: " + percept[i]);
                    break;
                case 1:
                    System.out.println("New room obstacle: " + percept[i]);
                    break;
                case 2:
                    System.out.println("New room is breezy: " + percept[i]);
                    break;
                case 3:
                    System.out.println("New room is stinky: " + percept[i]);
                    break;
                case 4:
                    System.out.println("New room has pit: " + percept[i]);
                    break;
                case 5:
                    System.out.println("New room has wumpus: " + percept[i]);
                    break;
                case 6:
                    System.out.println("New room has gold: " + percept[i]);
                    break;
                case 7:
                    System.out.println("Hear wumpus scream: " + percept[i]);
                    break;

            }
        }
        System.out.println("currentLocation: (" + currentLocation.i + ", " + currentLocation.j + ")");
    }
}