package enigma;

import static enigma.EnigmaException.*;

/**
 * Class that represents a reflector in the enigma.
 *
 */
class Reflector extends FixedRotor {

    /**
     * A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM.
     */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        if (!perm.derangement()) {
            throw error("Error: reflector must be in derangement");
        }
    }

    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("Error: reflector cannot be set");
        }
    }

    @Override
    public String toString() {
        return "Reflector " + name();
    }

}
