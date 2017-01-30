package org.xbib.standardnumber;

import java.util.Collection;

/**
 * Standard number implementation. A standard number is a number that
 *
 * * is backed by an international standard or a de-facto community use
 *
 * * can accept alphanumeric values (digits and letters and separator characters)
 *
 * * can be normalizedValue
 *
 * * can be verified and raise en error is verification fails
 *
 * * must have a createChecksum
 *
 * * can be formatted to a printable representation
 */
public abstract class StandardNumber extends StandardNumberSpi {

    protected final String type;

    protected String value;

    protected StandardNumber(String type) {
        this.type = type;
    }

    /**
     * Return the type of this standard number.
     *
     * @return the type
     */
    public String type() {
        return type;
    }

    /**
     * Set the input value of this standard number. The input must be normalized
     * and verified before being accepted as valid.
     *
     * @param value the raw input value
     * @return this standard number
     */
    public StandardNumber set(CharSequence value) {
        this.value = value != null ? value.toString() : null;
        return this;
    }

    /**
     * Normalize the value by removing all unwanted characters or
     * replacing characters with the ones required for verification.
     *
     * @return this standard number
     */
    public abstract StandardNumber normalize();

    /**
     * Check this number for validity.
     *
     * @return true if valid, false otherwise
     */
    public abstract boolean isValid();

    /**
     * Verify the number.
     *
     * @return this standard number if verification was successful
     * @throws NumberFormatException if verification failed
     */
    public abstract StandardNumber verify();

    /**
     * Indicate that a correct check sum should be computed.
     *
     * @param withChecksum if checksum is included
     * @return this standard number
     */
    public abstract StandardNumber createChecksum(boolean withChecksum);

    /**
     * Return normalized value of this standard number.
     * In most cases, this is also the canonical form of the standard number.
     * This is a representation without unneccessary characters, useful
     * for computation purposes, like comparing for equivalence.
     *
     * @return the normalized value
     */
    public abstract String normalizedValue();

    /**
     * Return a formatted value of this standard number
     * This is best for human-readable representation, but is
     * not necessarily a format for computation.
     *
     * @return a formatted value
     */
    public abstract String format();

    public abstract StandardNumber reset();

    public abstract Collection<String> getTypedVariants();
}
