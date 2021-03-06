package WumpusWorld;



/**
 * Wumpus World using First Order Logic
 * Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */


import WumpusWorld.Room;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Map {

    Random rand = new Random();

    private Room[][] world;      //world itself
    public int size;             //size of world   
    private List<Location> wumpusList = new ArrayList();
    //public Location agentLocation = new Location(0, 0);
    private Location goldLocation;

    public Map(int size, double p, double obs, double w) {

        this.size = size;

        world = new Room[size][size];

        initiateRooms(); //initialize the room objects

        setupGold();
        fillMap(p, obs, w);

    }

    public void printAttributes() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                System.out.println("Spot" + j + ", "+ i);
                System.out.println("breeze?"+world[j][i].getBreeze());
                System.out.println("stench?"+world[j][i].getStench());
                System.out.println("glitter?"+world[j][i].getGlitter());
            }
            System.out.println("");
        }
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

                } else if (rNum > obs && rNum <= p) { //a wumpus was chosen
                    world[i][j].setRoom('w');

                    setStench(i, j);

                    Location l = new Location(i, j);
                    wumpusList.add(l);

                } else if (rNum > p && rNum <= w) {  //a pit was chosen
                    world[i][j].setRoom('p');
                    setBreeze(i, j);

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

            Location w = wumpusList.get(k);

            if (i == w.i && j == w.j) {
                wumpusList.remove(k);
                break;
            }
        }

        for (int k = 0; k < world.length; k++) { //remove all of the stench from the world
            for (int l = 0; l < world.length; l++) {
                if (world[k][l].getStench() == true) {
                    world[k][l].removeStench();
                }
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

public boolean[] move(Location location, int direction){
    boolean[] percept = {
        false, //(0)Move suceeded (True) move failed/wall (False)
        false, //(1)New room obstacle
        false, //(2)New room is breezy room 
        false, //(3)New room is stinky room
        false, //(4)New room is pit
        false, //(5)New room is wumpus
        false //(6)New room is gold
    };
    try{
        if(direction == 1){
            percept = checkPercepts(world[location.i][location.j-1]);
        }else if(direction == 2){
            percept = checkPercepts(world[location.i+1][location.j]);
        }else if(direction == 3){
            percept = checkPercepts(world[location.i][location.j+1]);
        }else{
            percept = checkPercepts(world[location.i-1][location.j]);
        }
    }catch(IndexOutOfBoundsException e){
        percept[0] = false;
    }  
    return percept;
}

public boolean[] checkPercepts(Room room){
    boolean[] percept = new boolean[7];
    percept[0] = true;

    if(room.getRoom() == 'o'){
        percept[1] = true;
         percept[0] = false;
    } else if(room.getRoom() == 'p'){
        percept[4] = true;
    } else if(room.getRoom() == 'w'){
        percept[5] = true;
    }else if(room.getRoom() == 'g'){
        percept[6] = true;
    }
    percept[2] = room.getBreeze();
    percept[3] = room.getStench();

    return percept;
}

public boolean[] checkPerceptAtLocation(Location loc){
    Room room = world[loc.i][loc.j];
    boolean[] percept = new boolean[7];
    percept[0] = false;

    if(room.getRoom() == 'o'){
        percept[1] = true;
         percept[0] = false;
    } else if(room.getRoom() == 'p'){
        percept[4] = true;
    } else if(room.getRoom() == 'w'){
        percept[5] = true;
    }else if(room.getRoom() == 'g'){
        percept[6] = true;
    }
    percept[2] = room.getBreeze();
    percept[3] = room.getStench();

    return percept;
}

    public boolean shootArrow(Location l, int d) {
        //true = scream
        //false = no scream

        if (d == 1) {
            for (int i = l.i; i >= 0; i--) {
                if (world[i][l.j].getRoom() == 'o') {
                    return false;
                }
                if (world[i][l.j].getRoom() == 'w') {
                    removeWumpus(i, l.j);
                    return true;
                }
            }
        } else if (d == 2) {
            for (int j = l.i; j < size; j++) {
                if (world[l.i][j].getRoom() == 'o') {
                    return false;
                }
                if (world[l.i][j].getRoom() == 'w') {
                    removeWumpus(l.i, j);
                    return true;
                }
            }
        } else if (d == 3) {
            for (int i = l.i; i < size; i++) {
                if (world[i][l.j].getRoom() == 'o') {
                    return false;
                }
                if (world[i][l.j].getRoom() == 'w') {
                    removeWumpus(i, l.j);
                    return true;
                }
            }
        } else { //shoot arrow west
            for (int j = l.i; j >= 0; j--) {
                if (world[l.i][j].getRoom() == 'o') {
                    return false;
                }
                if (world[l.i][j].getRoom() == 'w') {
                    removeWumpus(l.i, j);
                    return true;
                }
            }
        }
        return false;
    }


/////////////////End Agent Interfacing Methods//////////////////////


    public void print() {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                System.out.print(world[j][i].getRoom());
            }
            System.out.println("");
        }
    }
}
