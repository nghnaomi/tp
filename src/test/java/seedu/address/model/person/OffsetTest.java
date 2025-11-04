package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class OffsetTest {

    @Test
    public void isValidOffset_null_false() {
        assertFalse(Offset.isValidOffset(null));
    }

    @Test
    public void isValidOffset_invalidFormats_false() {
        // Missing sign
        assertFalse(Offset.isValidOffset("08:00"));
        // Wrong separators/length
        assertFalse(Offset.isValidOffset("+8:00"));
        assertFalse(Offset.isValidOffset("+0800"));
        assertFalse(Offset.isValidOffset("+08-00"));
        // Out of range hours
        assertFalse(Offset.isValidOffset("+15:00"));
        assertFalse(Offset.isValidOffset("-99:00"));
        // Out of range minutes
        assertFalse(Offset.isValidOffset("+08:60"));
        assertFalse(Offset.isValidOffset("-02:99"));
        // Garbage
        assertFalse(Offset.isValidOffset("abc"));
        assertFalse(Offset.isValidOffset(""));
        assertFalse(Offset.isValidOffset("   "));
    }

    @Test
    public void isValidOffset_validFormats_true() {
        assertTrue(Offset.isValidOffset("+00:00"));
        assertTrue(Offset.isValidOffset("-00:00"));
        assertTrue(Offset.isValidOffset("+01:00"));
        assertTrue(Offset.isValidOffset("+05:30"));
        assertTrue(Offset.isValidOffset("+14:00")); // upper hour bound allowed by regex
    }

    @Test
    public void constructor_invalid_throwsIllegalArgumentException() {
        // no sign
        assertThrows(IllegalArgumentException.class, () -> new Offset("08:00"));

        // hour > 14
        assertThrows(IllegalArgumentException.class, () -> new Offset("+15:00"));

        // minutes >= 60
        assertThrows(IllegalArgumentException.class, () -> new Offset("+01:60"));

        // junk
        assertThrows(IllegalArgumentException.class, () -> new Offset("abc"));
    }

    @Test
    public void getTotalMinutes_signAndMath() {
        assertEquals(0, new Offset("+00:00").getTotalMinutes());
        // negative zero should still be 0
        assertEquals(0, new Offset("-00:00").getTotalMinutes());
        assertEquals(60, new Offset("+01:00").getTotalMinutes());
        assertEquals(345, new Offset("+05:45").getTotalMinutes());
        assertEquals(14 * 60, new Offset("+14:00").getTotalMinutes());
    }

    @Test
    public void toString_roundTrip() {
        String s = "+09:30";
        assertEquals(s, new Offset(s).toString());
    }

    @Test
    public void compareTo_ordersByMinutesAscending() {
        Offset a = new Offset("-05:00"); // -300
        Offset b = new Offset("-00:00"); // -30
        Offset c = new Offset("+00:00"); // 0
        Offset d = new Offset("+01:00"); // 75

        List<Offset> list = Arrays.asList(d, b, a, c);
        list.sort(Offset::compareTo);

        assertEquals(Arrays.asList(a, b, c, d), list);
    }

    @Test
    public void compareTo_equalOffsets_zero() {
        Offset x = new Offset("+08:00");
        Offset y = new Offset("+08:00");
        assertEquals(0, x.compareTo(y));
    }

    @Test
    public void toZoneOffset_matchesValue() {
        Offset off = new Offset("+13:45");
        ZoneOffset zo = off.toZoneOffset();
        assertEquals(ZoneOffset.of("+13:45"), zo);
        assertEquals("+13:45", zo.toString());
    }

    @Test
    public void equals_sameValue_true() {
        assertEquals(new Offset("+02:00"), new Offset("+02:00"));
    }

    @Test
    public void equals_differentValue_false() {
        assertNotEquals(new Offset("+05:00"), new Offset("+05:30"));
        assertNotEquals(new Offset("+00:00"), new Offset("-02:00")); // different canonical strings, even if minutes==0
    }

    @Test
    public void hashCode_consistentWithEquals() {
        Offset x1 = new Offset("+03:00");
        Offset x2 = new Offset("+03:00");
        assertEquals(x1, x2);
        assertEquals(x1.hashCode(), x2.hashCode());
    }

    @Test
    public void isValidOffset_multipleOrMisplacedPlusSigns_false() {
        assertFalse(Offset.isValidOffset("++08:00"));
        assertFalse(Offset.isValidOffset("+ +08:00"));

        assertFalse(Offset.isValidOffset("+-08:00"));
        assertFalse(Offset.isValidOffset("-+08:00"));

        assertFalse(Offset.isValidOffset("08:+00"));
        assertFalse(Offset.isValidOffset("08:00+"));
        assertFalse(Offset.isValidOffset("8+00"));

        assertFalse(Offset.isValidOffset("+"));
        assertFalse(Offset.isValidOffset("+:00"));

        assertFalse(Offset.isValidOffset("+08:+00"));
        assertFalse(Offset.isValidOffset("+08:0+0"));

        assertFalse(Offset.isValidOffset("+  +08:00"));
    }

    @Test
    public void isValidOffset_multipleOrMisplacedMinusSigns_false() {
        assertFalse(Offset.isValidOffset("--08:00"));
        assertFalse(Offset.isValidOffset("- -08:00"));

        assertFalse(Offset.isValidOffset("-+08:00"));
        assertFalse(Offset.isValidOffset("+-08:00"));

        assertFalse(Offset.isValidOffset("08:-00"));
        assertFalse(Offset.isValidOffset("08:00-"));
        assertFalse(Offset.isValidOffset("8-00"));

        assertFalse(Offset.isValidOffset("-"));
        assertFalse(Offset.isValidOffset("-:00"));
    }

    @Test
    public void isValidOffset_outOfRealWorldRange_false() {
        // Beyond +14:00
        assertFalse(Offset.isValidOffset("+14:01"));
        assertFalse(Offset.isValidOffset("+15:00"));

        // Beyond -12:00
        assertFalse(Offset.isValidOffset("-12:01"));
        assertFalse(Offset.isValidOffset("-13:00"));

        // Borderline valid ones
        assertTrue(Offset.isValidOffset("+14:00"));
        assertTrue(Offset.isValidOffset("-12:00"));
    }

    /**
     * Tests offsets with leading/trailing whitespace.
     * Whitespace should make an offset invalid.
     */
    @Test
    public void isValidOffset_withWhitespace_false() {
        assertFalse(Offset.isValidOffset(" +08:00"));
        assertFalse(Offset.isValidOffset("+08:00 "));
        assertFalse(Offset.isValidOffset(" +08:00 "));
    }

    /**
     * Tests boundary offsets at extreme valid and invalid limits.
     */
    @Test
    public void constructor_boundaryValues_correctness() {
        // Valid lower bound
        assertEquals(new Offset("-12:00").getTotalMinutes(), -720);
        // Valid upper bound
        assertEquals(new Offset("+14:00").getTotalMinutes(), 840);
        // Invalid just beyond bounds
        assertThrows(IllegalArgumentException.class, () -> new Offset("+14:01"));
        assertThrows(IllegalArgumentException.class, () -> new Offset("-12:01"));
    }

    /**
     * Ensures Offset equality is reflexive, symmetric, and transitive.
     */
    @Test
    public void equals_contract_valid() {
        Offset a = new Offset("+07:00");
        Offset b = new Offset("+07:00");
        Offset c = new Offset("+07:00");

        // Reflexive
        assertTrue(a.equals(a));
        // Symmetric
        assertTrue(a.equals(b) && b.equals(a));
        // Transitive
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c));
    }

    /**
     * Tests toString() consistency after parsing and reconstructing Offset objects.
     */
    @Test
    public void toString_consistencyAcrossInstances_true() {
        Offset off1 = new Offset("+03:30");
        Offset off2 = new Offset(off1.toString());
        assertEquals(off1.toString(), off2.toString());
    }

    /**
     * Tests hashCode consistency across multiple calls.
     */
    @Test
    public void hashCode_idempotent_true() {
        Offset o = new Offset("+09:00");
        int h1 = o.hashCode();
        int h2 = o.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Tests invalid patterns containing letters and mixed characters.
     */
    @Test
    public void isValidOffset_mixedCharacterGarbage_false() {
        assertFalse(Offset.isValidOffset("+0A:00"));
        assertFalse(Offset.isValidOffset("+AA:BB"));
        assertFalse(Offset.isValidOffset("hello"));
        assertFalse(Offset.isValidOffset("UTC+08:00"));
    }

    /**
     * Tests offsets with zero-padded correctness for negative and positive.
     */
    @Test
    public void constructor_zeroPadded_correctParsing() {
        assertEquals(new Offset("+09:00").getTotalMinutes(), 540);
        assertEquals(new Offset("-09:00").getTotalMinutes(), -540);
    }

    /**
     * Tests offsets where minutes component is 45 — common in some regions (e.g., Nepal +05:45).
     */
    @Test
    public void constructor_validNonRoundHourOffsets_true() {
        Offset o = new Offset("+05:45");
        assertEquals(345, o.getTotalMinutes());
    }

    /**
     * Tests invalid cases where offset has too many digits in the hour or minute parts.
     */
    @Test
    public void isValidOffset_tooManyDigits_false() {
        assertFalse(Offset.isValidOffset("+123:00"));
        assertFalse(Offset.isValidOffset("+01:000"));
    }

    /**
     * Tests invalid input with missing minute digits.
     */
    @Test
    public void isValidOffset_missingMinuteDigits_false() {
        assertFalse(Offset.isValidOffset("+09:0"));
        assertFalse(Offset.isValidOffset("+09:"));
    }

    /**
     * Tests if compareTo correctly handles identical negative offsets.
     */
    @Test
    public void compareTo_identicalNegativeOffsets_zero() {
        Offset a = new Offset("-10:00");
        Offset b = new Offset("-10:00");
        assertEquals(0, a.compareTo(b));
    }

    /**
     * Tests compareTo when comparing positive and negative offsets.
     */
    @Test
    public void compareTo_positiveVsNegative_correctOrder() {
        Offset neg = new Offset("-03:00");
        Offset pos = new Offset("+03:00");
        assertTrue(neg.compareTo(pos) < 0);
    }

    /**
     * Ensures invalid input with double colons is rejected.
     */
    @Test
    public void isValidOffset_doubleColons_false() {
        assertFalse(Offset.isValidOffset("+08::00"));
        assertFalse(Offset.isValidOffset("-09::30"));
    }

    /**
     * Tests equality when one Offset has equivalent total minutes but different string representation.
     * (Should not be equal because Offset preserves string form).
     */
    @Test
    public void equals_sameMinutesDifferentSigns_false() {
        assertNotEquals(new Offset("+00:00"), new Offset("-00:00"));
    }

    /**
     * Ensures that invalid offset missing minutes (e.g., "+08") throws exception.
     */
    @Test
    public void constructor_missingMinutes_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Offset("+08"));
    }

    /**
     * Verifies that equality and hashCode remain consistent for multiple instances created in a loop.
     */
    @Test
    public void equalsAndHashCode_consistencyOverMultipleInstances_true() {
        for (int i = -12; i <= 14; i += 2) {
            String formatted = String.format("%+03d:00", i);
            Offset o1 = new Offset(formatted);
            Offset o2 = new Offset(formatted);
            assertEquals(o1, o2);
            assertEquals(o1.hashCode(), o2.hashCode());
        }
    }
}

