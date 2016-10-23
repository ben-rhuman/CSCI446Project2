package WumpusWorld;

/**
 * Wumpus World using First Order Logic Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Map {

    Random rand = new Random();

    private Room[][] world;      //world itself
    public int size;             //size of world   
    private List<Location> wumpusList = new ArrayList();
    private Location goldLocation;
    private int numObstacles;
    private int numPits;
    private int numWumpus;

    public Map(int size, double p, double obs, double w) {

        this.size = size;

        world = new Room[size][size];

        initiateRooms(); //initialize the room objects

        setupGold();
        fillMap(p, obs, w);

    }

/////////////////////MAP SETUP////////////////////////////
    public void initiateRooms() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Room rm = new Room();
                world[i][j] = rm;
            }
        }
    }

    public void setupGold() {
        //finding a random location for the gold

        int i;
        int j;

        do {
            i = rand.nextInt(size);
            j = rand.nextInt(size);

        } while (i == 0 && j == 0);

        goldLocation = new Location(i, j);

        world[i][j].setRoom('g'); //set the gold in the map
        world[i][j].setGlitter();
    }

    public void fillMap(double p, double obs, double w) {

        p = obs + p; //creating a range for the random selection to occur
        w = p + w;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                if (i == goldLocation.i && j == goldLocation.j) { //do not put anything where the gold is
                    continue;
                }
                if (i == 0 && j == 0) {
                    world[0][0].setRoom('e'); //setting up the agents spawn
                    continue;
                }

                double rNum = rand.nextDouble();

                if (rNum <= obs) { // an obstacle was chosen
                    world[i][j].setRoom('o');
                    numObstacles++;

                } else if (rNum > obs && rNum <= p) { //a pit was chosen
                    world[i][j].setRoom('p');
                    numPits++;
                    setBreeze(i, j);

                } else if (rNum > p && rNum <= w) {  //a wumpus was chosen
                    world[i][j].setRoom('w');
                    numWumpus++;
                    setStench(i, j);
                    Location l = new Location(i, j);
                    wumpusList.add(l);
                } else {                            //empty space was chosen
                    world[i][j].setRoom('e');

                }

            }
        }
    }

/////////////////End MAP SETUP///////////////////////////////////
    public void removeWumpus(int i, int j) {

        world[i][j].setRoom('e'); //changing the wumpus on the map

        for (int k = 0; k < wumpusList.size(); k++) { //remove the wumpus from the list of wumpi

            if (i == wumpusList.get(k).i && j == wumpusList.get(k).j) {
                wumpusList.remove(k);
                break;
            }
        }

        for (int k = 0; k < world.length; k++) { //remove all of the stench from the world
            for (int l = 0; l < world.length; l++) {

                world[k][l].removeStench();

            }
        }

        for (int k = 0; k < wumpusList.size(); k++) { //reinstantiate all the wumpus stench

            setStench(wumpusList.get(k).i, wumpusList.get(k).j);
        }

    }

/////////////Setting Methods///////////////////////
    public void setStench(int i, int j) {
        try {
            world[i + 1][j].setStench();
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            world[i - 1][j].setStench();
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            world[i][j + 1].setStench();
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            world[i][j - 1].setStench();
        } catch (IndexOutOfBoundsException e) {

        }

    }

    public void setBreeze(int i, int j) {
        try {
            world[i + 1][j].setBreeze();
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            world[i - 1][j].setBreeze();
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            world[i][j + 1].setBreeze();
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            world[i][j - 1].setBreeze();
        } catch (IndexOutOfBoundsException e) {

        }

    }

///////////////////End Setting Methods//////////////////////////////
/////////////////////Agent Interfacing Methods//////////////////////
    public boolean[] move(Location location, int direction) {

        boolean[] percept = {
            false, //(0)Move suceeded (True) move failed/wall (False)
            false, //(1)New room obstacle
            false, //(2)New room is breezy room 
            false, //(3)New room is stinky room
            false, //(4)New room is pit
            false, //(5)New room is wumpus
            false, //(6)New room is gold
            false
        };

        try {
            if (direction == 1) {

                percept = checkPercepts(world[location.i][location.j - 1]);
                if (location.i < 0 || location.i >= world.length || location.j - 1 < 0 && location.j >= world.length) {
                    percept[0] = false;
                }
            } else if (direction == 2) {

                percept = checkPercepts(world[location.i + 1][location.j]);
                if (location.i < 0 || location.i + 1 >= world.length || location.j < 0 && location.j >= world.length) {
                    percept[0] = false;
                }
            } else if (direction == 3) {

                percept = checkPercepts(world[location.i][location.j + 1]);
                if (location.i < 0 || location.i >= world.length || location.j < 0 && location.j + 1 >= world.length) {
                    percept[0] = false;
                }
            } else {

                percept = checkPercepts(world[location.i - 1][location.j]);
                if (location.i - 1 < 0 || location.i >= world.length || location.j < 0 && location.j >= world.length) {
                    percept[0] = false;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            percept[0] = false;
        }
        return percept;
    }

    public boolean[] checkPercepts(Room room) {

        boolean[] percept = new boolean[8];
        percept[0] = true;

        if (room.getRoom() == 'o') {
            percept[1] = true;
            percept[0] = false;
        } else if (room.getRoom() == 'p') {
            percept[0] = false;
            percept[4] = true;
            
        } else if (room.getRoom() == 'w') {
            percept[0] = false;
            percept[5] = true;
        } else if (room.getRoom() == 'g') {
            percept[6] = true;
        }
        percept[2] = room.getBreeze();
        percept[3] = room.getStench();
        percept[7] = false;

        return percept;
    }

    public boolean[] checkPerceptAtLocation(Location loc) {
        Room room = world[loc.i][loc.j];
        boolean[] percept = new boolean[8];
        percept[0] = false;

        if (room.getRoom() == 'o') {
            percept[1] = true;
            percept[0] = false;
        } else if (room.getRoom() == 'p') {
            percept[4] = true;
        } else if (room.getRoom() == 'w') {
            percept[5] = true;
        } else if (room.getRoom() == 'g') {
            percept[6] = true;
        }
        percept[2] = room.getBreeze();
        percept[3] = room.getStench();
        percept[7] = false;
        return percept;
    }

    public boolean shootArrow(Location l, int d) {
        //true = scream
        //false = no scream

        if (d == 1) { //shoot arrow north
            for (int j = l.j; j >= 0; j--) {
                if (world[l.i][j].getRoom() == 'o') {
                    return false;
                }
                if (world[l.i][j].getRoom() == 'w') {
                    removeWumpus(l.i, j);
                    return true;
                }
            }
        } else if (d == 2) { //shoot arrow east
            for (int i = l.i; i < size; i++) {
                if (world[i][l.j].getRoom() == 'o') {
                    return false;
                }
                if (world[i][l.j].getRoom() == 'w') {
                    removeWumpus(i, l.j);
                    return true;
                }
            }
        } else if (d == 3) { //shoot arrow south
            for (int j = l.j; j < size; j++) {
                if (world[l.i][j].getRoom() == 'o') {
                    return false;
                }
                if (world[l.i][j].getRoom() == 'w') {
                    removeWumpus(l.i, j);
                    return true;
                }
            }
        } else { //shoot arrow west
            for (int i = l.i; i >= 0; i--) {
                if (world[i][l.j].getRoom() == 'o') {
                    return false;
                }
                if (world[i][l.j].getRoom() == 'w') {
                    removeWumpus(i, l.j);
                    return true;
                }
            }
        }
        return false;
    }

    public int getNumberWumpus() {
        return wumpusList.size();
    }

    public int getNumberObstacles() {
        return numObstacles;
    }
    public int getNumberPits(){
        return numPits;
    }
    public int getNumberWump2(){
        return numWumpus;
    }
/////////////////End Agent Interfacing Methods//////////////////////

    public void print(Location agentLoc, int direction) {
        System.out.println();
        printLine();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (agentLoc.i == j && agentLoc.j == i) {
                    if (direction == 1) {
                        System.out.print("| A ");
                    } else if (direction == 2) {
                        System.out.print("| > ");
                    } else if (direction == 3) {
                        System.out.print("| V ");
                    } else {
                        System.out.print("| < ");
                    }
                } else {
                    System.out.print("| " + world[j][i].getRoom() + " ");
                }
            }
            System.out.print("|\n");
            printLine();
        }
        System.out.println();
    }

    private void printLine() {
        System.out.print("|");
        for (int i = 0; i < size; i++) {
            System.out.print("---|");
        }
        System.out.println();
    }

    public void printAttributes() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                System.out.println("Spot" + j + ", " + i);
                System.out.println("breeze?" + world[j][i].getBreeze());
                System.out.println("stench?" + world[j][i].getStench());
                System.out.println("glitter?" + world[j][i].getGlitter());
            }
            System.out.println("");
        }
    }
}
