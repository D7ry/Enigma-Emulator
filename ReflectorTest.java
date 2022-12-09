package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;


import static enigma.TestUtils.*;


public class ReflectorTest {
    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;


    /* ***** TESTS ***** */
    @Test(expected = EnigmaException.class)
    public void testWrongPerm() {
        Reflector p = new Reflector(
                "HAHA", new Permutation(
                        "(BAD)", new Alphabet("ABCD")));
    }

    @Test(expected = EnigmaException.class)
    public void testWrongPerm2() {
        Reflector p = new Reflector(
                "HAHA", new Permutation(
                        "(BCD)", new Alphabet("ABCD")));
    }

    @Test(expected = EnigmaException.class)
    public void testWrongPerm3() {
        Reflector p = new Reflector(
                "HAHA", new Permutation(
                        "()", new Alphabet("ABCD")));
    }

    @Test
    public void testRightPerm() {
        Reflector p = new Reflector(
                "JAJA", new Permutation(
                        "(AB)(CD)", new Alphabet("ABCD")));
    }

}
