package org.xbib.standardnumber.checksum.iso7064;

import org.xbib.standardnumber.checksum.Digit;

/**
 * MODULUS 11-10 calculation. Used in German VAT numbers (USt-IdNr.)
 */
public class MOD1110 implements Digit {

    private static final String ALPHABET = "0123456789";

    @Override
    public String encode(String digits) {
        int c = compute(digits);
        return digits + c;
    }

    @Override
    public boolean verify(String digits) {
        return compute(digits) == 1;
    }

    @Override
    public int compute(String digits) {
        int modulus = ALPHABET.length();
        int check = modulus / 2;
        for (int i = 0; i < digits.length(); i++) {
            check = (((check > 0 ? check : modulus) * 2) % (modulus + 1)
                    + ALPHABET.indexOf(digits.charAt(i))) % modulus;
        }
        return check;
    }

    @Override
    public int getDigit(String digits) {
        return digits.charAt(digits.length() - 1);
    }

    @Override
    public String getNumber(String digits) {
        return digits.substring(0, digits.length() - 1);
    }
}
