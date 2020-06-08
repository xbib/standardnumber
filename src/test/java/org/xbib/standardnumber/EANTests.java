package org.xbib.standardnumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class EANTests {

    @Test
    public void testEAN() throws Exception {
        String value = "4 007630 000110";
        StandardNumber ean = StandardNumber.getInstance("ean").set(value).createChecksum(true).normalize().verify();
        assertEquals("4007630000116", ean.normalizedValue());
        assertEquals("4007630000116", ean.format());
    }

    @Test
    public void testEAN2() throws Exception {
        String value = "7501031311309";
        StandardNumber ean = StandardNumber.getInstance("ean").set(value).createChecksum(true).normalize().verify();
        assertEquals("7501031311309", ean.normalizedValue());
        assertEquals("7501031311309", ean.format());
    }

    @Test
    public void testEAN3() throws Exception {
        String value = "9781617291623";
        StandardNumber ean = StandardNumber.getInstance("ean").set(value).normalize().verify();
        assertEquals("9781617291623", ean.normalizedValue());
        assertEquals("9781617291623", ean.format());
    }


}
