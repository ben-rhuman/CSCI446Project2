
package KnowledgeBasePackage;

public class KBRoom {

    //Knowlege base specific information
    public boolean unknown;
    public boolean safe;  
    public boolean knownWumpus;
    public boolean possibleWumpus;
    public boolean knownPit;
    public boolean possiblePit;
    public boolean obstacle;
    
    //percepts
    public boolean breeze;
    public boolean stench;
    public boolean glitter;
    
    public KBRoom(){
    unknown = true;
    safe = false;
    knownWumpus = false;
    possibleWumpus = false;
    knownPit = false;
    possiblePit = false;
    obstacle = false;
    
    //percepts
    breeze = false;
    stench = false;
    glitter = false;
    }
}

