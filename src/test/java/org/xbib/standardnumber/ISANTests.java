package org.xbib.standardnumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class ISANTests {

    @Test
    public void testISAN0() throws Exception {
        String value = "ISAN B159-D8FA-0124-0000-K";
        StandardNumber isan = StandardNumber.getInstance("isan").set(value).createChecksum(true).normalize().verify();
        assertEquals("B159D8FA01240000K", isan.normalizedValue());
        assertEquals("ISAN B159-D8FA-0124-0000-K", isan.format());
    }

    @Test
    public void testISAN1() throws Exception {
        String value = "ISAN 0000-3BAB-9352-0000-G ";
        StandardNumber isan = StandardNumber.getInstance("isan").set(value).createChecksum(true).normalize().verify();
        assertEquals("00003BAB93520000G", isan.normalizedValue());
        assertEquals("ISAN 0000-3BAB-9352-0000-G", isan.format());
    }

    @Test
    public void testISAN2() throws Exception {
        String value = "006A-15FA-002B-C95F-W";
        StandardNumber isan = StandardNumber.getInstance("isan").set(value).createChecksum(true).normalize().verify();
        assertEquals("006A15FA002BC95FW", isan.normalizedValue());
        assertEquals("ISAN 006A-15FA-002B-C95F-W", isan.format());
    }

    @Test
    public void testISAN3() throws Exception {
        String value = "1881-66C7-3420-0000-3";
        StandardNumber isan = StandardNumber.getInstance("isan").set(value).createChecksum(true).normalize().verify();
        assertEquals("188166C7342000003", isan.normalizedValue());
        assertEquals("ISAN 1881-66C7-3420-0000-3", isan.format());
    }

    @Test
    public void testVersionedISAN() throws Exception {
        String value = "0001-F54C-302A-8D98-N-0000-0121-O";
        StandardNumber isan = StandardNumber.getInstance("isan").set(value).createChecksum(true).normalize().verify();
        assertEquals("0001F54C302A8D98N00000121O", isan.normalizedValue());
        assertEquals("ISAN 0001-F54C-302A-8D98-N-0000-0121-O", isan.format());
    }

    @Test
    public void testVersionedISAN2() throws Exception {
        String value = "1881-66C7-3420-6541-Y-9F3A-0245-O";
        StandardNumber isan = StandardNumber.getInstance("isan").set(value).createChecksum(true).normalize().verify();
        assertEquals("188166C734206541Y9F3A0245O", isan.normalizedValue());
        assertEquals("ISAN 1881-66C7-3420-6541-Y-9F3A-0245-O", isan.format());
    }

    @Test
    public void testVersionedISAN3() throws Exception {
        String value = "0000-0001-8CFA-0000-I-0000-0000-K";
        StandardNumber isan = StandardNumber.getInstance("isan").set(value).createChecksum(true).normalize().verify();
        assertEquals("000000018CFA0000I00000000K", isan.normalizedValue());
        assertEquals("ISAN 0000-0001-8CFA-0000-I-0000-0000-K", isan.format());
    }

    @Test
    public void testVersionedISAN4() throws Exception {
        String value = "0000-0000-D07A-0090-Q-0000-0000-X";
        StandardNumber isan = StandardNumber.getInstance("isan").set(value).createChecksum(true).normalize().verify();
        assertEquals("00000000D07A0090Q00000000X", isan.normalizedValue());
        assertEquals("ISAN 0000-0000-D07A-0090-Q-0000-0000-X", isan.format());
    }

    @Test
    public void testVersionedISAN5() throws Exception {
        String value = "0000-3BAB-9352-0000-G-0000-0000-Q";
        StandardNumber isan = StandardNumber.getInstance("isan").set(value).createChecksum(true).normalize().verify();
        assertEquals("00003BAB93520000G00000000Q", isan.normalizedValue());
        assertEquals("ISAN 0000-3BAB-9352-0000-G-0000-0000-Q", isan.format());
    }

    @Test
    public void testBrokenISAN() {
        try {
            String value = "1435-1838 = Lehrergilde-Rundbrief";
            new ISAN().set(value).createChecksum(true).normalize().verify();
            fail();
        } catch (NumberFormatException e) {
            assertTrue(true);
        }
    }
}
