package org.xbib.standardnumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class ISMNTests {

    @Test
    public void testISMN() throws Exception {
        ISMN ismn = new ISMN();
        ismn.set("M-2306-7118-7").normalize().verify();
        assertEquals("9790230671187", ismn.normalizedValue());
        assertEquals("9790230671187", ismn.format());
        assertEquals("9790230671187", ismn.createChecksum(true).toGTIN().normalizedValue());
    }

    @Test
    public void testISMN2() throws Exception {
        ISMN ismn = new ISMN();
        ismn.set("979-0-3452-4680-5").normalize().verify();
        assertEquals("9790345246805", ismn.normalizedValue());
        assertEquals("9790345246805", ismn.format());
        assertEquals("9790345246805", ismn.createChecksum(true).toGTIN().normalizedValue());
    }

    @Test
    public void testISMNChecksum() throws Exception {
        ISMN ismn = new ISMN();
        ismn.set("979-0-3452-4680").createChecksum(true).normalize().verify();
        assertEquals("9790345246805", ismn.normalizedValue());
        assertEquals("9790345246805", ismn.format());
        assertEquals("9790345246805", ismn.toGTIN().normalizedValue());
    }
}
