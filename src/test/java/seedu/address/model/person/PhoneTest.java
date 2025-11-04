package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("91")); // less than 3 numbers
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits

        // valid phone numbers
        assertTrue(Phone.isValidPhone("911")); // exactly 3 numbers
        assertTrue(Phone.isValidPhone("93121534"));
        assertTrue(Phone.isValidPhone("124293842033123")); // long phone numbers
        assertTrue(Phone.isValidPhone("9312 1534")); // spaces within digits
    }

    @Test
    public void countryDetection_validNumbers_success() {
        assertEquals("65", new Phone("+6598765432").getCountryCode());
        assertEquals("1", new Phone("+14155552671").getCountryCode());
        assertEquals("91", new Phone("+919876543210").getCountryCode());
    }

    @Test
    public void toString_formatsCorrectly() {
        assertEquals("+6598765432 (65)", new Phone("+6598765432").toString());
    }

    @Test
    public void equals() {
        Phone phone = new Phone("999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("995")));
    }
    @Test
    public void isValidPhone_minLength() {
        assertFalse(Phone.isValidPhone("12")); // less than 3 digits
        assertTrue(Phone.isValidPhone("123")); // exactly 3 digits
    }

    @Test
    public void isValidPhone_onlyNumbers() {
        assertTrue(Phone.isValidPhone("1234567890"));
        assertTrue(Phone.isValidPhone("999"));
    }

    @Test
    public void isValidPhone_withSpacesAndDashes() {
        assertTrue(Phone.isValidPhone("123-456-7890"));
        assertTrue(Phone.isValidPhone("123 456 7890"));
        assertTrue(Phone.isValidPhone("+1 123-456-7890"));
    }

    @Test
    public void isValidPhone_withParentheses() {
        assertTrue(Phone.isValidPhone("(123) 456-7890"));
        assertTrue(Phone.isValidPhone("+65 (123) 456 7890"));
    }

    @Test
    public void isValidPhone_invalidCharacters() {
        assertFalse(Phone.isValidPhone("123A456"));
        assertFalse(Phone.isValidPhone("+65@12345678"));
        assertFalse(Phone.isValidPhone("phone123"));
    }

    @Test
    public void isValidPhone_leadingPlus() {
        assertTrue(Phone.isValidPhone("+6598765432"));
        assertTrue(Phone.isValidPhone("+14155552671"));
        assertFalse(Phone.isValidPhone("+12")); // less than 3 digits
    }

    @Test
    public void isValidPhone_emptyString() {
        assertFalse(Phone.isValidPhone(""));
    }

    @Test
    public void constructor_validPhone_numbers() {
        Phone phone1 = new Phone("911");
        assertEquals("911", phone1.value);
        Phone phone2 = new Phone("+6598765432");
        assertEquals("+6598765432", phone2.value);
    }

    @Test
    public void constructor_invalidPhone_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Phone(""));
        assertThrows(IllegalArgumentException.class, () -> new Phone("12"));
        assertThrows(IllegalArgumentException.class, () -> new Phone("phone123"));
    }

    @Test
    public void countryCode_derivation_validNumbers() {
        assertEquals("65", new Phone("+6598765432").getCountryCode());
        assertEquals("1", new Phone("+14155552671").getCountryCode());
        assertEquals("91", new Phone("+919876543210").getCountryCode());
    }

    @Test
    public void countryDetection_longerPrefixes_success() {
        // Finland (+358), not "+35"
        assertEquals("358", new Phone("+358401234567").getCountryCode());
        assertEquals("358", new Phone("+358 40 123 4567").getCountryCode());

        // UAE (+971)
        assertEquals("971", new Phone("+971501234567").getCountryCode());

        // UK (+44)
        assertEquals("44", new Phone("+447911123456").getCountryCode());

        // Japan (+81)
        assertEquals("81", new Phone("+81-90-1234-5678").getCountryCode());

        // Australia (+61)
        assertEquals("61", new Phone("+61 (4) 1234 5678").getCountryCode());

        // Germany (+49)
        assertEquals("49", new Phone("+49 1512 3456789").getCountryCode());
    }

    @Test
    public void countryDetection_withSeparators_success() {
        // Spaces and dashes
        assertEquals("65", new Phone("+65 9876-5432").getCountryCode());

        // Parentheses around area parts (should still detect +65)
        assertEquals("65", new Phone("+65 (123) 456 7890").getCountryCode());

        // Mixed separators with NANP (+1)
        assertEquals("1", new Phone("+1 (415) 555-2671").getCountryCode());

        // Trunk (0) often included domestically; must not affect +44
        assertEquals("44", new Phone("+44 (0)20 7946 0958").getCountryCode());
    }

    @Test
    public void leadingPlus_edgeCases() {
        // Plus but too short overall
        assertFalse(Phone.isValidPhone("+6"));
        assertFalse(Phone.isValidPhone("+65"));

        // Non-digit right after plus
        assertFalse(Phone.isValidPhone("+A123456"));
        assertFalse(Phone.isValidPhone("++6512345678"));
        assertFalse(Phone.isValidPhone("+-6512345678"));

        // Valid once enough digits exist
        assertTrue(Phone.isValidPhone("+65 9"));
        assertTrue(Phone.isValidPhone("+65 98"));
        assertTrue(Phone.isValidPhone("+65 987"));
    }

    @Test
    public void countryDetection_equivalentFormats_sameResult() {
        String[] formats = new String[] {
            "+6598765432",
            "+65 9876 5432",
            "+65-9876-5432",
            "+65 (9876) 5432",
            "+65 (0) 9876 5432"
        };

        for (String fmt : formats) {
            assertEquals("65", new Phone(fmt).getCountryCode(), "Failed for format: " + fmt);
        }
    }

    @Test
    public void toString_withVariousFormats_showsDetectedCode() {
        assertEquals("+65 9876-5432 (65)", new Phone("+65 9876-5432").toString());
        assertEquals("+1 (415) 555-2671 (1)", new Phone("+1 (415) 555-2671").toString());
        assertEquals("+44 7911 123456 (44)", new Phone("+44 7911 123456").toString());
        assertEquals("+358 40 123 4567 (358)", new Phone("+358 40 123 4567").toString());
    }

    @Test
    public void countryDetection_nanp_staysOne() {
        assertEquals("1", new Phone("+12015550123").getCountryCode()); // US DC area code 201 is just NANP detail
        assertEquals("1", new Phone("+16175550123").getCountryCode()); // US Boston
        assertEquals("1", new Phone("+19025550123").getCountryCode()); // US Memphis
    }

    @Test
    public void countryDetection_variety_sanity() {
        assertEquals("33", new Phone("+33 6 12 34 56 78").getCountryCode()); // France
        assertEquals("34", new Phone("+34 612 34 56 78").getCountryCode()); // Spain
        assertEquals("39", new Phone("+39 347 123 4567").getCountryCode()); // Italy
        assertEquals("52", new Phone("+52 55 1234 5678").getCountryCode()); // Mexico
        assertEquals("62", new Phone("+62 812-1234-5678").getCountryCode()); // Indonesia
        assertEquals("82", new Phone("+82 10-1234-5678").getCountryCode()); // South Korea
    }

    @Test
    public void equals_sameValue() {
        Phone phone1 = new Phone("999");
        Phone phone2 = new Phone("999");
        assertTrue(phone1.equals(phone2));
    }

    @Test
    public void equals_sameObject() {
        Phone phone = new Phone("999");
        assertTrue(phone.equals(phone));
    }

    @Test
    public void equals_nullAndDifferentType() {
        Phone phone = new Phone("999");
        assertFalse(phone.equals(null));
        assertFalse(phone.equals("999"));
        assertFalse(phone.equals(123));
    }

    @Test
    public void equals_differentValues() {
        Phone phone1 = new Phone("999");
        Phone phone2 = new Phone("995");
        assertFalse(phone1.equals(phone2));
    }

    @Test
    public void hashCode_consistency() {
        Phone phone = new Phone("+6598765432");
        int first = phone.hashCode();
        int second = phone.hashCode();
        assertEquals(first, second);
    }

    @Test
    public void hashCode_differentPhones() {
        Phone phone1 = new Phone("+6598765432");
        Phone phone2 = new Phone("+14155552671");
        assertFalse(phone1.hashCode() == phone2.hashCode());
    }

    @Test
    public void immutability_testValueField() {
        Phone phone = new Phone("911");
        String copy = phone.value;
        copy = "12345";
        assertEquals("911", phone.value);
    }

    @Test
    public void immutability_testCountryCodeField() {
        Phone phone = new Phone("+6598765432");
        String copy = phone.countryCode;
        copy = "99";
        assertEquals("65", phone.countryCode);
    }

    @Test
    public void multipleValidPhones() {
        String[] validNumbers = { "911", "93121534", "+14155552671", "+919876543210", "124293842033123" };
        for (String number : validNumbers) {
            Phone phone = new Phone(number);
            assertTrue(Phone.isValidPhone(number));
            assertNotNull(phone.getCountryCode());
        }
    }

    @Test
    public void repeatedToStringConsistency() {
        Phone phone = new Phone("+6598765432");
        String first = phone.toString();
        String second = phone.toString();
        assertEquals(first, second);
    }

    @Test
    public void repeatedEqualsConsistency() {
        Phone phone1 = new Phone("911");
        Phone phone2 = new Phone("911");
        assertTrue(phone1.equals(phone2));
        assertTrue(phone1.equals(phone2));
    }

    @Test
    public void repeatedHashCodeConsistency() {
        Phone phone = new Phone("911");
        int first = phone.hashCode();
        int second = phone.hashCode();
        int third = phone.hashCode();
        assertEquals(first, second);
        assertEquals(second, third);
    }
    @Test
    public void isValidPhone_multipleLeadingSigns() {
        assertFalse(Phone.isValidPhone("++6598765432"));
        assertFalse(Phone.isValidPhone("+-6598765432"));
        assertFalse(Phone.isValidPhone("+--1234567"));

        assertTrue(Phone.isValidPhone("+6598765432"));
        assertTrue(Phone.isValidPhone("+1 123-456-7890"));
    }

    @Test
    public void isValidPhone_plusInsideNumber_invalid() {
        assertFalse(Phone.isValidPhone("123+456789"));
        assertFalse(Phone.isValidPhone("12+34+56"));
        assertFalse(Phone.isValidPhone("123456+"));
    }

    @Test
    public void isValidPhone_onlyDigitsAndAllowedSymbols() {
        assertTrue(Phone.isValidPhone("1234567890"));
        assertTrue(Phone.isValidPhone("123 456 7890"));
        assertTrue(Phone.isValidPhone("(123) 456-7890"));

        assertFalse(Phone.isValidPhone("123*456"));
        assertFalse(Phone.isValidPhone("123#456"));
        assertFalse(Phone.isValidPhone("123@456"));
    }

    @Test
    public void constructor_invalidPhoneMultipleSigns_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Phone("++6598765432"));
        assertThrows(IllegalArgumentException.class, () -> new Phone("+-6598765432"));
    }

    @Test
    public void constructor_invalidPhonePlusInside_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Phone("123+456789"));
    }

    @Test
    public void isValidPhone_maxLength() {
        // 15 digits -> valid
        assertTrue(Phone.isValidPhone("123456789012345"));
        assertTrue(Phone.isValidPhone("+651234567890123")); // including country code

        // 16 digits -> invalid
        assertFalse(Phone.isValidPhone("1234567890123456"));
        assertFalse(Phone.isValidPhone("+6512345678901234")); // including country code
    }

    @Test
    public void constructor_invalidPhone_exceedsMaxLength() {
        // 16 digits should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> new Phone("1234567890123456"));
        assertThrows(IllegalArgumentException.class, () -> new Phone("+6512345678901234"));
    }

    @Test
    public void constructor_validPhone_maxLength() {
        // 15 digits should be allowed
        Phone phone1 = new Phone("123456789012345");
        assertEquals("123456789012345", phone1.value);

        Phone phone2 = new Phone("+651234567890123");
        assertEquals("+651234567890123", phone2.value);
    }

    /**
     * Tests invalid phone numbers containing multiple plus signs within the number.
     */
    @Test
    public void isValidPhone_multiplePluses_invalid() {
        assertFalse(Phone.isValidPhone("+65+98765432"));
        assertFalse(Phone.isValidPhone("123+456+789"));
    }

    /**
     * Tests various non-standard separators like slashes or underscores.
     */
    @Test
    public void isValidPhone_withNonStandardSeparators_invalid() {
        assertFalse(Phone.isValidPhone("123/456/7890"));
        assertFalse(Phone.isValidPhone("123_456_7890"));
        assertFalse(Phone.isValidPhone("+65_9876_5432"));
    }

    /**
     * Tests phone numbers with mixed symbols and digits.
     */
    @Test
    public void isValidPhone_withMixedSymbols_invalid() {
        assertFalse(Phone.isValidPhone("+65-98@76#5432"));
        assertFalse(Phone.isValidPhone("123 456 * 7890"));
    }

    /**
     * Tests multiple spaces between digits.
     */
    @Test
    public void isValidPhone_multipleSpaces_valid() {
        assertTrue(Phone.isValidPhone("123  456  7890"));
        assertTrue(Phone.isValidPhone("+65  9876  5432"));
    }

    /**
     * Tests phones with mixed dash and space separators.
     */
    @Test
    public void isValidPhone_mixedSeparators_valid() {
        assertTrue(Phone.isValidPhone("+65 98-76 5432"));
        assertTrue(Phone.isValidPhone("123-456 7890"));
    }

    /**
     * Tests country code detection for Scandinavian numbers.
     */
    @Test
    public void countryDetection_scandinavianNumbers_success() {
        assertEquals("46", new Phone("+46701234567").getCountryCode()); // Sweden
        assertEquals("47", new Phone("+4790123456").getCountryCode()); // Norway
        assertEquals("45", new Phone("+4520123456").getCountryCode()); // Denmark
    }

    /**
     * Tests that equals() returns false for different formatting but different value.
     */
    @Test
    public void equals_differentNumber_returnsFalse() {
        Phone phone1 = new Phone("+6598765432");
        Phone phone2 = new Phone("+6598765433");
        assertFalse(phone1.equals(phone2));
    }

    /**
     * Tests immutability of countryCode field even after toString is called.
     */
    @Test
    public void immutability_countryCode_afterToString() {
        Phone phone = new Phone("+6598765432");
        phone.toString();
        assertEquals("65", phone.countryCode);
    }

    /**
     * Tests consistency between toString() and value.
     */
    @Test
    public void toString_matchesStoredValue() {
        Phone phone = new Phone("+6598765432");
        assertTrue(phone.toString().contains(phone.value));
    }

    /**
     * Tests very long input containing valid but spaced-out digits.
     */
    @Test
    public void isValidPhone_longWithSpaces_valid() {
        assertTrue(Phone.isValidPhone("1234 5678 9012 345"));
        assertFalse(Phone.isValidPhone("1234 5678 9012 3456"));
    }

    /**
     * Tests behaviour for empty country code symbol "+" only.
     */
    @Test
    public void constructor_plusOnly_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Phone("+"));
    }

    /**
     * Tests behaviour for phone number with letters after valid digits.
     */
    @Test
    public void isValidPhone_digitsThenLetters_invalid() {
        assertFalse(Phone.isValidPhone("123456A"));
        assertFalse(Phone.isValidPhone("+65123456b"));
    }

    /**
     * Tests behaviour for phone numbers containing punctuation marks.
     */
    @Test
    public void isValidPhone_withPunctuation_invalid() {
        assertFalse(Phone.isValidPhone("123,456,7890"));
        assertFalse(Phone.isValidPhone("123!456?7890"));
    }

    /**
     * Tests multiple country codes (invalid scenario).
     */
    @Test
    public void isValidPhone_multipleCountryCodes_invalid() {
        assertFalse(Phone.isValidPhone("+65+44 98765432"));
    }

    /**
     * Tests numbers starting with 0 and no plus sign (domestic format).
     */
    @Test
    public void isValidPhone_domesticLeadingZero_valid() {
        assertTrue(Phone.isValidPhone("0123456789"));
        assertTrue(Phone.isValidPhone("08 1234 5678"));
    }

    /**
     * Tests numbers starting with 00 (common international prefix without +).
     */
    @Test
    public void isValidPhone_doubleZeroPrefix_valid() {
        assertTrue(Phone.isValidPhone("006598765432"));
        assertTrue(Phone.isValidPhone("00441234567890"));
    }

    /**
     * Tests country code detection for smaller nations.
     */
    @Test
    public void countryDetection_smallNations_success() {
        assertEquals("64", new Phone("+64211234567").getCountryCode()); // New Zealand
        assertEquals("353", new Phone("+353871234567").getCountryCode()); // Ireland
        assertEquals("41", new Phone("+41791234567").getCountryCode()); // Switzerland
    }

    /**
     * Tests invalid characters at the start of a number.
     */
    @Test
    public void isValidPhone_invalidStartingCharacters() {
        assertFalse(Phone.isValidPhone("*1234567"));
        assertFalse(Phone.isValidPhone("#98765432"));
        assertFalse(Phone.isValidPhone("@6598765432"));
    }

    /**
     * Tests that value field remains unchanged after multiple hashCode calls.
     */
    @Test
    public void immutability_valueAfterHashCode() {
        Phone phone = new Phone("911");
        int hash = phone.hashCode();
        assertEquals("911", phone.value);
    }

    /**
     * Tests that getCountryCode returns non-null for valid plus-prefixed numbers.
     */
    @Test
    public void getCountryCode_nonNullForPlusPrefixed() {
        assertNotNull(new Phone("+6598765432").getCountryCode());
        assertNotNull(new Phone("+14155552671").getCountryCode());
    }

    /**
     * Tests equality across repeated instances of same string.
     */
    @Test
    public void equals_repeatedInstances_true() {
        for (int i = 0; i < 10; i++) {
            assertTrue(new Phone("+6598765432").equals(new Phone("+6598765432")));
        }
    }

    /**
     * Tests that equals is reflexive and symmetric for valid numbers.
     */
    @Test
    public void equals_reflexiveSymmetric_validNumbers() {
        Phone p1 = new Phone("+6598765432");
        Phone p2 = new Phone("+6598765432");
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
        assertTrue(p1.equals(p1));
    }

    /**
     * Tests that equals handles case-insensitive scenarios gracefully.
     */
    @Test
    public void equals_caseInsensitive_sameResult() {
        Phone p1 = new Phone("+6598765432");
        Phone p2 = new Phone("+6598765432");
        assertTrue(p1.equals(p2));
    }

    /**
     * Tests phone numbers that are extremely short and invalid.
     */
    @Test
    public void isValidPhone_extremelyShort_invalid() {
        assertFalse(Phone.isValidPhone("1"));
        assertFalse(Phone.isValidPhone("9"));
        assertFalse(Phone.isValidPhone("0"));
    }

    /**
     * Tests random long but valid-looking numbers.
     */
    @Test
    public void isValidPhone_randomLong_valid() {
        assertTrue(Phone.isValidPhone("+112345678901234"));
        assertTrue(Phone.isValidPhone("999999999999999"));
    }

    /**
     * Tests random long invalid numbers (exceeding 15 digits).
     */
    @Test
    public void isValidPhone_randomTooLong_invalid() {
        assertFalse(Phone.isValidPhone("+12345678901234567"));
        assertFalse(Phone.isValidPhone("999999999999999999"));
    }
}
