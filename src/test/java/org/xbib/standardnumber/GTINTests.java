package org.xbib.standardnumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class GTINTests {

    @Test
    public void testGTIN() throws Exception {
        String value = "4104420033801";
        StandardNumber gtin = StandardNumber.getInstance("gtin").set(value).normalize().verify();
        assertEquals("4104420033801", gtin.normalizedValue());
        assertEquals("4104420033801", gtin.format());
    }

    @Test
    public void testISSNGTIN() throws Exception {
        String value = "977-1869712-03-8";
        StandardNumber gtin = StandardNumber.getInstance("gtin").set(value).normalize().verify();
        assertEquals("9771869712038", gtin.normalizedValue());
        assertEquals("9771869712038", gtin.format());
    }

    @Test
    public void testGTIN2() throws Exception {
        String value = "4191054501707";
        StandardNumber gtin = StandardNumber.getInstance("gtin").set(value).normalize().verify();
        assertEquals("4191054501707", gtin.normalizedValue());
        assertEquals("4191054501707", gtin.format());
    }

    @Test
    public void testISBNGTIN() throws Exception {
        String value = "9783652002264";
        StandardNumber gtin = StandardNumber.getInstance("gtin").set(value).normalize().verify();
        assertEquals("9783652002264", gtin.normalizedValue());
        assertEquals("9783652002264", gtin.format());
    }
}
