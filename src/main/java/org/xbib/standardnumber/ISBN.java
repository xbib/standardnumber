package org.xbib.standardnumber;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * :linkattrs:
 *
 * ISO 2108 International Standard Book Number (ISBN), Z39.50 BIB-1 Use Attribute 7.
 *
 * The International Standard Book Number is a 13-digit number
 * that uniquely identifies books and book-like products published
 * internationally.
 *
 * The purpose of the ISBN is to establish and identify one title or
 * edition of a title from one specific publisher
 * and is unique to that edition, allowing for more efficient marketing of products by booksellers,
 * libraries, universities, wholesalers and distributors.
 *
 * Every ISBN consists of thirteen digits and whenever it is printed it is preceded by the letters ISBN.
 * The thirteen-digit number is divided into four parts of variable length, each part separated by a hyphen.
 *
 * This class is based upon the ISBN converter and formatter class by
 * link:http://www.openly.com/[Openly Informatics, Inc.]
 *
 * See link:https://www.isbn-international.org/content/isbn-users-manual[The ISBN Users' Manual, window='_blank'],
 * link:https://www.ietf.org/proceedings/37/charters/urn-charter.html[The IETF URN Charter, window='_blank'],
 * link:http://www.iana.org/assignments/urn-namespaces[The IANA URN assignments, window='_blank'],
 * link:https://www.isbn-international.org/range_file_generation[ISBN prefix generation, window='_blank']
 */
public class ISBN extends StandardNumber implements Cloneable, Comparable<ISBN> {

    private static final Pattern PATTERN = Pattern.compile("[\\p{Digit}xX\\p{Pd}]{10,17}");

    private static final List<String> ranges = new ISBNRangeMessageConfigurator().getRanges();

    private boolean createWithChecksum;

    private String eanvalue;

    private boolean eanPreferred;

    private boolean valid;

    private boolean isEAN;

    public ISBN() {
        super("isbn");
    }

    @Override
    public ISBN createChecksum(boolean createWithChecksum) {
        this.createWithChecksum = createWithChecksum;
        return this;
    }

    @Override
    public int compareTo(ISBN isbn) {
        return value != null ? value.compareTo(isbn.normalizedValue()) : -1;
    }

    @Override
    public ISBN normalize() {
        Matcher m = PATTERN.matcher(value);
        this.value = m.find() ? dehyphenate(value.substring(m.start(), m.end())) : null;
        return this;
    }

    /**
     * Check for this ISBN number validity.
     *
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isValid() {
        return value != null && !value.isEmpty() && check() && (eanPreferred ? eanvalue != null : value != null);
    }

    @Override
    public ISBN verify() {
        if (value == null || value.isEmpty()) {
            throw new NumberFormatException("must not be null");
        }
        check();
        this.valid = eanPreferred ? eanvalue != null : value != null;
        if (!valid) {
            throw new NumberFormatException("invalid number");
        }
        return this;
    }

    /**
     * Get the normalized value of this standard book number.
     *
     * @return the value of this standard book number
     */
    @Override
    public String normalizedValue() {
        return eanPreferred ? eanvalue : value;
    }

    /**
     * Get printable representation of this standard book number.
     *
     * @return ISBN-13, with (fixed) check digit
     */
    @Override
    public String format() {
        if ((!eanPreferred && value == null) || eanvalue == null) {
            return null;
        }
        return eanPreferred ?
                fix(eanvalue) :
                fix("978" + value).substring(4);
    }

    @Override
    public ISBN reset() {
        this.value = null;
        this.createWithChecksum = false;
        this.eanvalue = null;
        this.eanPreferred = false;
        this.valid = false;
        this.isEAN = false;
        return this;
    }

    @Override
    public Collection<String> getTypedVariants() {
        return Arrays.asList(
                type().toUpperCase() + " " + format(),
                type().toUpperCase() + " " + normalizedValue());
    }

    public boolean isEAN() {
        return isEAN;
    }

    /**
     * Prefer European Article Number (EAN, ISBN-13).
     *
     * @param eanPreferred true if EAN should be preferred
     * @return this ISBN object
     */
    public ISBN ean(boolean eanPreferred) {
        this.eanPreferred = eanPreferred;
        return this;
    }

