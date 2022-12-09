package enigma;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.antlr.v4.runtime.misc.IntegerList;


import static enigma.EnigmaException.*;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cyclesString = cycles.replaceAll("\\s+", "");
        _cyclesArray = _cyclesString.split("\\)");
        inputChecker();
        if (_cyclesString.length() != 0) {
            for (int i = 0; i < _cyclesArray.length; i++) {
                parenChecker(_cyclesArray[i]);
                _cyclesArray[i] = _cyclesArray[i].replaceAll("\\(", "");
            }
        }
    }


    /**
     * checker for the left parentheses in a single cycle.
     * throws error if:
     * there are more than one left parentheses OR
     * the cycle starts without left parentheses OR
     *
     * @param cycle the single cycle being processed.
     */
    private void parenChecker(String cycle) {
        if (!cycle.startsWith("(")) {
            throw error("Error: missing parenthesis at beginning of the cycle"
                    + String.format("cycle string is %s", cycle));
        }
        int parencount = 0;
        for (int k = 0; k < cycle.length(); k++) {
            if (cycle.charAt(k) == '(') {
                parencount += 1;
            }
        }
        if (parencount > 1) {
            throw error("Error: left parentheses overload!");
        }
    }

    /**
     * check if the input CYCLES is in correct format. Throws error:
     * if there are duplicate characters in the cycle OR
     * if character in the cycle is not in the alphabet
     */
    private void inputChecker() {
        _contained = "";
        for (int i = 0; i < _cyclesString.length(); i++) {
            char c = _cyclesString.charAt(i);
            if (c == '(' || c == ')') {
                continue;
            } else if (_contained.contains(Character.toString(c))) {
                throw error("Error: duplicate characters");
            } else if (!_alphabet.contains(c)) {
                throw error("Error: letter in cycle not found in alphabet");
            } else {
                _contained += c;
            }
        }
    }

    /**
     * Return the value of P modulo the size of this permutation's alphabet.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet.
     */
    int size() {
        return _alphabet.size();
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        return _alphabet.toInt(
                permute(_alphabet.toChar(wrap(p)))
        );
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        return _alphabet.toInt(
                invert(_alphabet.toChar(wrap(c)))
        );
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        String pString = Character.toString(p);
        if (!_alphabet.contains(p)) {
            throw error("Errror: character not in alphabet");
        }
        if (!_cyclesString.contains(pString)) {
            return p;
        }
        for (String cycle : _cyclesArray) {
            if (cycle.contains(pString)) {
                int index = (cycle.indexOf(p) + 1) % cycle.length();
                if (index < 0) {
                    index += cycle.length();
                }
                return cycle.charAt(index);
            }
        }
        throw error("Error: this shouldn't occur");
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    char invert(char c) {
        String cString = Character.toString(c);
        if (!_alphabet.contains(c)) {
            throw error("character not in alphabet");
        }
        if (!_cyclesString.contains(cString)) {
            return c;
        }
        for (String cycle : _cyclesArray) {
            if (cycle.contains(cString)) {
                int index = (cycle.indexOf(c) - 1) % cycle.length();
                if (index < 0) {
                    index += cycle.length();
                }
                return cycle.charAt(index);
            }
        }
        throw error("Error: this shouldn't occur");
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        return _contained.length() == alphabet().size();
    }

    /**
     * Alphabet of this permutation.
     */
    private final Alphabet _alphabet;

    /**
     * cycles of this permutation represented in String[].
     */
    private String[] _cyclesArray;

    /**
     * cycles of this permutation represented in a string.
     */
    private String _cyclesString;

    /**
     * Characters contained in the cycles.
     */
    private String _contained;
}
