package KnowledgeBasePackage;

public class KBRoom {

    //Knowlege base specific information
    public boolean unknown;
    public boolean safe;  
    public boolean kindaSafe;
    public boolean knownWumpus;
    public boolean possibleWumpus;
    public boolean knownPit;
    public boolean possiblePit;
    public boolean obstacle;
    public boolean visited;
    
    //percepts
    public boolean breeze;
    public boolean stench;
    public boolean glitter;
    
    public KBRoom(){
    unknown = true;
    safe = false;
    kindaSafe = false;
    knownWumpus = false;
    possibleWumpus = false;
    knownPit = false;
    possiblePit = false;
    obstacle = false;
    visited = false;
    
    //percepts
    breeze = false;
    stench = false;
    glitter = false;
    }
    
    public void printRoom(int i, int j){
        System.out.println("i: "+ i);
        System.out.println("j: "+ j);
        System.out.println("unkown: "+ unknown);
        System.out.println("safe: "+ safe);
        System.out.println("kindaSafe: "+ kindaSafe);
        System.out.println("knownWumpus: "+ knownWumpus);
        System.out.println("possibleWumpus: "+ possibleWumpus);
        System.out.println("knownPit: "+ knownPit);
        System.out.println("possiblePit: "+ possiblePit);
        System.out.println("obstacle: "+ obstacle);
        System.out.println("visited: "+ visited);
        System.out.println("breeze: "+ breeze);
        System.out.println("stench: "+ stench);
        
        
    }
}