/**
 * Ballot.java
 * 
 * <p>This file implements the class for each ballot.</p>
 * 
 * @author Jackie Li, Dominic Deiman, Aahan Tyagi, Alex Grenier
 */

public class Ballot {
    private int[] votes;
    private int rank;

    /**
     * Ballot constructor
     */
    public Ballot() {
        this.votes = null;
        this.rank = 0;
    }

        /**
         * 
         * @return votes to get
         */
    public int[] getVotes() {
        return votes;
    }
    
    /**
     * 
     * @param votes to set
     */
    public void setVotes(int[] votes) {
        this.votes = votes;
    }

    /**
     * 
     * @return rank to get
     */
    public int getRank() {
        return rank;
    }
    
    /**
     * 
     * @param rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * increaseRank incrementer
     */
    public void increaseRank() {
        rank += 1;
    }

    /**
     * Ballot is used to build the ballots used in IR and CPL systems.
     * 
     * @param voteString
     * @param numCandidates
     */
    public Ballot(String voteString, int numCandidates) {
        this.votes = new int[numCandidates];
        this.rank = 1;

        // The input is a string like 1,,2, for the votes that are in the csv file
        // Split the string by commas, then loop through that and add it to the votes Vector
        String[] split = voteString.split(",");
        for (int i = 0; i < numCandidates; i++) {
            if((i > split.length - 1) || split[i].length() == 0) {
                this.votes[i] = 0;
            } else {
                this.votes[i] = Integer.parseInt(split[i]);
            }
        }

    }
}
