package WumpusWorld;

/**
 * Wumpus World using First Order Logic Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */

import java.util.Scanner;

public class WumpusWorld {

    public static void main(String[] args) {
        int size = 5;
        double pitP = .09;
        double obstacleP = .1;
        double wumpusP = .12;

        System.out.println("CSCI 446 Wumpus World");
        System.out.println("Isaac Sotelo, Ben Rhuman, Danny Kumpf");
        System.out.println("Cave Size: " + size);
        System.out.println("Probability of Spawning a Pit: " + pitP * 100 + "%");
        System.out.println("Probability of Spawning a Obstacle: " + obstacleP * 100 + "%");
        System.out.println("Probability of Spawning a Wumpus: " + wumpusP * 100 + "%");
        System.out.println("-------------------------------------------------------------------");
        System.out.println("\n Map Key");
        System.out.println("    Agent; 'A' (North), '>' (East), 'V' (South), '<' (West)");
        System.out.println("    Pit : P");
        System.out.println("    Wumpus : W");
        System.out.println("    Obstacle : O");
        System.out.println("    Empty : E");
        System.out.println("    Gold : G");

        Map m = new Map(size, pitP, obstacleP, wumpusP);
        Agent a = new Agent(0, 0, 1, 2, m);

        ///////////////////////////Reactive agent ///////////////////////////////
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
        System.out.println("Number of Trials: " + numTrials);
        System.out.println("");
        for (int i = 0; i < 5; i++) {
            size += 5;
            for (int j = 1; j <= numTrials; j++) {

                Map n = new Map(size, pitP, obstacleP, wumpusP);

                ReactiveAgent r = new ReactiveAgent(0, 0, 1, 2, numMoves, n);

                avgGold += r.payOff;
                avgMoves += r.moveCounter;
                avgUnqRooms += r.uniqueVisitedSpots;
                avgWumpKilled += r.wumpusesKilled;
                avgDeathPit += r.deathByPit;
                avgDeathWump += r.deathByWump;
                avgObstacleBump += r.obstacleBump;
                avgTimesTrappedOrNoMove += r.trappedOrNoMoves;

            }
            avgGold = avgGold / numTrials;
            avgMoves = avgMoves / numTrials;
            avgUnqRooms = avgUnqRooms / numTrials;
            avgWumpKilled = avgWumpKilled / numTrials;
            avgDeathPit = avgDeathPit / numTrials;
            avgDeathWump = avgDeathWump / numTrials;
            avgObstacleBump = avgObstacleBump / numTrials;

            System.out.println("    Size of Map: " + size + "X" + size);
            System.out.println("    Max number of moves available: " + numMoves * size);
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
    }

}
