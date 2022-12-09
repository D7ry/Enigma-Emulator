package enigma;

import static enigma.EnigmaException.*;

/**
 * Class that represents a rotating rotor in the enigma machine.
 *
 */
class MovingRotor extends Rotor {

    /**
     * A rotor named NAME whose permutation in its default setting is
     * PERM, and whose notches are at the positions indicated in NOTCHES.
     * The Rotor is initally in its 0 setting (first character of its
     * alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }


    /**
     * Return true iff I have a ratchet and can move;
     * i.e. this rotor can rotate
     */
    @Override
    boolean rotates() {
        return true;
    }

    /**
     * Returns true iff I am positioned to allow the rotor to my left
     * to advance.
     */
    @Override
    boolean atNotch() {
        int currSetting = setting() + ringSetting();
        while (currSetting >= alphabet().size()) {
            currSetting -= alphabet().size();
        }
        while (currSetting < 0) {
            currSetting += alphabet().size();
        }
        return (_notches.contains(
                Character.toString(
                        alphabet().toChar(currSetting))));
    }

    /**
     * simply advance the setting of the moving rotor by one.
     */
    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }

    @Override
    public String toString() {
        return "MovingRotor " + name();
    }

    /**
     * the positions where i have my notches.
     */
    private String _notches;
}
