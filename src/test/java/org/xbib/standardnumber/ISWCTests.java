package org.xbib.standardnumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class ISWCTests {

    @Test
    public void testISWC() {
        StandardNumber iswc = new ISWC().set("T-034524680-1").normalize().verify();
        assertEquals("T0345246801", iswc.normalizedValue());
        assertEquals("ISWC T-034524680-1", iswc.format());
    }

}
