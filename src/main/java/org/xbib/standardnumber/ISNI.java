package org.xbib.standardnumber;

import org.xbib.standardnumber.checksum.iso7064.MOD112;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ISO 27729 International Standard Name Identifier (ISNI).
 *
 * The International Standard Name Identifier (ISNI) is a method for uniquely identifying
 * the public identities of contributors to media content such as books, TV programmes,
 * and newspaper articles. Such an identifier consists of 16 numerical digits divided
 * into four blocks.
 *
 * Checksum calculation is in accordance to ISO/IEC 7064:2003, MOD 11-2.
 */
public class ISNI extends StandardNumber implements Cloneable, Comparable<ISNI> {

    private static final Pattern PATTERN = Pattern.compile("[\\p{Digit}xX\\p{Pd}\\s]{16,24}");

    private static final MOD112 check = new MOD112();

    private String formatted;

    private boolean createWithChecksum;

    public ISNI() {
        super("isni");
    }

    protected ISNI(String type) {
        super(type);
    }

    @Override
    public ISNI createChecksum(boolean createWithChecksum) {
        this.createWithChecksum = createWithChecksum;
        return this;
    }

    @Override
    public boolean isValid() {
        return value != null && !value.isEmpty() && check();
    }

    @Override
    public ISNI verify() {
        if (!check()) {
            throw new NumberFormatException("bad checksum");
        }
        return this;
    }

    /**
     * Returns the value representation of the standard number.
     *
     * @return value
     */
    @Override
    public String normalizedValue() {
        return value;
    }

    /**
     * Format this number.
     *
     * @return the formatted number
     */
    @Override
    public String format() {
        if (formatted == null) {
            this.formatted = value;
        }
        return formatted;
    }

    @Override
    public ISNI normalize() {
        Matcher m = PATTERN.matcher(value);
        if (m.find()) {
            this.value = clean(value.substring(m.start(), m.end()));
        }
        return this;
    }

    @Override
    public ISNI reset() {
        this.value = null;
        this.formatted = null;
        this.createWithChecksum = false;
        return this;
    }

    @Override
    public Collection<String> getTypedVariants() {
        return Arrays.asList(
                type().toUpperCase() + " " + format(),
                type().toUpperCase() + " " + normalizedValue());
    }

    private boolean check() {
        if (createWithChecksum) {
            this.value = check.encode(value.length() < 16 ? value : value.substring(0, value.length() - 1));
        }
        return value.length() >= 16 && check.verify(value);
    }

    private String clean(String isbn) {
        StringBuilder sb = new StringBuilder(isbn);
        int i = sb.indexOf("-");
        while (i >= 0) {
            sb.deleteCharAt(i);
            i = sb.indexOf("-");
        }
        i = sb.indexOf(" ");
        while (i >= 0) {
            sb.deleteCharAt(i);
            i = sb.indexOf(" ");
        }
        return sb.toString();
    }


    @Override
    public int compareTo(ISNI isni) {
        return value != null ? value.compareTo((isni).normalizedValue()) : -1;
    }

    @Override
    public boolean equals(Object object) {
        return object != null && getClass() == object.getClass() && value.equals(((ISNI) object).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ISNI isni = (ISNI) super.clone();
        isni.set(value);
        return isni;
    }
}
