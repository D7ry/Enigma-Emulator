package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/**
 * Class that represents a complete enigma machine.
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 < PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        inputChecker(alpha, numRotors, pawls, allRotors);
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = allRotors;
        _slots = new Rotor[numRotors];
        _plugBoard = new Permutation("", alpha);
    }

    /**
     * Checks if the input to the machine constructor is valid.
     * throws error iff:
     * the machine has less than one pawl
     * the machine's rotor is not more than its pawls
     * alphabet of rotors doen's match actual alphabet.
     * @param alpha the alphabet.
     * @param numRotors the number of rotors.
     * @param pawls the number of pawls.
     * @param allRotors collection of all rotors.
     */
    void inputChecker(Alphabet alpha, int numRotors,
                      int pawls, Collection<Rotor> allRotors) {
        if (pawls <= 0) {
            throw error("Error: the machine has to have more than one pawl");
        }
        if (numRotors <= pawls) {
            throw error("Error: the machine "
                    + "has to have more rotors than pawls");
        }
    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls I have.
     */
    int numPawls() {
        return _numPawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        insertRotorChecker(rotors);
        for (int i = 0; i < _slots.length; i++) {
            _slots[i] = rotorGrabber(rotors[i]);
        }
    }

    /**
     * Grab the rotor with corresponding name
     * from the collection of all available rotors.
     * Throw error iff no matching rotor is found.
     * @param rotorName name of the rotor.
     * @return
     */
    Rotor rotorGrabber(String rotorName) {
        for (Rotor r : _allRotors) {
            if (r.name().equals(rotorName)) {
                return r;
            }
        }
        throw error("Error: failed to grab rotor, "
                + "matching Rotor name not found");
    }

    /**
     * check if the array of rotor
     * can be inserted to the current rotor slots.
     * Throw error iff:
     * total num of rotor doesn't match num of available slots
     * the leftmost rotor is not reflector
     * the rightmost rotor is not a moving rotor
     * reflector exists somewhere that's not the leftmost position
     * a fixed rotor is at a moving rotor's right side
     * @param rotors an array of rotors.
     */
    void insertRotorChecker(String[] rotors) {
        if (rotors.length != _numRotors) {
            throw error("Error: #num of rotors does not match #num of slots");
        }
        if (!rotorGrabber(rotors[0]).reflecting()) {
            throw error("Error: the leftmost rotor must be a reflector");
        }
        if (!rotorGrabber(rotors[rotors.length - 1]).rotates()) {
            throw error("Error: the rightmost rotor must be a moving rotor");
        }
        boolean movingRotorPrecedes = false;
        Collection<Rotor> containedRotors = new ArrayList<>();
        for (int i = 1; i < rotors.length; i++) {
            Rotor tempRotor = rotorGrabber(rotors[i]);
            if (containedRotors.contains(tempRotor)) {
                throw error("Error: duplicate rotors to be inserted");
            }
            containedRotors.add(tempRotor);
            if (tempRotor.reflecting()) {
                throw error("Error: wrong placement of reflector");
            }
            if (tempRotor.rotates()) {
                movingRotorPrecedes = true;
            } else {
                if (movingRotorPrecedes) {
                    throw error("Error: all fixed rotor "
                            + "must precede moving rotor");
                }
            }
        }
    }

    /**
     * Set my rotors according to SETTING,
     * which must be a string of
     * numRotors()-1 characters in my alphabet.
     * The first letter refers
     * to the leftmost rotor setting
     * (not counting the reflector).
     */
    void setRotors(String setting) {
        if (setting.length() != (_numRotors - 1)) {
            throw error("Error: Rotor setting length mismatch");
        }
        for (int i = 1; i < _numRotors; i++) {
            _slots[i].set(setting.charAt(i - 1));
        }
    }

    /**
     * set my rotors' ringSetting according to
     * SETTING.
     */
    void ringSetRotors(String setting) {
        if (setting.length() != (_numRotors - 1)) {
            throw error("Error: ringSetting length mismatch");
        }
        for (int i = 1; i < _numRotors; i++) {
            _slots[i].setRingSet(setting.charAt(i - 1));
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        if (plugboard.alphabet().alphabetArray() != _alphabet.alphabetArray()) {
            throw error("Error: plugboard alphabet mismatch");
        }
        _plugBoard = plugboard;
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size -1), after first advancing
     * the machine.
     */
    int convert(int c) {
        int i = _slots.length - 1;
        rotate();
        c = _plugBoard.permute(c);
        while (i >= 0) {
            c = _slots[i].convertForward(c);
            i -= 1;
        }
        i = 1;
        while (i < _slots.length) {
            c = _slots[i].convertBackward(c);
            i += 1;
        }
        c = _plugBoard.invert(c);
        return c;
    }

    /**
     * perform a single movement of all pawls
     * that advances the fast rotor and
     * conditionally advance the rest rotors,
     * changing their settings if applicable.
     */
    void rotate() {
        int index = _slots.length - 1;
        boolean shouldAdvThis = true;
        boolean shouldDbstepPrev = false;
        boolean shouldAdvNext = false;
        while (index > 0) {
            if (!_slots[index].rotates()) {
                break;
            }
            if (_slots[index].atNotch()) {
                shouldAdvNext = true;
            }
            if (shouldAdvThis) {
                _slots[index].advance();
                if (shouldDbstepPrev) {
                    _slots[index + 1].advance();
                }
                shouldDbstepPrev = false;
            } else {
                shouldDbstepPrev = true;
            }
            index -= 1;
            shouldAdvThis = shouldAdvNext;
            shouldAdvNext = false;
        }
    }


    /**
     * Returns the encoding/decoding of MSG,
     * updating the state of the rotors accordingly.
     */
    String convert(String msg) {
        String result = new String();
        for (char c : msg.toCharArray()) {
            result += _alphabet.toChar(convert(_alphabet.toInt(c)));
        }
        return result;
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;

    /**
     * number of rotor slots the machine has.
     */
    private int _numRotors;

    /**
     * number of pawls the machine has.
     */
    private int _numPawls;

    /**
     * a collection of all rotors.
     */
    private Collection<Rotor> _allRotors;

    /**
     * a collection of slots indexing from the left,
     * each slot can contain one rotor in the _allRotors.
     */
    private Rotor[] _slots;

    /**
     * the plugboard of the Machine,
     * as a permutation placed at the beginning of the machine
     * default plugboard has each character in the alphabet
     * mapping to itself.
     */
    private Permutation _plugBoard;
}
