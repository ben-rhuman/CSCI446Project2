package WumpusWorld;
/**
 * Wumpus World using First Order Logic
 * Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */

import java.util.Scanner;

public class WumpusWorld {

    public static void main(String[] args) {
        int size = 0;
        double pitP = .2;
        double obstacleP = .2;
        double wumpusP = .2;

        for(int i = 0 ; i < 5; i++){
            size += 5;
            
        Map m = new Map(size, pitP, obstacleP, wumpusP);
        
        ReactiveAgent r = new ReactiveAgent(0,0,1,2,m);
        }
        //Agent a =  new Agent(0, 0, 1, 2, m);

    }

}