    /**
     * Get country and publisher code.
     *
     * @return the country/publisher code from ISBN
     */
    public String getCountryAndPublisherCode() {
        // we don't care about the wrong createChecksum when we fix the value
        String code = eanvalue != null ? fix(eanvalue) : fix("978" + value);
        String s = code.substring(4);
        int pos1 = s.indexOf('-');
        if (pos1 <= 0) {
            return null;
        }
        String pubCode = s.substring(pos1 + 1);
        int pos2 = pubCode.indexOf('-');
        if (pos2 <= 0) {
            return null;
        }
        return code.substring(0, pos1 + pos2 + 5);
    }

    private String hyphenate(String pref, String s) {
        String prefix = pref;
        String isbn = s;
        StringBuilder sb = new StringBuilder(prefix.substring(0, 4)); // '978-', '979-'
        prefix = prefix.substring(4);
        isbn = isbn.substring(3); // 978, 979
        int i = 0;
        int j = 0;
        while (i < prefix.length()) {
            char ch = prefix.charAt(i++);
            if (ch == '-') {
                sb.append('-'); // set first hyphen
            } else {
                sb.append(isbn.charAt(j++));
            }
        }
        sb.append('-'); // set second hyphen
        while (j < (isbn.length() - 1)) {
            sb.append(isbn.charAt(j++));
        }
        sb.append('-'); // set third hyphen
        sb.append(isbn.charAt(isbn.length() - 1));
        return sb.toString();
    }

    private boolean check() {
        this.eanvalue = null;
        this.isEAN = false;
        int val;
        int i;
        if (value.length() < 9) {
            return false;
        }
        if (value.length() == 10) {
            // ISBN-10
            int checksum = 0;
            int weight = 10;
            i = 0;
            while (weight > 0) {
                val = value.charAt(i) == 'X' || value.charAt(i) == 'x' ? 10
                        : value.charAt(i) - '0';
                if (val >= 0) {
                    if (val == 10 && weight != 1) {
                        return false;
                    }
                    checksum += weight * val;
                    weight--;
                } else {
                    return false;
                }
                i++;
            }
            String s = value.substring(0, 9);
            if (checksum % 11 != 0) {
                if (createWithChecksum) {
                    this.value = s + createCheckDigit10(s);
                } else {
                    return false;
                }
            }
            this.eanvalue = "978" + s + createCheckDigit13("978" + s);
        } else if (value.length() == 13) {
            // ISBN-13 "book land"
            if (!value.startsWith("978") && !value.startsWith("979")) {
                return false;
            }
            int checksum13 = 0;
            int weight13 = 1;
            for (i = 0; i < 13; i++) {
                val = value.charAt(i) == 'X' || value.charAt(i) == 'x' ? 10 : value.charAt(i) - '0';
                if (val >= 0) {
                    if (val == 10) {
                        return false;
                    }
                    checksum13 += (weight13 * val);
                    weight13 = (weight13 + 2) % 4;
                } else {
                    return false;
                }
            }
            // set value
            if ((checksum13 % 10) != 0) {
                if (eanPreferred && createWithChecksum) {
                    // with createChecksum
                    eanvalue = value.substring(0, 12) + createCheckDigit13(value.substring(0, 12));
                } else {
                    return false;
                }
            } else {
                eanvalue = value;
            }
            if (!eanPreferred && (eanvalue.startsWith("978") || eanvalue.startsWith("979"))) {
                // create 10-digit from 13-digit
                this.value = eanvalue.substring(3, 12) + createCheckDigit10(eanvalue.substring(3, 12));
            } else {
                // 10 digit version not available - not an error
                this.value = null;
            }
            this.isEAN = true;
        } else if (value.length() == 9) {
            String s = value.substring(0, 9);
            // repair ISBN-10 ?
            if (createWithChecksum) {
                // create 978 from 10-digit without createChecksum
                eanvalue = "978" + s + createCheckDigit13("978" + s);
                value = s + createCheckDigit10(s);
            } else {
                return false;
            }
        } else if (value.length() == 12) {
            // repair ISBN-13 ?
            if (!value.startsWith("978") && !value.startsWith("979")) {
                return false;
            }
            if (createWithChecksum) {
                String s = value.substring(0, 9);
                String t = value.substring(3, 12);
                // create 978 from 10-digit
                this.eanvalue = "978" + s + createCheckDigit13("978" + s);
                this.value = t + createCheckDigit10(t);
            } else {
                return false;
            }
            this.isEAN = true;
        } else {
            return false;
        }
        return true;
    }

