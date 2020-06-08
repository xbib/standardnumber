package org.xbib.standardnumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class PPNTests {

    @Test
    public void testPPN1() throws Exception {
        StandardNumber ppn = new PPN().set("641379617").normalize().verify();
        assertEquals(ppn.normalizedValue(), "641379617");
    }

    @Test
    public void testPPN2() throws Exception {
        StandardNumber ppn = new PPN().set("101115658X").normalize().verify();
        assertEquals(ppn.normalizedValue(), "101115658X");
    }

}
