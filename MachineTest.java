
package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;


import java.util.ArrayList;
import java.util.Collection;

import static enigma.TestUtils.*;

/** Testing machines, I guess
 */
public class MachineTest {
    Permutation permB = new Permutation(
            "(AE) (BN) (CK) (DQ) (FU) (GY) "
                   + "(HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", UPPER);
    Permutation permBeta = new Permutation(
            "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", UPPER);
    Permutation permV = new Permutation(
            "(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)", UPPER);
    Permutation permIII = new Permutation(
            "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", UPPER);
    Permutation permII = new Permutation(
            "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)", UPPER);
    Permutation permI = new Permutation(
            "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);
    /* ***** TESTS ***** */

    @Test
    public void check() {
        _B = new Reflector("B", permB);
        _Beta = new FixedRotor("Beta", permBeta);
        _V = new MovingRotor("V", permV, "Z");
        _III = new MovingRotor("III", permIII, "V");
        _II = new MovingRotor("II", permII, "E");
        _I = new MovingRotor("I", permI, "Q");
        rotors = new ArrayList<Rotor>();
        rotors.add(_B);
        rotors.add(_Beta);
        rotors.add(_V);
        rotors.add(_III);
        rotors.add(_II);
        rotors.add(_I);
        String[] rotorsToinsert = new String[5];
        rotorsToinsert[0] = "B";
        rotorsToinsert[1] = "Beta";
        rotorsToinsert[2] = "III";
        rotorsToinsert[3] = "II";
        rotorsToinsert[4] = "I";
        _machine = new Machine(UPPER, 5, 3, rotors);
        _machine.insertRotors(rotorsToinsert);
        _machine.setRotors("AADQ");
        String converted = _machine.convert("AA");
        System.out.println(converted);
    }

    Machine _machine;
    Collection<Rotor> rotors;
    Rotor _V;
    Rotor _B;
    Rotor _Beta;
    Rotor _III;
    Rotor _II;
    Rotor _I;
}
