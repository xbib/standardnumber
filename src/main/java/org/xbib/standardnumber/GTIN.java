package org.xbib.standardnumber;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * :linkattrs:
 *
 * Global Trade Item Number (GTIN).
 *
 * GTIN describes a family of GS1 (EAN.UCC) global data structures that employ
 * 14 digits and can be encoded into various types of data carriers.
 *
 * Currently, GTIN is used exclusively within bar codes, but it could also be used
 * in other data carriers such as radio frequency identification (RFID).
 * The GTIN is only a term and does not impact any existing standards, nor does
 * it place any additional requirements on scanning hardware.
 *
 * For North American companies, the UPC is an existing form of the GTIN.
 *
 * Since 2005, EAN International and American UCC merged to GS1 and also
 * EAN and UPC is now named GTIN.
 *
 * The EAN/UCC-13 code is now officially called GTIN-13 (Global Trade Identifier Number).
 * Former 12-digit UPC codes can be converted into EAN/UCC-13 code by simply
 * adding a preceeding zero.
 *
 * As of January 1, 2007, the former ISBN numbers have been integrated into
 * the EAN/UCC-13 code. For each old ISBN-10 code, there exists a proper translation
 * into EAN/UCC-13 by adding "978" as prefix.
 *
 * The family of data structures comprising GTIN include
 *
 * * GTIN-8 (EAN/UCC-8): this is an 8-digit number
 * * GTIN-12 (UPC-A): this is a 12-digit number
 * * GTIN-13 (EAN/UCC-13): this is a 13-digit number
 * * GTIN-14 (EAN/UCC-14 or ITF-14): this is a 14-digit number
 *
 * See link:http://www.gtin.info/[GTIN info, window='_blank']
 */
public class GTIN extends StandardNumber implements Cloneable, Comparable<GTIN> {

    private static final Pattern PATTERN = Pattern.compile("\\b[\\p{Digit}\\-]{3,18}\\b");

    private boolean createWithChecksum;

    public GTIN() {
        super("gtin");
    }

    @Override
    public int compareTo(GTIN gtin) {
        return gtin != null ? normalizedValue().compareTo(gtin.normalizedValue()) : -1;
    }

    @Override
    public GTIN createChecksum(boolean createWithChecksum) {
        this.createWithChecksum = createWithChecksum;
        return this;
    }

    @Override
    public GTIN normalize() {
        Matcher m = PATTERN.matcher(value);
        if (m.find() && value.length() >= m.end()) {
            this.value = dehyphenate(value.substring(m.start(), m.end()));
        }
        return this;
    }

    @Override
    public boolean isValid() {
        return value != null && !value.isEmpty() && check();
    }

    @Override
    public GTIN verify() {
        if (value == null || value.isEmpty()) {
            throw new NumberFormatException("invalid value");
        }
        if (!check()) {
            throw new NumberFormatException("bad checksum");
        }
        return this;
    }

    @Override
    public String normalizedValue() {
        return value;
    }

    @Override
    public String format() {
        return value;
    }

    @Override
    public GTIN reset() {
        this.value = null;
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
        int l = value.length() - 1;
        int checksum = 0;
        int weight;
        int val;
        for (int i = 0; i < l; i++) {
            val = value.charAt(i) - '0';
            weight = i % 2 == 0 ? 1 : 3;
            checksum += val * weight;
        }
        int chk = 10 - checksum % 10;
        if (createWithChecksum) {
            char ch = (char) ('0' + chk);
            value = value.substring(0, l) + ch;
        }
        return chk == value.charAt(l) - '0';
    }

    private String dehyphenate(String isbn) {
        StringBuilder sb = new StringBuilder(isbn);
        int i = sb.indexOf("-");
        while (i >= 0) {
            sb.deleteCharAt(i);
            i = sb.indexOf("-");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof GTIN && value.equals(((GTIN) object).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        GTIN gtin = (GTIN) super.clone();
        gtin.set(value);
        return gtin;
    }
}
