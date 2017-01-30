package org.xbib.standardnumber;

import java.util.Locale;

/**
 */
abstract class StandardNumberSpi {

    @SuppressWarnings("unchecked")
    public static StandardNumber getInstance(String type) throws NoSuchStandardNumberException {
        return getInstance(StandardNumberSpi.class.getPackage(), StandardNumberSpi.class.getClassLoader(), type);
    }

    @SuppressWarnings("unchecked")
    public static StandardNumber getInstance(Package thePackage, ClassLoader classLoader, String type)
            throws NoSuchStandardNumberException {
        String className = thePackage.getName() + "." + type.toUpperCase(Locale.ROOT);
        try {
            Class<StandardNumber> clazz = (Class<StandardNumber>) classLoader.loadClass(className);
            return clazz.newInstance();
        } catch (Exception e) {
            throw new NoSuchStandardNumberException(type);
        }
    }

    /**
     * Returns a clone if the implementation is cloneable.
     *
     * @return a clone if the implementation is cloneable.
     * @throws CloneNotSupportedException if this is called on an
     *                                    implementation that does not support `Cloneable`
     */
    public Object clone() throws CloneNotSupportedException {
        if (this instanceof Cloneable) {
            return super.clone();
        } else {
            throw new CloneNotSupportedException();
        }
    }
}
