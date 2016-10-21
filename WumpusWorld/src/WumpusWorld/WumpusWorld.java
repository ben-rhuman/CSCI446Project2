package WumpusWorld;
/**
 * Wumpus World using First Order Logic
 * Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */

import java.util.Scanner;

public class WumpusWorld {

    public static void main(String[] args) {
        int size = 3;
        double pitP = .3;
        double obstacleP = .0;
        double wumpusP = .4;

        
        Map m = new Map(size, pitP, obstacleP, wumpusP);
        
        ReactiveAgent r = new ReactiveAgent(0,0,1,2,m);
        //Agent a =  new Agent(0, 0, 1, 2, m);

    }

}