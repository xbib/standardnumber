package org.xbib.standardnumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class UPCTests {

    @Test
    public void testUPC() {
        String value = "796030114977";
        StandardNumber upc = new UPC().set(value).normalize().verify();
        assertEquals("796030114977", upc.normalizedValue());
        assertEquals("796030114977", upc.format());
    }

    @Test
    public void testUPC2() {
        String value = "036000291452";
        StandardNumber upc = new UPC().set(value).normalize().verify();
        assertEquals("036000291452", upc.normalizedValue());
        assertEquals("036000291452", upc.format());
    }
}
