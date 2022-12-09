package enigma;

import com.sun.xml.internal.rngom.xml.sax.AbstractLexicalHandler;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;


import static enigma.EnigmaException.*;

/**
 * Enigma simulator.
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Check ARGS and open the necessary files (see comment on main).
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _config = getInput(args[0]);
        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }
        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() {
        Machine machine = readConfig();
        if (!_input.hasNext("\\*")) {
            throw error("Error: missing setting");
        }
        while (_input.hasNextLine()) {
            String line = _input.nextLine();
            if (line.contains("*")) {
                setUp(machine, line);
                continue;
            }
            if (line.isBlank()) {
                _output.println();
            } else {
                line = machine.convert(line.replaceAll("\\s+", ""));
                printMessageLine(line);
            }
        }
    }


    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        try {
            ArrayList<Rotor> allRotors = new ArrayList<>();
            _alphabetString = _config.nextLine();
            _alphabet = new Alphabet(_alphabetString);
            if (!_config.hasNextInt()) {
                throw error("Error: wrong numSlot value");
            }
            _numRotors = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw error("Error: wrong numPawl value");
            }
            _numPawls = _config.nextInt();
            while (_config.hasNext()) {
                Rotor R = readRotor();
                allRotors.add(R);
            }
            return new Machine(_alphabet, _numRotors, _numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /**
     * Return a rotor, reading its description from _config.
     */
    private Rotor readRotor() {
        try {
            String name = "";
            String type = "";
            String permString = "";
            name = _config.next();
            type = _config.next();
            while (_config.hasNext("\\(.+\\)")) {
                permString += _config.next();
            }
            Permutation perm = new Permutation(permString, _alphabet);
            if (type.charAt(0) == 'R') {
                return new Reflector(name, perm);
            }
            if (type.charAt(0) == 'N') {
                return new FixedRotor(name, perm);
            }
            if (type.charAt(0) == 'M') {
                String notches = "";
                if (type.length() > 1) {
                    notches += type.substring(1);
                }
                return new MovingRotor(name, perm, notches);
            } else {
                String errorMsg = String.format("Error: "
                        + "bad rotor name, current rotor name is %s", name);
                throw error(errorMsg);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }


    /**
     * Set M according to the specification given in input,
     * assuming the input always begins with an asterisk.
     * taking LINE that begins with "*" as a setup.
     */
    private void setUp(Machine M, String line) {
        Scanner setup = new Scanner(line);
        setup.next();
        String[] rotorsToInsert = new String[M.numRotors()];
        for (int i = 0; i < rotorsToInsert.length; i++) {
            rotorsToInsert[i] = setup.next();
        }
        M.insertRotors(rotorsToInsert);
        M.setRotors(setup.next());
        if (setup.hasNext("[^(]+")) {
            String ringStellung = setup.next();
            M.ringSetRotors(ringStellung);
        }
        String plugboardCycles = "";
        while (setup.hasNext()) {
            plugboardCycles += setup.next();
        }
        M.setPlugboard(new Permutation(plugboardCycles, _alphabet));
    }

    /**
     * Print MSG in groups of five (except that the last group may
     * have fewer letter).
     */
    private void printMessageLine(String msg) {
        String print = "";
        int counter = 5;
        for (int i = 0; i < msg.length(); i++) {
            if (counter == 0) {
                counter = 5;
                print += ' ';
                if (msg.charAt(i) == ' ') {
                    continue;
                }
            }
            print += msg.charAt(i);
            counter -= 1;
        }
        _output.println(print);
    }

    /**
     * Alphabet used in this machine.
     */
    private Alphabet _alphabet;

    /**
     * Alphabet of the current machine, represented in a string.
     */
    private String _alphabetString;

    /**
     * Source of input messages.
     */
    private Scanner _input;

    /**
     * Source of machine configuration.
     */
    private Scanner _config;

    /**
     * File for encoded/decoded messages.
     */
    private PrintStream _output;

    /**
     * num of available slots in the current machine.
     * they must be filled in once the machine is setup.
     */
    private int _numRotors;

    /**
     * num of pawls in the current machine,
     * symbolizes the amount of moving rotors.
     */
    private int _numPawls;


}
