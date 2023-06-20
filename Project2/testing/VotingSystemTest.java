import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

public class VotingSystemTest {

    // helper method to create a fake ballots
    public Vector<Ballot> tempBallots() {
        Vector<Ballot> ballots = new Vector<Ballot>();
        Ballot newBallot1 = new Ballot("1,0,0,0,0",5);
        Ballot newBallot2 = new Ballot("0,1,0,0,0",5);
        Ballot newBallot3 = new Ballot("0,0,1,0,0",5);
        Ballot newBallot4 = new Ballot("0,0,0,1,0",5);
        Ballot newBallot5 = new Ballot("1,0,0,0,0",5);
        Ballot newBallot6 = new Ballot("1,0,0,0,0",5);
        ballots.add(newBallot1);
        ballots.add(newBallot2);
        ballots.add(newBallot3);
        ballots.add(newBallot4);
        ballots.add(newBallot5);
        ballots.add(newBallot6);
        return ballots;
    }

    // helper method to create candidates with no votes
    public Vector<Candidate> tempCandidates() {
        Vector<Candidate> candidates = new Vector<Candidate>();
        Candidate newCandidate1 = new Candidate("C1");
        Candidate newCandidate2 = new Candidate("C2");
        Candidate newCandidate3 = new Candidate("C3");
        Candidate newCandidate4 = new Candidate("C4");
        Candidate newCandidate5 = new Candidate("C5");
        candidates.add(newCandidate1);
        candidates.add(newCandidate2);
        candidates.add(newCandidate3);
        candidates.add(newCandidate4);
        candidates.add(newCandidate5);
        return candidates;
    }

    // Test-VotingSystem-1
    @Test
    public void testgetNumBallots() {
        VotingSystem votingSystem = new VotingSystem();
        assertEquals(0,votingSystem.getNumBallots());
    }

    // Test-VotingSystem-2
    @Test
    public void testsetNumBallots() {
        VotingSystem votingSystem = new VotingSystem();
        votingSystem.setNumBallots(2);

        assertEquals(2, votingSystem.getNumBallots());
    }

    // Test-VotingSystem-3
    @Test
    public void testgetBallots() {
        VotingSystem votingSystem = new VotingSystem();
        assertEquals(new Vector<>(0, 0),votingSystem.getBallots());
    }

    // Test-VotingSystem-4
    @Test
    public void testsetBallots() {
        Vector<Ballot> ballots = tempBallots();

        VotingSystem votingSystem = new VotingSystem();
        votingSystem.setBallots(ballots);

        assertNotNull(votingSystem.getBallots());
        assertEquals(6,votingSystem.getBallots().size());
    }

    // Test-VotingSystem-5
    @Test
    public void testgetNumCandidates() {
        VotingSystem votingSystem = new VotingSystem();
        assertEquals(0,votingSystem.getNumCandidates());
    }

    // Test-VotingSystem-6
    @Test
    public void testsetNumCandidates() {
        VotingSystem votingSystem = new VotingSystem();
        votingSystem.setNumCandidates(2);

        assertEquals(2, votingSystem.getNumCandidates());
    }

    // Test-VotingSystem-7
    @Test
    public void testgetCandidates() {
        VotingSystem votingSystem = new VotingSystem();
        assertNull(votingSystem.getCandidates());
    }

    // Test-VotingSystem-8
    @Test
    public void testsetCandidates() {
        Vector<Candidate> candidates = tempCandidates();

        VotingSystem votingSystem = new VotingSystem();
        votingSystem.setCandidates(candidates);

        assertNotNull(votingSystem.getCandidates());
        assertEquals(5,votingSystem.getCandidates().size());
    }

    // Test-VotingSystem-9
    @Test
    public void testgetNumSeats() {
        VotingSystem votingSystem = new VotingSystem();
        assertEquals(0,votingSystem.getNumSeats());
    }

    // Test-VotingSystem-10
    @Test
    public void testsetNumSeats() {
        VotingSystem votingSystem = new VotingSystem();
        votingSystem.setNumSeats(2);

        assertEquals(2, votingSystem.getNumSeats());
    }

    // Test-VotingSystem-11
    @Test
    public void testgetType() {
        VotingSystem votingSystem = new VotingSystem();
        assertNull(votingSystem.getType());
    }

    // Test-VotingSystem-12
    @Test
    public void testsetType() {
        VotingSystem votingSystem = new VotingSystem();
        votingSystem.setType("CPL");

        assertEquals("CPL", votingSystem.getType());
    }

    // Test-VotingSystem-13
    @Test
    public void testrunCalculator() {
        Vector<Candidate> candidates = tempCandidates();
        Vector<Ballot> ballots = tempBallots();
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";
        int seats = 5;
    
        CPL obj = new CPL(auditFileName, votingSystem, seats);
        votingSystem.setBallots(ballots);
        votingSystem.setCandidates(candidates);

        votingSystem.setCalculator(obj);
        String winner = votingSystem.runCalculator();

        assertEquals("C1", winner);
            
    }
    
}
