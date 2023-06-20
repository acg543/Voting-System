import java.util.Vector;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/** 
* IR.java
*
* <p>This file implements and runs the instant runoff form of voting.</p>
* <p>It does so by creating a new file, initializing ballots, writing information to the audit file,
* resolving ties, updating the ballots, and determining a winner.</p>
*
* @author Jackie Li, Dominic Deiman, Aahan Tyagi, Alex Grenier
*/
public class IR implements VotingType{
    private String auditFileName;
    private VotingSystem votingSystem;
    private Vector<String> orderOfRemoval = new Vector<>(0, 0);

    public Vector<String> getOrderOfRemoval() {
        return this.orderOfRemoval;
    }

    public void setOrderOfRemoval(Vector<String> orderOfRemoval) {
        this.orderOfRemoval = orderOfRemoval;
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
     * IR creates and names a new file.
     * 
     * @param auditFileName
     * @param votingSystem
     */
    public IR(String auditFileName, VotingSystem votingSystem){

        setAuditFileName(auditFileName);
        setVotingSystem(votingSystem);

        try{
            File auditFile = new File(auditFileName);
            auditFile.createNewFile();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * InitializeBallots creates the ballots used to store ballot info from CSV files.
     * 
     * Returns initialized ballot.
     * 
     * @param ballots
     * @param initial_value
     * @return newly created ballots
     */
    public Vector<Ballot> initializeBallots( Vector<Ballot> ballots, int initial_value ){
        for ( int i = 0; i < ballots.size() ; i++ ){
            ballots.get(i).setRank(initial_value);
        }

        return ballots;
    }

    /**
     * writeToAudit opens audit file, write the name given to it, and closes it.
     * It throws an exception if the file is unable to be written on.
     * 
     * @param contents
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
     * resolveTie is used to break ties in the IR process.
     * 
     * @param n used as a counter
     * @return -1
     */
    public int resolveTie( int n ){
        double randomNumber = Math.random();
        for ( int i = 0; i < n; i++ ){
            if ( randomNumber >= i/(double)n && randomNumber < (i+1)/(double)n ){
                return i;
            }
        }
        return -1;
    }

    /**
     * nextRankExists implements preferences for candidates.
     * 
     * @param votes
     * @param currRank
     * @return true or false
     */
    public boolean nextRankExists( int[] votes , int currRank ){
        
        for ( int i = 0 ; i < votes.length ; i++ ){
            if ( currRank+1 == votes[i] ){
                return true;
            }
        }
        return false;
    }

    /**
     * updateBallots updates the ranks of the candidates.
     * 
     * @param candidates
     * @param ballots
     * @return ballots that have been updated
     */
    public Vector<Ballot> updateBallots(Vector<Candidate> candidates, Vector<Ballot> ballots ){

        // according to least number of candidates the rank of the ballot will change
        Vector<Candidate> leastVotesCandidates = hasLeastVotes(candidates);
        Candidate leastVoteCandidate;

        // if there are multiple least vote candidates then resolve tie amongst them
        // can be put in a funciton 
        if ( leastVotesCandidates.size() > 1 ){
            leastVoteCandidate = leastVotesCandidates.get(resolveTie(leastVotesCandidates.size()));
        }
        else{
            leastVoteCandidate = leastVotesCandidates.get(0);
        }

        if (leastVoteCandidate.getNumVotes() > 0 ){
            // if the least vote candidate has votes we need to change the 
            // rank of that particulr ballot corresponding to the least vote candidate
            for ( int i = 0; i < ballots.size(); i++ ){
                int[] votes = ballots.get(i).getVotes();
                for ( int j = 0 ; j < votes.length ; j++ ){
                    if ( votes[j] == ballots.get(i).getRank() && candidates.get(j).equals(leastVoteCandidate) && !candidates.get(j).getUsed()){
                        if (nextRankExists(votes, j)){
                            ballots.get(i).increaseRank();
                        }
                        else{
                            // represents that there is no next rank available and we need to remove the ballot after we are 
                            // done with our operation
                            ballots.get(i).setRank(-1);
                        }
                        candidates.get(j).setUsed(true);
                        getOrderOfRemoval().add(candidates.get(j).getName());
                        break;
                    }
                }
            }

            // removing the ballots with rank -1
            for ( int i = 0; i < ballots.size() ; i++ ){
                if (ballots.get(i).getRank() == -1 ){
                    ballots.remove(i);
                    i--;
                }
            }

        }
        else{
            // if the least vote candidate has zero votes then there is no need to redistribute votes
            // search for the least vote candidate
            for ( int i = 0; i < candidates.size(); i++ ){
                if ( candidates.get(i).equals(leastVoteCandidate) ){
                    candidates.get(i).setUsed(true);
                    break;
                }
            }
        }

        return ballots;
    }

    /**
     * countVotes tallies votes for each candidate.
     * 
     * @param ballots
     * @param candidates
     * @return candidates that have been updated
     */
    public Vector<Candidate> countVotes( Vector<Ballot> ballots, Vector<Candidate> candidates){

        for ( int i = 0; i < ballots.size() ; i++ ){
            // candidates.get(ballots.get(i).getRank()).incrementNumVotes();
            int[] votes = ballots.get(i).getVotes();
            for ( int j = 0; j < votes.length; j++ ){
                if ( votes[j] == ballots.get(i).getRank() ){
                    candidates.get(j).incrementNumVotes();
                }
            }
        }

        return candidates;
    }
    
    /**
     * hasLeastVotes puts candidates with the least amount of votes into a vector.
     * This is to help resolve ties between them.
     * 
     * @param candidates
     * @return leastVotesCandidates
     */
    public Vector<Candidate> hasLeastVotes(Vector<Candidate> candidates){
        Vector<Candidate> leastVotesCandidates = new Vector<Candidate>();
        int min_votes = Integer.MAX_VALUE;

        // comparing the num of votes of a candidate to minimum number of votes
        for ( int i = 0 ; i < candidates.size() ; i++ ){
            if (candidates.get(i).getNumVotes() < min_votes && !candidates.get(i).getUsed()){
                min_votes = candidates.get(i).getNumVotes();
            }
        }

        // adding all candidates with minimum number of votes to leastVotesCandidates
        for ( int i = 0 ; i < candidates.size() ; i++ ){
            if ( candidates.get(i).getNumVotes() == min_votes ){
                leastVotesCandidates.add(candidates.get(i));
            }
        }

        return leastVotesCandidates;
    }

    /**
     * majorityVotesCheck checks to see if a candidate has received 50% or more of the total votes.
     * 
     * @param candidates
     * @param ballots
     * @return candidates or null
     */
    public String majorityVotesCheck( Vector<Candidate> candidates , Vector<Ballot> ballots){
        for ( int i = 0 ; i < candidates.size() ; i++ ){
            if (candidates.get(i).getNumVotes() > 0.5*ballots.size() ){
                // System.out.println(candidates.get(i).getName());
                return candidates.get(i).getName();
            }
        }
        return null;
    }

    /**
     * refreshCountOfVotes resets the candidate sheets after initiate cuts.
     * 
     * @param candidates
     * @return candidates
     */
    public Vector<Candidate> refreshCountOfVotes( Vector<Candidate> candidates ){
        for ( int i = 0 ; i < candidates.size() ; i++ ){
            candidates.get(i).setNumVotes(0);
        }

        return candidates;
    }

    /**
     * auditContent creates what is inputted into the audit.
     * 
     * @param ballots
     * @param candidates
     * @param winner
     * @return finalContent to be written to auditContent
     */
    public String auditContent(Vector<Ballot> ballots, Vector<Candidate> candidates, String winner) {
        String votingType = "IR \n";
        String candidateInfo = "Number of Candidates: " + String.valueOf(ballots.firstElement().getVotes().length) + "\n";

        for (Candidate candidate: candidates) {
            candidateInfo += candidate.getName() + " " + String.valueOf(candidate.getNumVotes()) + "\n";
        }

        String numberOfBallots = "Number of Ballots: " + String.valueOf(ballots.size()) + "\n";

        String finalContent = votingType + candidateInfo + numberOfBallots;

        finalContent = finalContent + "Order of Removal: ";

        for (String name : getOrderOfRemoval()){
            finalContent += name + " ";
        }

        finalContent += "\n";

        return finalContent;
    }

    /**
     * totalVotesCount counts the total votes for each round
     * 
     * @param votesForEachRound
     * @return sum
     */
    public int totalVotesCount(ArrayList<Vector<Integer>> votesForEachRound) {
        int sum = 0;
        for ( int i = 0; i < votesForEachRound.size(); i++ ){
            sum += votesForEachRound.get(i).get(0);
        }

        return sum;
    }

    /**
     * updateVotesForEachRound updates the votes for each round as IR requires
     * 
     * @param votesForEachRound
     * @param candidates
     */
    public void updateVotesForEachRound(ArrayList<Vector<Integer>> votesForEachRound ,Vector<Candidate> candidates){

        for ( int i = 0; i < votesForEachRound.size(); i++ ){
            votesForEachRound.get(i).add(candidates.get(i).getNumVotes());
        }
    }

    /**
     * IRVtable formats the IR data
     * 
     * @param votesForEachRound
     * @param candidates
     */
    public void IRVtable(ArrayList<Vector<Integer>> votesForEachRound, Vector<Candidate> candidates) {

        int num_of_rounds = votesForEachRound.get(0).size();

        // | Candidates      | 1st Round       |                 | 2nd Round       |                 
        System.out.format("| %-15s ","Candidates");
        for ( int i = 0 ; i < num_of_rounds ; i++ ){
            // System.out.format("| %-15s ", (i+1) + "st Round");
            if ( (i+1) % 10 == 1 && i+1 != 11){
                System.out.format("| %-15s ", (i+1) + "st Round");
            }
            else if ( (i+1) % 10 == 2 ){
                System.out.format("| %-15s ", (i+1) + "nd Round");
            }  
            else if ( (i+1) % 10 == 3 ){
                System.out.format("| %-15s ", (i+1) + "rd Round");
            }
            else {
                System.out.format("| %-15s ", (i+1) + "th Round");
            }
            System.out.format("| %-15s ", "");
        }
        System.out.print("\n");

        // | Candidate       | Votes           | -+              | Votes           | -+    
        System.out.format("| %-15s ","Candidate");
        for ( int i = 0 ; i < num_of_rounds ; i++ ){
            System.out.format("| %-15s ", "Votes");
            System.out.format("| %-15s ", "-+");
        }
        System.out.print("\n");

        // | Rosen (D)       | 3               | 3               | 3               | 0  
        for ( int i = 0 ; i < candidates.size() ; i++ ){
            System.out.format("| %-15s ",candidates.get(i).getName());
            for ( int j = 0 ; j < num_of_rounds ; j++ ){
                if ( j == 0 ){
                    System.out.format("| %-15s ", votesForEachRound.get(i).get(j));
                    System.out.format("| %-15s ", "+" + votesForEachRound.get(i).get(j));
                }
                else{
                    System.out.format("| %-15s ", votesForEachRound.get(i).get(j));
                    int diff = votesForEachRound.get(i).get(j) - votesForEachRound.get(i).get(j-1);
                    System.out.format("| %-15s ", ((diff >= 0) ? "+" + diff : "-" + diff));
                }
            }
            System.out.print("\n");
        }

        System.out.format("| %-15s ","TOTALS");
        for ( int i = 0; i < num_of_rounds; i++ ){
            if ( i == 0 ){
                System.out.format("| %-15s ", totalVotesCount(votesForEachRound));
                System.out.format("| %-15s ", "+" + totalVotesCount(votesForEachRound));
            }
            else{
                System.out.format("| %-15s ", "");
                System.out.format("| %-15s ", "");
            }
        }
        System.out.print("\n");
        
        
    }

    /**
     * calculateResults runs the process over and over until a candidate has the majority of votes.
     * 
     * @return majorityVotesCheck to determine winner
     */
    public String calculateResults(Vector<Ballot> ballots, Vector<Candidate> candidates){
        // IR algorithm

        // initializing the reference counting
        // using rank instead of this
        // currBallot = initialize(currBallot, initial_value);

        // initializing rank of all ballots to 1
        int initial_value = 1;
        ballots = initializeBallots(ballots,initial_value);

        // storing the votes for each candidates for each round
        // System.out.println(candidates.size());
        ArrayList<Vector<Integer>> votesForEachRound = new ArrayList<Vector<Integer>>();
        // votesForEachRound.setSize(candidates.size());
        // System.out.println(votesForEachRound.size());

        // create a function for this
        for ( int i = 0 ; i < candidates.size() ; i++ ){
            // Vector<Candidate> candidate = new Vector<Candidate>();
            votesForEachRound.add(new Vector<Integer>());
        }

        // System.out.println(votesForEachRound.size());


        setOrderOfRemoval(new Vector<String>());
        // int count = 0;
        // there will be candidates.size()-1 rounds or less (until we hit majority)
        while (true){
            // count++;

        setOrderOfRemoval(new Vector<String>());

        // there will be candidates.size()-1 rounds or less (until we hit majority)
        while (true){
            candidates = countVotes(ballots, candidates);
            if (majorityVotesCheck(candidates, ballots) != null ){
                break;
            }

            // for ( int i = 0 ; i < candidates.size() ; i++ ){
            //     System.out.print(candidates.get(i).getNumVotes() + " ");
            // }
            updateVotesForEachRound(votesForEachRound, candidates);
            ballots = updateBallots(candidates, ballots);
            // System.out.println("Round number " + count);

            ballots = updateBallots(candidates, ballots);
            candidates = refreshCountOfVotes(candidates);
        }

        String content = auditContent(ballots, candidates, majorityVotesCheck(candidates,ballots));
        writeToAudit(content);


        IRVtable(votesForEachRound, candidates);

        return majorityVotesCheck(candidates, ballots);
    }
}
}
