import java.util.*;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * CPL.java
 * 
 * <p>This file implements and runs the closed party list form of voting.</p>
 * <p>It does so by creating a new file, initializing ballots, writing information to the audit file,
 * resolving ties, updating the ballots, and determining a winner.</p>
 * 
 * @author Jackie Li, Dominic Deiman, Aahan Tyagi, Alex Grenier
 */
public class CPL implements VotingType{
    private String auditFileName;
    private VotingSystem votingSystem;
    private int numSeats;
    private boolean tieBreaker;

    /**
     * CPL constructor
     * 
     * @param auditFileName
     * @param votingSystem
     * @param numSeats
     */
    public CPL(String auditFileName, VotingSystem votingSystem, int numSeats){
        this.auditFileName = auditFileName;
        this.votingSystem = votingSystem;
        this.numSeats = numSeats;
        this.tieBreaker = false;
    }

    /**
     * 
     * @return auditFileName to get
     */
    public String getAuditFileName() {
        return this.auditFileName;
    }

    /**
     * 
     * @param auditFileName to set
     */
    public void setAuditFileName(String auditFileName) {
        this.auditFileName = auditFileName;
    }

    /**
     * 
     * @return votingSystem to get
     */
    public VotingSystem getVotingSystem() {
        return this.votingSystem;
    }

    /**
     * 
     * @param votingSystem to set
     */
    public void setVotingSystem(VotingSystem votingSystem) {
        this.votingSystem = votingSystem;
    }

    /**
     * 
     * @return tieBreaker to get
     */
    public boolean getTieBreaker() {
        return this.tieBreaker;
    }

    /**
     * 
     * @param tieBreaker to set
     */
    public void setTieBreaker(boolean tieBreaker) {
        this.tieBreaker = tieBreaker;
    }

    /**
     * writeToAudit writes information to the audit file.
     * 
     * @param contents to be written
     */
    public void writeToAudit(String contents){
        votingSystem = getVotingSystem();
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(getAuditFileName());
            byte[] strToBytes = contents.getBytes();
            fout.write(strToBytes);
        }
        catch( IOException e ){
            System.out.println("File \"" + getAuditFileName() + "\" not writeable.");
        }
        finally{
            if (fout != null){
                try {
                    // Closing the file connections
                    // if no exception has occurred
                    fout.close();
                }
                catch (IOException e) {
                    // Display exceptions if occurred
                    System.out.print(e.getMessage());
                }
            }
        }
        
    }

    /**
     * countVotes counts votes for each candidate.
     * 
     * @param ballots
     * @param candidates
     */
    public void countVotes(Vector<Ballot> ballots, Vector<Candidate> candidates) {
        for(Ballot ballot : ballots) {
            for (int i = 0; i < ballot.getVotes().length; i++) {
                if (ballot.getVotes()[i] == 1) {
                    candidates.get(i).incrementNumVotes();
                }
            }
        }
    }

    /**
     * mostVotes finds the candidate with the most votes.
     * This method can return multiple candidates.
     * 
     * @param candidates
     * @return hasMostVotes - the candidate with the most votes
     */
    public Vector<Candidate> mostVotes(Vector<Candidate> candidates) {
        int mostVotes = 0;
        Vector<Candidate> hasMostVotes = new Vector<Candidate>();

        for (Candidate candidate : candidates) {
            if (candidate.getNumVotes() > mostVotes) {
                mostVotes = candidate.getNumVotes();
                hasMostVotes.clear();
                hasMostVotes.add(candidate);
            }
            else if (candidate.getNumVotes() == mostVotes) {
                hasMostVotes.add(candidate);
            }
        }
        return hasMostVotes;
    }

    /**
     * findWinner selects a winner by random tie breaker or if there is only one candidate with the most votes.
     * 
     * @param hasMostVotes
     * @return hasMostVotes updated
     */
    public String findWinner(Vector<Candidate> hasMostVotes) {
        if (hasMostVotes.size() > 1) {
            Candidate winner = tieBreaker(hasMostVotes);
            this.tieBreaker = true;
            return winner.getName();
        }

        return hasMostVotes.firstElement().getName();
    }

    /**
     * resolveTie uses random number generation to break ties.
     * 
     * @param n as a counter
     * @return -1
     */
    public int resolveTie( int n ){
        double randomNumber = Math.random();
        for ( int i = 0; i < n; i++ ){
            if ( randomNumber >= i/(double)n && randomNumber < (i+1)/(double)n ){
                // debug
                // System.out.println(randomNumber);
                // System.out.println(i);
                return i;
            }
        }
        return -1;
    }

    /**
     * tieBreaker is used to call the resolveTie() method
     * 
     * @param candidates
     * @return candidates updated
     */
    public Candidate tieBreaker(Vector<Candidate> candidates) {
        return candidates.get(resolveTie(candidates.size()));
    }

    /**
     * auditContent builds the audit file itself
     * 
     * @param ballots
     * @param candidates
     * @param winner
     * @return finalContent which holds audit file info
     */
    public String auditContent(Vector<Ballot> ballots, Vector<Candidate> candidates, String winner) {
        String votingType = "CPL \n";
        String candidateInfo = "Number of Candidates: " + String.valueOf(ballots.firstElement().getVotes().length) + "\n";

        for (Candidate candidate: candidates) {
            candidateInfo += candidate.getName() + " " + String.valueOf(candidate.getNumVotes()) + "\n";
        }

        String numberOfBallots = "Number of Ballots: " + String.valueOf(ballots.size()) + "\n";

        if (this.tieBreaker) {
            String tieBreak = "Tiebreak Done. Winner: " + winner + "\n";
            numberOfBallots += tieBreak;
        }

        String finalContent = votingType + candidateInfo + numberOfBallots;

        return finalContent;
    }

    /**
     * calculateResults is a parent method to calculate the candidate with the most votes.
     * 
     * @param ballots
     * @param candidates
     * @return winner
     */
    public String calculateResults(Vector<Ballot> ballots, Vector<Candidate> candidates){
        countVotes(ballots, candidates);
        Vector<Candidate> hasMostVotes = mostVotes(candidates);
        String winner = findWinner(hasMostVotes);

        String content = auditContent(ballots, candidates, winner);
        writeToAudit(content);
        
        return winner;
    }

}