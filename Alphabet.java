package enigma;

import static enigma.EnigmaException.*;

/**
 * An alphabet of encodable characters.  Provides a mapping from characters
 * to and from indices into the alphabet.
 */
class Alphabet {

    /**
     * A new alphabet containing CHARS. The K-th character has index
     * K (numbering from 0). No character may be duplicated.
     */
    Alphabet(String chars) {
        chars = chars.replaceAll("\\s+", "");
        for (int i = 0; i < chars.length(); i++) {
            for (int k = i + 1; k < chars.length(); k++) {
                if (chars.charAt(i) == chars.charAt(k)) {
                    throw error("Error: duplicate characters in alphabet");
                }
            }
        }
        _alphabetString = chars;
        _alphabetArray = chars.toCharArray();
    }

    /**
     * A default alphabet of all upper-case characters.
     */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * returns the alphabet array of this alphabet.
     */
    char[] alphabetArray() {
        return _alphabetArray;
    }

    /**
     * Returns the size of the alphabet.
     */
    int size() {
        return _alphabetArray.length;
    }

    /**
     * Returns true if CH is in this alphabet.
     */
    boolean contains(char ch) {
        for (char c : _alphabetArray) {
            if (ch == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns character number INDEX in the alphabet, where
     * 0 <= INDEX < size().
     */
    char toChar(int index) {
        if (index >= size()) {
            throw error("Error: index out of bound"
            + String.format("index is %d", index)
            );
        }
        return _alphabetArray[index];
    }

    /**
     * Returns the index of character CH which must be in
     * the alphabet. This is the inverse of toChar().
     */
    int toInt(char ch) {
        for (int i = 0; i < size(); i++) {
            if (_alphabetArray[i] == ch) {
                return i;
            }
        }
        throw error(
                String.format(
                        "Error: ch not in the alphabet: %s", _alphabetString)
                        + String.format("the character is: %s", ch)
                );
    }

    /**
     * alphabet represented as an array.
     */
    private char[] _alphabetArray;
    /**
     * alphabet represented as a string.
     */
    private String _alphabetString;
}
