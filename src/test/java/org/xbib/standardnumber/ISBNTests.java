package org.xbib.standardnumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class ISBNTests {

    @Test
    public void testDehypenate() {
        assertEquals("000111333", new ISBN().set("000-111-333").normalize().normalizedValue());
    }

    @Test
    public void testISBNTooShort() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new ISBN().set("12-7").normalize().verify());
    }

    @Test
    public void testDirtyISBN() throws Exception {
        String value = "ISBN 3-9803350-5-4 kart. : DM 24.00";
        StandardNumber isbn = StandardNumber.getInstance("isbn").set(value).normalize().verify();
        assertEquals(isbn.normalizedValue(), "3980335054");
    }

    @Test
    public void testTruncatedISBN() {
        Assertions.assertThrows(NumberFormatException.class, () -> {
            String value = "ISBN";
            new ISBN().set(value).normalize().verify();
        });
    }

    @Test
    public void fixChecksum() throws Exception {
        String value = "3616065810";
        StandardNumber isbn = StandardNumber.getInstance("isbn").set(value).createChecksum(true).normalize().verify();
        assertEquals("361606581X", isbn.normalizedValue());
    }

    @Test
    public void testEAN() {
        String value = "978-3-551-75213-0";
        StandardNumber isbn = new ISBN().ean(true).set(value).normalize().verify();
        assertEquals("9783551752130", isbn.normalizedValue());
        assertEquals("978-3-551-75213-0", isbn.format());
    }

    @Test
    public void testEAN2() {
        String value = "978-3-551-75213-1";
        StandardNumber isbn = new ISBN().ean(true).set(value).createChecksum(true).normalize().verify();
        assertEquals("9783551752130", isbn.normalizedValue());
        assertEquals("978-3-551-75213-0", isbn.format());
    }

    @Test
    public void testWrongAndDirtyEAN() {
        Assertions.assertThrows(NumberFormatException.class, () ->{
            // correct ISBN-10 is 3-451-04112-X
            String value = "ISBN ISBN 3-451-4112-X kart. : DM 24.80";
            new ISBN().ean(false).set(value).createChecksum(true).normalize().verify();
        });
    }

    @Test
    public void testVariants() {
        String content = "1-9339-8817-7.";
        ISBN isbn = new ISBN();
        isbn.set(content).normalize();
        if (!isbn.isEAN()) {
            // create up to 4 variants: ISBN, ISBN normalized, ISBN-13, ISBN-13 normalized
            if (isbn.isValid()) {
                assertEquals("1-933988-17-7", isbn.ean(false).format());
                assertEquals("1933988177", isbn.ean(false).normalizedValue());
            }
            isbn = isbn.ean(true);
            isbn.set(content).normalize();
            if (isbn.isValid()) {
                assertEquals("978-1-933988-17-7", isbn.format());
                assertEquals("9781933988177", isbn.normalizedValue());
            }
        } else {
            // 2 variants, do not create ISBN-10 for an ISBN-13
            if (isbn.isValid()) {
                assertEquals(isbn.ean(true).format(), "978-1-933988-17-7");
                assertEquals(isbn.ean(true).normalizedValue(), "9781933988177");
            }
        }
    }
}
