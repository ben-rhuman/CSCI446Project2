package WumpusWorld;

/**
 * Wumpus World using First Order Logic Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */
import java.util.Scanner;

public class WumpusWorld {

    public static void main(String[] args) {
        int size = 0;
        double pitP = .05;
        double obstacleP = .05;
        double wumpusP = .05;

        int avgGold = 0;
        int avgMoves = 0;
        int avgUnqRooms = 0;
        int avgWumpKilled = 0;
        int avgDeathPit = 0; 
        int avgDeathWump = 0; 
        int avgObstacleBump = 0;
        int avgTimesTrappedOrNoMove = 0;
        int numMoves = 300;
        
        int numTrials = 1000;
        
        System.out.println("\nFinal Statistics:");
        System.out.println("Number of Trials: "+ numTrials);
        System.out.println("");
        for (int i = 0; i < 5; i++) {
            size += 5;
            for (int j = 1; j <= numTrials; j++) {
                

                Map m = new Map(size, pitP, obstacleP, wumpusP);

                ReactiveAgent r = new ReactiveAgent(0, 0, 1, 2, numMoves, m);
                
                avgGold += r.payOff;
                avgMoves += r.moveCounter;
                avgUnqRooms += r.uniqueVisitedSpots;
                avgWumpKilled += r.wumpusesKilled;
                avgDeathPit += r.deathByPit;
                avgDeathWump += r.deathByWump;
                avgObstacleBump += r.obstacleBump;
                avgTimesTrappedOrNoMove += r.trappedOrNoMoves;
                
            }
            avgGold = avgGold/numTrials;
            avgMoves = avgMoves/numTrials;
            avgUnqRooms = avgUnqRooms/numTrials;
            avgWumpKilled = avgWumpKilled/numTrials;
            avgDeathPit = avgDeathPit/numTrials;
            avgDeathWump = avgDeathWump/numTrials;
            avgObstacleBump = avgObstacleBump/numTrials;
            
            
            
            
            System.out.println("    Size of Map: " + size + "X"+ size );
            System.out.println("    Max number of moves available: " + numMoves*size);
            System.out.println("    Number of times ran out of moves or was trapped: " + avgTimesTrappedOrNoMove);
            System.out.println("    Average Gold: " + avgGold);
            System.out.println("    Average Number of Moves: " + avgMoves);
            System.out.println("    Average Unique Rooms Explored: " + avgUnqRooms);
            System.out.println("    Average Number of Wumpi Killed: " + avgWumpKilled);
            System.out.println("    Average Amount of Death by Pits: " + avgDeathPit);
            System.out.println("    Average Amount of Death by Wumpus: " + avgDeathWump);
            System.out.println("    Average Number of Obstacles Bumped into: " + avgObstacleBump);
            System.out.println("");
            
                avgGold = 0;
                avgMoves = 0;
                avgUnqRooms = 0;
                avgWumpKilled = 0;
                avgDeathPit = 0;
                avgDeathWump = 0;
                avgObstacleBump = 0;
                avgTimesTrappedOrNoMove = 0;

        }
        //Agent a =  new Agent(0, 0, 1, 2, m);
    }

}