    /**
     * Returns a ISBN check digit for the first 9 digits in a string.
     *
     * @param value the value
     * @return check digit
     */
    private char createCheckDigit10(String value) {
        int checksum = 0;
        int val;
        int l = value.length();
        for (int i = 0; i < l; i++) {
            val = value.charAt(i) - '0';
            if (val < 0 || val > 9) {
                throw new NumberFormatException("not a digit in " + value);
            }
            checksum += val * (10 - i);
        }
        int mod = checksum % 11;
        return mod == 0 ? '0' : mod == 1 ? 'X' : (char) ((11 - mod) + '0');
    }

    /**
     * Returns an ISBN check digit for the first 12 digits in a string.
     *
     * @param value the value
     * @return check digit
     */
    private char createCheckDigit13(String value) {
        int checksum = 0;
        int weight;
        int val;
        int l = value.length();
        for (int i = 0; i < l; i++) {
            val = value.charAt(i) - '0';
            if (val < 0 || val > 9) {
                throw new NumberFormatException("not a digit in " + value);
            }
            weight = i % 2 == 0 ? 1 : 3;
            checksum += weight * val;
        }
        int mod = 10 - checksum % 10;
        return mod == 10 ? '0' : (char) (mod + '0');
    }

    private String fix(String isbn) {
        if (isbn == null) {
            return null;
        }
        for (int i = 0; i < ranges.size(); i += 2) {
            if (isInRange(isbn, ranges.get(i), ranges.get(i + 1)) == 0) {
                return hyphenate(ranges.get(i), isbn);
            }
        }
        return isbn;
    }

    /**
     * Check if ISBN is within a given value range.
     *
     * @param isbn  ISBN to check
     * @param begin lower ISBN
     * @param end   higher ISBN
     * @return -1 if too low, 1 if too high, 0 if range matches
     */
    private int isInRange(String isbn, String begin, String end) {
        String b = dehyphenate(begin);
        int blen = b.length();
        int c = blen <= isbn.length() ?
                isbn.substring(0, blen).compareTo(b) :
                isbn.compareTo(b);
        if (c < 0) {
            return -1;
        }
        String e = dehyphenate(end);
        int elen = e.length();
        c = e.compareTo(isbn.substring(0, elen));
        if (c < 0) {
            return 1;
        }
        return 0;
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
        return object instanceof ISBN && value.equals(((ISBN) object).value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ISBN isbn = (ISBN) super.clone();
        isbn.set(value);
        return isbn;
    }

    private static final class ISBNRangeMessageConfigurator {

        private final Deque<StringBuilder> content;

        private final List<String> ranges;

        private String prefix;

        private String rangeBegin;

        private String rangeEnd;

        private int length;

        private boolean valid;

        public ISBNRangeMessageConfigurator() {
            content = new ArrayDeque<>();
            ranges = new ArrayList<>();
            length = 0;
            try (InputStream in = getClass().getResourceAsStream("/org/xbib/standardnumber/RangeMessage.xml")) {
                XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
                XMLEventReader xmlReader = xmlInputFactory.createXMLEventReader(in);
                while (xmlReader.hasNext()) {
                    processEvent(xmlReader.peek());
                    xmlReader.nextEvent();
                }
            } catch (IOException | XMLStreamException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }

        private void processEvent(XMLEvent e) {
            switch (e.getEventType()) {
                case XMLEvent.START_ELEMENT:
                    StartElement element = e.asStartElement();
                    String name = element.getName().getLocalPart();
                    if ("RegistrationGroups".equals(name)) {
                        valid = true;
                    }
                    content.push(new StringBuilder());
                    break;
                case XMLEvent.END_ELEMENT:
                    EndElement endElement = e.asEndElement();
                    String endName = endElement.getName().getLocalPart();
                    String v = content.pop().toString();
                    if ("Prefix".equals(endName)) {
                        prefix = v;
                    }
                    if ("Range".equals(endName)) {
                        int pos = v.indexOf('-');
                        if (pos > 0) {
                            rangeBegin = v.substring(0, pos);
                            rangeEnd = v.substring(pos + 1);
                        }
                    }
                    if ("Length".equals(endName)) {
                        length = Integer.parseInt(v);
                    }
                    if ("Rule".equals(endName) && valid && rangeBegin != null && rangeEnd != null && length > 0) {
                        ranges.add(prefix + "-" + rangeBegin.substring(0, length));
                        ranges.add(prefix + "-" + rangeEnd.substring(0, length));
                    }
                    break;
                case XMLEvent.CHARACTERS:
                    Characters c = (Characters) e;
                    if (!c.isIgnorableWhiteSpace()) {
                        String text = c.getData().trim();
                        if (text.length() > 0 && !content.isEmpty()) {
                            content.peek().append(text);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        List<String> getRanges() {
            return ranges;
        }
    }
}
