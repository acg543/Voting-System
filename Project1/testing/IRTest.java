import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

public class IRTest {

    // test-IR-1
    @Test
    public void testCalculateResults() {
        // test-IR-1
        Vector<Candidate> candidates = new Vector<Candidate>();
        Vector<Ballot> ballots = new Vector<Ballot>();

        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();

        IR obj = new IR(auditFileName, votingSystem);

        candidates.add(new Candidate("Alice"));
        candidates.add(new Candidate("Bob"));

        ballots.add(new Ballot("1,2", 2));
        ballots.add(new Ballot("1,", 2));

        assertEquals(obj.calculateResults(ballots,candidates),"Alice");
    }

    // test-IR-2
    @Test
    public void testCountVotes() {
        // test-IR-2
        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();

        IR obj = new IR(auditFileName, votingSystem);

        Vector<Ballot> ballots = new Vector<>();
        Vector<Candidate> candidates = new Vector<>();
        Vector<Candidate> result = obj.countVotes(ballots, candidates);
        assert(result.size() == 0);

        ballots = new Vector<>();
        ballots.add(new Ballot("1,2,3",3));
        ballots.add(new Ballot("2,1,3",3));
        ballots.add(new Ballot("3,1,2",3));
        candidates = new Vector<>();
        candidates.add(new Candidate("John"));
        candidates.add(new Candidate("Alice"));
        candidates.add(new Candidate("Bob"));
        result = obj.countVotes(ballots, candidates);
        assert(result.size() == 3);
        assert(result.get(0).getName().equals("John"));
        assert(result.get(0).getNumVotes() == 1);
        assert(result.get(1).getName().equals("Alice"));
        assert(result.get(1).getNumVotes() == 2);
        assert(result.get(2).getName().equals("Bob"));
        assert(result.get(2).getNumVotes() == 0);

    }

    // test-IR-3
    @Test
    public void testGetAuditFileName() {
        // test-IR-3
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        assertNotNull(obj.getAuditFileName());

        assertEquals(auditFileName, obj.getAuditFileName());

    }

    // test-IR-4
    @Test
    public void testGetVotingSystem() {
        // test-IR-4
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        assertNotNull(obj.getVotingSystem());

        assertEquals(votingSystem, obj.getVotingSystem());

    }

    // test-IR-5
    @Test
    public void testHasLeastVotes() {
        // test-IR-5
        Vector<Candidate> candidates = new Vector<Candidate>();

        candidates.add(new Candidate("Alice"));
        candidates.add(new Candidate("Bob"));

        candidates.get(0).setNumVotes(2);
        candidates.get(1).setNumVotes(3);

        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();

        IR obj = new IR(auditFileName,votingSystem);

        Vector<Candidate> leastCandidates = new Vector<Candidate>();
        leastCandidates.add(candidates.get(0));

        assertEquals(obj.hasLeastVotes(candidates),leastCandidates);

    }

    // test-IR-6
    @Test
    public void testInitializeBallots() {
        // test-IR-6
        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();

        IR obj = new IR(auditFileName,votingSystem);

        Vector<Ballot> ballots = new Vector<Ballot>();

        obj.initializeBallots(ballots, 3);

        assertTrue(ballots.isEmpty());

        ballots.add(new Ballot());
        ballots.add(new Ballot());
        ballots.add(new Ballot());

        obj.initializeBallots(ballots, 3);

        for ( Ballot ballot : ballots){
            assertEquals(3, ballot.getRank());
        }
    }

    // test-IR-7
    @Test
    public void testMajorityVotesCheck() {
        // test-IR-7
        Vector<Candidate> candidates = new Vector<Candidate>();
        Vector<Ballot> ballots = new Vector<Ballot>();

        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();

        IR obj = new IR(auditFileName, votingSystem);

        String winner = obj.majorityVotesCheck(candidates, ballots);

        // no candidates 
        assertNull(winner);

        // clear winner 
        candidates.add(new Candidate("Alice"));
        candidates.add(new Candidate("Bob"));

        ballots.add(new Ballot("1,2", 2));
        ballots.add(new Ballot("1,", 2));

        ballots = obj.initializeBallots(ballots,1);
        candidates = obj.countVotes(ballots,candidates);

        winner = obj.majorityVotesCheck(candidates,ballots);

        assertEquals("Alice", winner);
    }

    // test-IR-8
    @Test
    public void testNextRankExists() {

        // test-IR-8
        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();

        IR obj = new IR(auditFileName, votingSystem);

        int[] votes = {1,3,2,5,4};

        boolean result = obj.nextRankExists(votes, 3);

        assertTrue(result);

        result = obj.nextRankExists(votes,5);

        assertFalse(result);
    }

    // test-IR-9
    @Test
    public void testResolveTie() {

        // test-IR-9
        String auditFileName = "audit.csv";
        VotingSystem votingSystem = new VotingSystem();

        IR obj = new IR(auditFileName, votingSystem);

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

    // test-IR-10
    @Test
    public void testSetAuditFileName() {

        // test-IR-10
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        obj.setAuditFileName("newAudit.csv");

        assertEquals("newAudit.csv", obj.getAuditFileName());
    }

    // test-IR-11
    @Test
    public void testSetVotingSystem() {

        // test-IR-11
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        VotingSystem newVotingSystem = new VotingSystem();
        obj.setVotingSystem(newVotingSystem);


        assertEquals(newVotingSystem, obj.getVotingSystem());
    }

    // test-IR-12
    @Test
    public void testUpdateBallots() {

        // test-IR-12
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        Vector<Ballot> ballots = new Vector<Ballot>();
        ballots.add(new Ballot("1,2,3",3));
        Vector<Candidate> candidates = new Vector<Candidate>();
        candidates.add(new Candidate("A"));
        candidates.add(new Candidate("B"));
        candidates.add(new Candidate("C"));
        candidates.get(0).setNumVotes(1);
        candidates.get(1).setNumVotes(1);
        candidates.get(2).setNumVotes(1);
        Vector<Ballot> updatedBallots = obj.updateBallots(candidates, ballots);
        assertEquals(ballots, updatedBallots);
    }

    //// test-IR-13
    @Test
    public void testWriteToAudit() throws IOException {

        // test-IR-13
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);
        
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

    // test-IR-14
    @Test
    public void testRefreshCountOfVotes(){

        // test-IR-14
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        Vector<Candidate> candidates = new Vector<>();
        Vector<Candidate> result = obj.refreshCountOfVotes(candidates);
        assert(result.size() == 0);

        candidates.add(new Candidate("John"));
        result = obj.refreshCountOfVotes(candidates);

        assert(result.size() == 1);
        assert(result.get(0).getName().equals("John"));
        assert(result.get(0).getNumVotes() == 0);

        candidates = new Vector<>();
        candidates.add(new Candidate("John"));
        candidates.add(new Candidate("Alice"));
        candidates.add(new Candidate("Bob"));
            result = obj.refreshCountOfVotes(candidates);
        assert(result.size() == 3);
        for (Candidate c : result) {
            assert(c.getNumVotes() == 0);
        }

    }
}
