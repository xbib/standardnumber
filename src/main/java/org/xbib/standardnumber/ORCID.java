package org.xbib.standardnumber;

import java.net.URI;

/**
 * Open Researcher and Contributor ID - ORCID.
 *
 * ORCID is compatible to International Standard Name Identifier (ISNI, ISO 2772).
 *
 * Checksum calculation is according to ISO/IEC 7064:2003, MOD 11-2.
 */
public class ORCID extends ISNI {

    public ORCID() {
        super("orcid");
    }

    @Override
    public ORCID set(CharSequence value) {
        super.set(value);
        return this;
    }

    @Override
    public ORCID createChecksum(boolean createChecksum) {
        super.createChecksum(createChecksum);
        return this;
    }

    @Override
    public ORCID normalize() {
        super.normalize();
        return this;
    }

    @Override
    public ORCID verify() {
        super.verify();
        return this;
    }

    public URI toURI() {
        return URI.create("http://orcid.org/" + normalizedValue());
    }

    @Override
    public int compareTo(ISNI isni) {
        return isni instanceof ORCID && value != null ? value.compareTo((isni).normalizedValue()) : -1;
    }

    @Override
    public boolean equals(Object object) {
        return object != null && getClass() == object.getClass() && value.equals(((ORCID) object).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ORCID orcid = (ORCID) super.clone();
        orcid.set(value);
        return orcid;
    }
}
