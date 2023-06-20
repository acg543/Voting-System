/**
 * Candidate.java
 * 
 * <p>This file implements the class for each candidate.</p>
 * 
 * @author Jackie Li, Dominic Deiman, Aahan Tyagi, Alex Grenier
 */

public class Candidate{
    private String name;
    private int numVotes;
    private boolean used;

    /**
     * Candidate constructor
     * 
     * @param name
     */
    public Candidate(String name){
        this.name = name;
        this.numVotes = 0;
        this.used = false;
    }

    /**
     * 
     * @return name to get
     */
    public String getName(){
        return this.name;
    }

    /**
     * 
     * @param name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return used to get
     */
    public boolean getUsed() {
        return this.used;
    }

    /**
     * 
     * @param used to set
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * 
     * @return numVotes to get
     */
    public int getNumVotes(){
        return this.numVotes;
    }

    /**
     * 
     * @param votes to set
     */
    public void setNumVotes(int votes){
        this.numVotes = votes;
    }

    /**
     * incrementNumVotes incrementer
     */
    public void incrementNumVotes(){
        this.numVotes += 1;
    }

    /**
     * decrementNumVotes decrementer
     */
    public void decrementNumVotes(){
        this.numVotes -= 1;
    }
}