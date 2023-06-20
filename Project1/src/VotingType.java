import java.util.*;

/**
 * VotingType.java
 * 
 * <p>This file implements the voting type interface for each system.</p>
 * 
 * @author Jackie Li, Dominic Deiman, Aahan Tyagi, Alex Grenier
 */

public interface VotingType{
    public void writeToAudit(String contents);
    public String calculateResults(Vector<Ballot> ballots, Vector<Candidate> candidates);
}