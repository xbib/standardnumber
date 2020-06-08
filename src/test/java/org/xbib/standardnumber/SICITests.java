package org.xbib.standardnumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class SICITests {

    @Test
    public void testSICI1() {
        StandardNumber sici = new SICI().set("0095-4403(199502/03)21:3<12:WATIIB>2.0.TX;2-J").normalize().verify();
        assertEquals("0095-4403(199502/03)21:3<12:WATIIB>2.0.TX;2-J", sici.normalizedValue());
        assertEquals("SICI 0095-4403(199502/03)21:3<12:WATIIB>2.0.TX;2-J", sici.format());
    }

}
