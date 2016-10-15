package WumpusWorld;
/**
 * Wumpus World using First Order Logic
 * Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */

import java.util.Scanner;

public class WumpusWorld {

    public static void main(String[] args) {
        int size = 5;
        double pitP = .20;
        double obstacleP = .20;
        double wumpusP = .20;

        Map m = new Map(size, pitP, obstacleP, wumpusP);

        int x = 0;
        String cow;
        m.print();
       
        
        m.printAttributes();
        do {
            Scanner input = new Scanner(System.in);
            System.out.println("which i for wumpus remove?");
            int i = input.nextInt();

            System.out.println("which j for wumpus remove?");
            int j = input.nextInt();

            m.removeWumpus(i, j);
            System.out.println("end?");
            cow = input.next();
            m.print();
            m.printAttributes();
        } while (!cow.equals("end"));
        

    }

}