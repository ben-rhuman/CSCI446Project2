/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KnowledgeBasePackage;

/**
 *
 * @author benrhuman
 */
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
    
    //percepts
    breeze = false;
    stench = false;
    glitter = false;
    }
}

