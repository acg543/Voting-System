import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;
import org.junit.Test;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

public class IRTest {

    // test-IR-1
    @Test
    public void testCalculateResults() {
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
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        assertNotNull(obj.getAuditFileName());

        assertEquals(auditFileName, obj.getAuditFileName());

    }

    // test-IR-4
    @Test
    public void testGetVotingSystem() {
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        assertNotNull(obj.getVotingSystem());

        assertEquals(votingSystem, obj.getVotingSystem());

    }

    // test-IR-5
    @Test
    public void testHasLeastVotes() {
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
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        obj.setAuditFileName("newAudit.csv");

        assertEquals("newAudit.csv", obj.getAuditFileName());
    }

    // test-IR-11
    @Test
    public void testSetVotingSystem() {
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

    @Test
    public void testIRVtable() {
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        Vector<Candidate> candidates = new Vector<>();
        ArrayList<Vector<Integer>> votesForEachRound = new ArrayList<>();
        Vector<Integer> v1 = new Vector<>();
        v1.add(1);
        v1.add(2);
        v1.add(3);
        votesForEachRound.add(v1);
        Vector<Integer> v2 = new Vector<>();
        v2.add(2);
        v2.add(3);
        v2.add(1);
        votesForEachRound.add(v2);
        Vector<Integer> v3 = new Vector<>();
        v3.add(3);
        v3.add(1);
        v3.add(2);
        votesForEachRound.add(v3);

        Candidate c1 = new Candidate("A");
        Candidate c2 = new Candidate("B");
        Candidate c3 = new Candidate("C");
        candidates.add(c1);
        candidates.add(c2);
        candidates.add(c3);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        obj.IRVtable(votesForEachRound, candidates);

        String expectedOutput = 
                "| Candidates     | 1st Round       |                 | 2nd Round       |                 | 3rd Round       |                 |\n" +
                "| Candidate      | Votes           | -+              | Votes           | -+              | Votes           | -+              |\n" +
                "| A              | 1               | +1              | 2               | +1              | 3               | +1              |\n" +
                "| B              | 2               | +1              | 3               | +1              | 1               | -2              |\n" +
                "| C              | 3               | +1              | 1               | -2              | 2               | +1              |\n" +
                "| TOTALS         | 6               | +3              | 6               | +3              | 6               | +3              |\n";

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testUpdateVotesForEachRound() {
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        Vector<Candidate> candidates = new Vector<>();

        ArrayList<Vector<Integer>> votesForEachRound = new ArrayList<>();
        Vector<Integer> round1Votes = new Vector<>();
        round1Votes.add(5);
        round1Votes.add(7);
        round1Votes.add(3);
        votesForEachRound.add(round1Votes);

        candidates.add(new Candidate("Alice"));
        candidates.add(new Candidate("Bob"));
        candidates.add(new Candidate("Charlie"));

        candidates.get(0).setNumVotes(2);
        candidates.get(1).setNumVotes(4);
        candidates.get(2).setNumVotes(6);

        obj.updateVotesForEachRound(votesForEachRound, candidates);

        assertEquals(2, (int) votesForEachRound.get(0).get(3));
    }

    @Test
    public void testUpdateVotesForEachRoundEmptyInput() {
        VotingSystem votingSystem = new VotingSystem();
        String auditFileName = "audit.csv";

        IR obj = new IR(auditFileName, votingSystem);

        Vector<Candidate> candidates = new Vector<>();
        ArrayList<Vector<Integer>> votesForEachRound = new ArrayList<>();

        obj.updateVotesForEachRound(votesForEachRound, candidates);

        assertTrue(votesForEachRound.isEmpty());
    }

}
