import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;
import org.junit.Test;
import static org.junit.Assert.*;


public class CPLTest {

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

    // helper method to create candidates with votes
    public Vector<Candidate> tempCandidatesWithVotesNoTie() {
        Vector<Candidate> candidates = new Vector<Candidate>();
        Candidate newCandidate1 = new Candidate("C1");
        newCandidate1.incrementNumVotes();
        newCandidate1.incrementNumVotes();
        newCandidate1.incrementNumVotes();

        Candidate newCandidate2 = new Candidate("C2");
        newCandidate2.incrementNumVotes();

        Candidate newCandidate3 = new Candidate("C3");
        newCandidate3.incrementNumVotes();

        Candidate newCandidate4 = new Candidate("C4");
        newCandidate4.incrementNumVotes();

        Candidate newCandidate5 = new Candidate("C5");
        candidates.add(newCandidate1);
        candidates.add(newCandidate2);
        candidates.add(newCandidate3);
        candidates.add(newCandidate4);
        candidates.add(newCandidate5);
        return candidates;
    }

    // helper method to create candidates with votes and tie
    public Vector<Candidate> tempCandidatesWithVotesWithTie() {
        Vector<Candidate> candidates = new Vector<Candidate>();
        Candidate newCandidate1 = new Candidate("C1");
        newCandidate1.incrementNumVotes();
        newCandidate1.incrementNumVotes();
        newCandidate1.incrementNumVotes();

        Candidate newCandidate2 = new Candidate("C2");
        newCandidate2.incrementNumVotes();
        newCandidate2.incrementNumVotes();
        newCandidate2.incrementNumVotes();

        Candidate newCandidate3 = new Candidate("C3");
        newCandidate3.incrementNumVotes();

        Candidate newCandidate4 = new Candidate("C4");
        newCandidate4.incrementNumVotes();

        Candidate newCandidate5 = new Candidate("C5");
        candidates.add(newCandidate1);
        candidates.add(newCandidate2);
        candidates.add(newCandidate3);
        candidates.add(newCandidate4);
        candidates.add(newCandidate5);
        return candidates;
    }

    // Test-CPL-1
    @Test
    public void testGetAuditFileName() {
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";
        int seats = 2;

        CPL obj = new CPL(auditFileName, votingSystem, seats);

        assertNotNull(obj.getAuditFileName());

        assertEquals(auditFileName, obj.getAuditFileName());

    }

    // Test-CPL-2
    @Test
    public void testSetAuditFileName() {
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";
        int seats = 2;

        String newName = "new.csv";

        CPL obj = new CPL(auditFileName, votingSystem, seats);

        obj.setAuditFileName(newName);

        assertNotNull(obj.getAuditFileName());

        assertEquals(newName, obj.getAuditFileName());

    }

    // Test-CPL-3
    @Test
    public void testGetVotingSystem() {
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";
        int seats = 2;
    
        CPL obj = new CPL(auditFileName, votingSystem, seats);

        assertNotNull(obj.getVotingSystem());

        assertEquals(votingSystem, obj.getVotingSystem());

    }

    // Test-CPL-4
    @Test
    public void testGetTieBreaker() {
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";
        int seats = 2;
    
        CPL obj = new CPL(auditFileName, votingSystem, seats);

        assertNotNull(obj.getTieBreaker());
        assertFalse(obj.getTieBreaker());

    }

    // Test-CPL-5
    @Test
    public void testSetTieBreaker() {
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";
        int seats = 2;
    
        CPL obj = new CPL(auditFileName, votingSystem, seats);

        obj.setTieBreaker(true);

        assertNotNull(obj.getTieBreaker());
        assertTrue(obj.getTieBreaker());

    }

    // Test-CPL-6
    @Test 
    public void testcountVotesForCandidateVotes() {
        Vector<Ballot> ballots = tempBallots();
        Vector<Candidate> candidates = tempCandidates();
        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();
        int seats = 5;

        CPL obj = new CPL(auditFileName, votingSystem, seats);

        obj.countVotes(ballots, candidates);

        assertEquals(3, candidates.get(0).getNumVotes());
        assertEquals(1, candidates.get(1).getNumVotes());
        assertEquals(0, candidates.lastElement().getNumVotes());
    }

    // Test-CPL-7
    @Test 
    public void testmostVotesWithNoTie() {
        Vector<Candidate> candidatesWithVotes = tempCandidatesWithVotesNoTie();
        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();
        int seats = 5;

        CPL obj = new CPL(auditFileName, votingSystem, seats);

        Vector<Candidate> winners = obj.mostVotes(candidatesWithVotes);

        assertNotNull(winners);
        assertEquals(1, winners.size());
    }

    // Test-CPL-8
    @Test 
    public void testmostVotesWithTie() {
        Vector<Candidate> candidatesWithVotes = tempCandidatesWithVotesWithTie();
        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();
        int seats = 5;

        CPL obj = new CPL(auditFileName, votingSystem, seats);

        Vector<Candidate> winners = obj.mostVotes(candidatesWithVotes);

        assertNotNull(winners);
        assertEquals(2, winners.size());
    }

    // Test-CPL-9
    @Test 
    public void testfindWinner() {
        Vector<Candidate> candidatesWithVotes = new Vector<Candidate>();
        candidatesWithVotes.add(new Candidate("C1"));

        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();
        int seats = 5;

        CPL obj = new CPL(auditFileName, votingSystem, seats);

        String winner = obj.findWinner(candidatesWithVotes);

        assertNotNull(winner);
        assertEquals("C1", winner);
    }

    // Test-CPL-10
    @Test 
    public void testtieBreaker() {
        Vector<Candidate> candidatesWithVotes = new Vector<Candidate>();
        candidatesWithVotes.add(new Candidate("C1"));

        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();
        int seats = 5;

        CPL obj = new CPL(auditFileName, votingSystem, seats);

        String winner = obj.findWinner(candidatesWithVotes);

        assertNotNull(winner);
        assertEquals("C1", winner);
    }

    // Test-CPL-11
    @Test
    public void testWriteToAudit() throws IOException {
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        CPL obj = new CPL(auditFileName, votingSystem,5);
        
        // Write a message to the audit log
        obj.writeToAudit("Test message");
        
        // Test that the message was written to the audit log file
        String expectedContents = "Test message";
        String actualContents = Files.readString(Paths.get(obj.getAuditFileName()));
        assertEquals(expectedContents, actualContents);
        
        // Clean up - delete the test audit file
        File file = new File(obj.getAuditFileName());
        file.delete();
    }

    // Test-CPL-12
    @Test
    public void testCalculateResults() {
        Vector<Candidate> candidates = tempCandidates();
        Vector<Ballot> ballots = tempBallots();

        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();

        CPL obj = new CPL(auditFileName, votingSystem,5);

        obj.calculateResults(ballots, candidates);

        assertEquals(obj.calculateResults(ballots,candidates),"C1");
    }

    // Test-CPL-13
    @Test
    public void testResolveTie() {

        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();

        CPL obj = new CPL(auditFileName, votingSystem,5);

        int result = obj.resolveTie(0);

        assert(result == -1);

        result = obj.resolveTie(1);

        assert(result == 0);

        // ensuring randomness 
        int[] counts = new int[10];
        for ( int i = 0; i < 1000000 ; i++ ){
            int r = obj.resolveTie(10);
            counts[r]++;
        }

        for ( int count : counts ){
            assert(count > 90000 && count < 110000);
        }
    }

}
