package org.xbib.standardnumber;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DOITests {

    @Test
    public void testDOI() throws Exception {
        StandardNumber doi = StandardNumber.getInstance("doi").set("10.1016/0032-3861(93)90481-o").normalize().verify();
        assertEquals("10.1016/0032-3861(93)90481-o", doi.normalizedValue());
        assertEquals("http://doi.org/10.1016/0032-3861(93)90481-o", doi.format());
    }

    @Test
    public void testClone() throws Exception {
        StandardNumber doi = StandardNumber.getInstance("doi").set("10.1016/0032-3861(93)90481-o").normalize().verify();
        StandardNumber doi2 = (StandardNumber) doi.clone();
        assertEquals(doi, doi2);
    }
}
