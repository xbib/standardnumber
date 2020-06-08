package org.xbib.standardnumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class ZDBTests {

    @Test
    public void testZDB1() {
        StandardNumber zdb = new ZDB().set("127").normalize().verify();
        assertEquals("127", zdb.normalizedValue());
    }

    @Test
    public void testZDB2() {
        StandardNumber zdb = new ZDB().set("127976-2").normalize().verify();
        assertEquals("1279762", zdb.normalizedValue());
        assertEquals("127976-2", zdb.format());
    }

    @Test
    public void testZDB3() {
        StandardNumber zdb = new ZDB().set("1279760").createChecksum(true).normalize().verify();
        assertEquals("1279762", zdb.normalizedValue());
        assertEquals("127976-2", zdb.format());
    }
}
