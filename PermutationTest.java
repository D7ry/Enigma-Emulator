package enigma;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class.
 *
 */
public class PermutationTest {

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;


    /**
     * Check that perm has an alphabet whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */
    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation q = new Permutation("(BACD)", new Alphabet("ABCDE"));
        assertEquals('B', p.invert('A'));
        assertEquals('C', p.invert('D'));
        assertEquals('D', p.invert('B'));
        assertEquals('E', q.invert('E'));
    }

    @Test
    public void testpermutechar() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation q = new Permutation("(BACD)", new Alphabet("ABCDE"));
        Permutation s = new Permutation("(BAcd)    (EFG)",
                new Alphabet("ABCDEFG"));
        assertEquals("A", String.valueOf(p.permute('B')));
        assertEquals("D", String.valueOf(p.permute('C')));
        assertEquals("B", String.valueOf(p.permute('D')));
        assertEquals("E", String.valueOf(q.permute('E')));
        assertEquals("E", String.valueOf(s.permute('G')));
    }


    @Test
    public void testPermuteInt() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation q = new Permutation("(BACD)", new Alphabet("ABCDE"));
        assertEquals(2, p.permute(4));
        assertEquals(0, p.permute(1));
        assertEquals(0, p.permute(5));
        assertEquals(4, q.permute(4));
    }

    @Test
    public void testInvertInt() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation q = new Permutation("(BACD)", new Alphabet("ABCDE"));
        assertEquals(1, p.invert(0));
        assertEquals(4, q.invert(4));
        assertEquals(0, p.invert(2));
    }

    @Test
    public void testSize() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation q = new Permutation("(BACD)", new Alphabet("ABCDE"));
        assertEquals(4, p.size());
        assertEquals(5, q.size());
    }

    @Test
    public void testDerangement() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation q = new Permutation("(BACD)", new Alphabet("ABCDE"));
        Permutation r = new Permutation("(AD)(CG)", new Alphabet("ADCG"));
        Permutation s = new Permutation("(AD)(CG)", new Alphabet("ADCGE"));
        assertTrue(p.derangement());
        assertFalse(q.derangement());
        assertTrue(r.derangement());
        assertFalse(s.derangement());
    }

    @Test(expected = EnigmaException.class)
    public void testInvertNotInAlphabet() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        p.invert('F');
    }

    @Test(expected = EnigmaException.class)
    public void testPemuteNotInAlphabet() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        p.permute('F');
    }

    @Test(expected = EnigmaException.class)
    public void testillegalCycles1() {
        Permutation p = new Permutation("(BACCD)", new Alphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testillegalCycles2() {
        Permutation p = new Permutation("(ABCDEFG)", new Alphabet("A"));
    }


}
