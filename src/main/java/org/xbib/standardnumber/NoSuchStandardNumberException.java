package org.xbib.standardnumber;

/**
 * An exception that is thrown if a standard number implementation is not available.
 */
public class NoSuchStandardNumberException extends Exception {
    private static final long serialVersionUID = 1278209793544774590L;

    public NoSuchStandardNumberException(String msg) {
        super(msg);
    }
}
