import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CandidateTest {

    // Test-Candidate-1
    @Test
    public void testcreateCandidate() {
        String name = "C1";
        Candidate candidate = new Candidate(name);

        assertNotNull(candidate);
    }

    // Test-Candidate-2
    @Test
    public void testgetUsed() {
        String name = "C1";
        Candidate candidate = new Candidate(name);

        assertFalse(candidate.getUsed());
    }

    // Test-Candidate-3
    @Test
    public void testsetUsed() {
        String name = "C1";
        Candidate candidate = new Candidate(name);

        candidate.setUsed(true);

        assertTrue(candidate.getUsed());
    }

    // Test-Candidate-4
    @Test
    public void testgetNumVotes() {
        String name = "C1";
        Candidate candidate = new Candidate(name);

        assertEquals(0, candidate.getNumVotes());
    }

    // Test-Candidate-5
    @Test
    public void testsetNumVotes() {
        String name = "C1";
        Candidate candidate = new Candidate(name);

        candidate.setNumVotes(1);

        assertEquals(1, candidate.getNumVotes());
    }

    // Test-Candidate-6
    @Test
    public void testincreaseNumVotes() {
        String name = "C1";
        Candidate candidate = new Candidate(name);
        candidate.incrementNumVotes();

        assertEquals(1, candidate.getNumVotes());
    }

    // Test-Candidate-7
    @Test
    public void testdecreaseNumVotes() {
        String name = "C1";
        Candidate candidate = new Candidate(name);
        candidate.setNumVotes(3);
        candidate.decrementNumVotes();

        assertEquals(2, candidate.getNumVotes());
    }
    
}
