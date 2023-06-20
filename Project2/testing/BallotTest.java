import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BallotTest {

    // Test-Ballot-1
    @Test
    public void testgetVotes() {
        Ballot ballot = new Ballot();

        assertNull(ballot.getVotes());
    }

    // Test-Ballot-2
    @Test
    public void testsetVotes() {
        Ballot ballot = new Ballot();
        int[] votes = new int[2];
        ballot.setVotes(votes);

        assertNotNull(ballot.getVotes());
        assertEquals(2, ballot.getVotes().length);
    }

    // Test-Ballot-3
    @Test
    public void testgetRank() {
        Ballot ballot = new Ballot();

        assertEquals(0, ballot.getRank());
    }

    // Test-Ballot-4
    @Test
    public void testsetRank() {
        Ballot ballot = new Ballot();
        ballot.setRank(1);

        assertNotNull(ballot.getRank());
        assertEquals(1, ballot.getRank());
    }

    // Test-Ballot-5
    @Test
    public void testincreaseRank() {
        Ballot ballot = new Ballot();
        ballot.increaseRank();

        assertNotNull(ballot.getRank());
        assertEquals(1, ballot.getRank());
    }

    // Test-Ballot-6
    @Test
    public void testballotCreation() {
        String voteString = "1,0,0,0,0";
        int numCandidates = 5;

        Ballot ballot = new Ballot(voteString,numCandidates);

        assertNotNull(ballot);
        assertEquals(1, ballot.getVotes()[0]);
        assertEquals(0, ballot.getVotes()[1]);
    }
}
