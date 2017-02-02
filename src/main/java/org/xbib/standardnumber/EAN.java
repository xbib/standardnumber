package org.xbib.standardnumber;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * European Article Number is a 13-digit barcoding standard for marking products
 * sold at retail point of sale.
 *
 * Numbers encoded in UPC and EAN barcodes are known as
 * Global Trade Item Numbers (GTIN).
 */
public class EAN extends StandardNumber implements Cloneable, Comparable<EAN> {

    private static final Pattern PATTERN = Pattern.compile("\\b[\\p{Digit}\\s]{13,18}\\b");

    private boolean createWithChecksum;

    public EAN() {
        super("ean");
    }

    @Override
    public int compareTo(EAN ean) {
        return ean != null ? normalizedValue().compareTo(ean.normalizedValue()) : -1;
    }

    @Override
    public EAN createChecksum(boolean createWithChecksum) {
        this.createWithChecksum = createWithChecksum;
        return this;
    }

    @Override
    public EAN normalize() {
        Matcher m = PATTERN.matcher(value);
        if (m.find()) {
            this.value = despace(value.substring(m.start(), m.end()));
        }
        return this;
    }

    @Override
    public boolean isValid() {
        return value != null && !value.isEmpty() && check();
    }

    @Override
    public EAN verify() {
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
    public EAN reset() {
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
        return chk == (value.charAt(l) - '0');
    }

    private String despace(String s) {
        StringBuilder sb = new StringBuilder(s);
        int i = sb.indexOf(" ");
        while (i >= 0) {
            sb.deleteCharAt(i);
            i = sb.indexOf(" ");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof EAN && value.equals(((EAN) object).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EAN ean = (EAN) super.clone();
        ean.set(value);
        return ean;
    }
}
