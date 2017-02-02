package org.xbib.standardnumber;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * :linkattrs:
 *
 * ARK Archival Resource Key.
 *
 * An ARK is a Uniform Resource Locator (URL) that is a multi-purpose identifier
 * for information objects of any type. An ARK contains the label ark: after the
 * hostname, an URL request terminated by '?' returns a brief metadata record,
 * and an URL request terminated by '??' returns metadata that includes a commitment
 * statement from the current service provider.
 *
 * The ARK and its inflections ('?' and '??') gain access to three facets of a
 * provider's ability to provide persistence.
 *
 * Implicit in the design of the ARK scheme is that persistence is purely a matter
 * of service and not a property of a naming syntax.
 *
 * See link:http://tools.ietf.org/html/draft-kunze-ark-18[ARK IETF RFC, window='_blank'],
 * link:http://www.cdlib.org/services/uc3/docs/jak_ARKs_Berlin_2012.pdf[10 years ARK, window='_blank']
 */
public class ARK extends StandardNumber implements Cloneable, Comparable<ARK> {

    private static final Pattern PATTERN = Pattern.compile("[\\p{Graph}\\p{Punct}]{0,48}");

    private static final Pattern URI_PATTERN = Pattern.compile("^(ark)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    private URI uri;

    public ARK() {
        super("ark");
    }

    @Override
    public ARK set(CharSequence value) {
        super.set(value);
        this.uri = value != null ? URI.create(value.toString()) : null;
        return this;
    }

    @Override
    public ARK createChecksum(boolean checksum) {
        return this;
    }

    @Override
    public ARK normalize() {
        if (uri == null) {
            return this;
        }
        String s = uri.toString();
        Matcher m = URI_PATTERN.matcher(s);
        if (m.find()) {
            this.uri = URI.create(s.substring(m.start(), m.end()));
        }
        m = PATTERN.matcher(s);
        if (m.find()) {
            this.uri = URI.create(s.substring(m.start(), m.end()));
        }
        return this;
    }

    @Override
    public boolean isValid() {
        return uri != null && "ark".equals(uri.getScheme());
    }

    /**
     * No verification.
     *
     * @return this ARK
     */
    @Override
    public ARK verify() {
        if (uri == null || !"ark".equals(uri.getScheme())) {
            throw new NumberFormatException();
        }
        return this;
    }

    @Override
    public String normalizedValue() {
        return uri != null ? uri.toString() : null;
    }

    @Override
    public String format() {
        return uri != null ? uri.toString() : null;
    }

    public URI asURI() {
        return uri;
    }

    @Override
    public ARK reset() {
        this.uri = null;
        return this;
    }

    @Override
    public Collection<String> getTypedVariants() {
        return Arrays.asList(type().toUpperCase() + " " + format(), type().toUpperCase() + " " + normalizedValue());
    }

    @Override
    public int compareTo(ARK ark) {
        return ark != null ? normalizedValue().compareTo(ark.normalizedValue()) : -1;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ARK && uri.equals(((ARK) object).uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ARK ark = (ARK) super.clone();
        ark.set(value);
        return ark;
    }
}
