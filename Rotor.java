package enigma;

import static enigma.EnigmaException.*;

/**
 * Superclass that represents a rotor in the enigma machine.
 *
 */
class Rotor {

    /**
     * A rotor named NAME whose permutation is given by PERM.
     */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _ringSetting = 0;
    }

    /**
     * Return my name.
     */
    String name() {
        return _name;
    }

    /**
     * Return my alphabet.
     */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /**
     * Return my permutation.
     */
    Permutation permutation() {
        return _permutation;
    }

    /**
     * Return the size of my alphabet.
     */
    int size() {
        return _permutation.size();
    }

    /**
     * Return true iff I have a ratchet and can move.
     */
    boolean rotates() {
        return false;
    }

    /**
     * Return true iff I reflect.
     */
    boolean reflecting() {
        return false;
    }

    /**
     * Return my current setting.
     */
    int setting() {
        return _setting;
    }

    /** Return my current ringSetting.
     */
    int ringSetting() {
        return _ringSetting;
    }

    /**
     * Set setting() to POSN. The position may be out of bound,
     * so an addition checker is implemented
     */
    void set(int posn) {
        if (posn > _permutation.size()) {
            throw error("Error: position out of bound");
        }
        set(alphabet().toChar(posn));
    }

    /**Set setting() to character CPOSN.
     * throws error if the alphabet doens't contain the position
     */
    void set(char cposn) {
        if (!alphabet().contains(cposn)) {
            throw error("Error: position not found in alphabet");
        }
        _setting = alphabet().toInt(cposn);
    }

    /** set ring setting to character CPOSN.
     */
    void setRingSet(char cposn) {
        _ringSetting = alphabet().toInt(cposn);
    }



    /**
     * Return the conversion of P (an integer in the range 0..size()-1)
     * according to my permutation.
     */
    int convertForward(int p) {
        return _permutation.wrap(
                _permutation.permute(_setting + p - _ringSetting)
                        - (_setting - _ringSetting));
    }

    /**
     * Return the conversion of E (an integer in the range 0..size()-1)
     * according to the inverse of my permutation.
     */
    int convertBackward(int e) {
        return _permutation.wrap(
                _permutation.invert(_setting + e - _ringSetting)
                        - (_setting - _ringSetting));
    }

    /**
     * Returns true iff I am positioned to allow the rotor to my left
     * to advance.
     */
    boolean atNotch() {
        return false;
    }

    /**
     * Advance me one position, if possible. By default, does nothing.
     */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /**
     * My name.
     */
    private final String _name;

    /**
     * The permutation implemented by this rotor in its 0 position.
     */
    private Permutation _permutation;

    /**
     * the setting of the rotor, represented in int.
     */
    private int _setting;

    /**
     * the ring setting of the rotor, representing my
     * extra credit.
     */
    private int _ringSetting;
}
