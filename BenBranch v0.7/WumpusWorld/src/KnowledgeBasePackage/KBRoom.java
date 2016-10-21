
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
}

