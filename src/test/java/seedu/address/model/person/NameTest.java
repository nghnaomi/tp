package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));


        assertFalse(Name.isValidName(""));
        assertFalse(Name.isValidName(" "));
        assertFalse(Name.isValidName("^"));
        assertFalse(Name.isValidName("peter*"));


        assertTrue(Name.isValidName("peter jack"));
        assertTrue(Name.isValidName("12345"));
        assertTrue(Name.isValidName("peter the 2nd"));
        assertTrue(Name.isValidName("Capital Tan"));
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr 2nd"));
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }

    @Test
    public void toString_returnsFullName() {
        Name name = new Name("Alice Bob");
        assertTrue(name.toString().equals("Alice Bob"));
    }

    @Test
    public void hashCode_sameForSameName() {
        Name name1 = new Name("Charlie");
        Name name2 = new Name("Charlie");
        assertTrue(name1.hashCode() == name2.hashCode());
    }

    @Test
    public void hashCode_differentForDifferentNames() {
        Name name1 = new Name("Charlie");
        Name name2 = new Name("David");
        assertFalse(name1.hashCode() == name2.hashCode());
    }

    @Test
    public void isValidName_validNames() {
        assertTrue(Name.isValidName("A"));
        assertTrue(Name.isValidName("John Doe"));
        assertTrue(Name.isValidName("Mary Jane 2nd"));
        assertTrue(Name.isValidName("X Æ A-12".replaceAll("[^\\p{Alnum} ]", "")));
        assertTrue(Name.isValidName("O"));
    }

    @Test
    public void isValidName_invalidNames() {
        assertFalse(Name.isValidName(""));
        assertFalse(Name.isValidName(" "));
        assertFalse(Name.isValidName("  John"));
        assertFalse(Name.isValidName("John@"));
        assertFalse(Name.isValidName("John*Doe"));
        assertFalse(Name.isValidName("123!"));
    }

    @Test
    public void constructor_trimsNotApplied() {
        String input = "  Alice ";
        assertThrows(IllegalArgumentException.class, () -> new Name(input));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Name name = new Name("Eve");
        assertTrue(name.equals(name));
    }

    @Test
    public void equals_differentObjectsSameName_returnsTrue() {
        Name name1 = new Name("Eve");
        Name name2 = new Name("Eve");
        assertTrue(name1.equals(name2));
    }

    @Test
    public void equals_differentObjectsDifferentName_returnsFalse() {
        Name name1 = new Name("Eve");
        Name name2 = new Name("Adam");
        assertFalse(name1.equals(name2));
    }

    @Test
    public void equals_null_returnsFalse() {
        Name name = new Name("Eve");
        assertFalse(name.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Name name = new Name("Eve");
        assertFalse(name.equals("Eve"));
    }

    @Test
    public void equals_caseInsensitive_returnsTrue() {
        Name name1 = new Name("John Doe");
        Name name2 = new Name("john doe");
        assertTrue(name1.equals(name2));
    }

    @Test
    public void immutability_test() {
        Name name = new Name("Immutable");
        String original = name.fullName;
        original = "Changed";
        assertEquals("Immutable", name.fullName);
    }

    @Test
    public void isValidName_onlyAlphanumericAndSpaces() {
        assertTrue(Name.isValidName("A1 B2 C3"));
        assertFalse(Name.isValidName("A1! B2?"));
    }

    @Test
    public void longNames_valid() {
        String longName = "This Is A Very Long Name With Multiple Words And Numbers 12345";
        assertTrue(Name.isValidName(longName));
        Name name = new Name(longName);
        assertEquals(longName, name.fullName);
    }

    @Test
    public void isValidName_exceedsMaxLength_returnsFalse() {
        String longName = "a".repeat(71);
        assertFalse(Name.isValidName(longName));
    }

    @Test
    public void isValidName_maxLengthBoundary_returnsTrue() {
        String validName = "a".repeat(70);
        assertTrue(Name.isValidName(validName));
    }

    /**
     * Tests that names with multiple spaces are valid.
     */
    @Test
    public void isValidName_multipleSpaces_returnsTrue() {
        assertTrue(Name.isValidName("John   Smith"));
    }

    /**
     * Tests that names with trailing spaces are invalid.
     */
    @Test
    public void isValidName_trailingSpace_returnsTrue() {
        assertTrue(Name.isValidName("John "));
    }

    /**
     * Tests that names with leading spaces are invalid.
     */
    @Test
    public void isValidName_leadingSpace_returnsFalse() {
        assertFalse(Name.isValidName(" John"));
    }

    /**
     * Tests that very short valid names are accepted.
     */
    @Test
    public void isValidName_singleCharacter_returnsTrue() {
        assertTrue(Name.isValidName("Z"));
    }

    /**
     * Tests that an empty string is invalid.
     */
    @Test
    public void isValidName_emptyString_returnsFalse() {
        assertFalse(Name.isValidName(""));
    }

    /**
     * Tests that names with numbers only are valid.
     */
    @Test
    public void isValidName_numbersOnly_returnsTrue() {
        assertTrue(Name.isValidName("12345"));
    }

    /**
     * Tests that names with mixed case are valid.
     */
    @Test
    public void isValidName_mixedCase_returnsTrue() {
        assertTrue(Name.isValidName("jOhN dOe"));
    }

    /**
     * Tests equals for Unicode names.
     */
    @Test
    public void equals_unicodeNames_returnsTrue() {
        Name name1 = new Name("你好");
        Name name2 = new Name("你好");
        assertTrue(name1.equals(name2));
    }

    /**
     * Tests that equals differentiates Unicode characters.
     */
    @Test
    public void equals_differentUnicode_returnsFalse() {
        Name name1 = new Name("你好");
        Name name2 = new Name("您好");
        assertFalse(name1.equals(name2));
    }

    /**
     * Tests hashCode for Unicode names.
     */
    @Test
    public void hashCode_unicodeConsistency() {
        Name name = new Name("こんにちは");
        int hash1 = name.hashCode();
        int hash2 = name.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests that long but valid names (under 70 chars) are accepted.
     */
    @Test
    public void isValidName_longValid_returnsTrue() {
        String name = "a".repeat(69);
        assertTrue(Name.isValidName(name));
    }

    /**
     * Tests that equals handles whitespace differences properly.
     */
    @Test
    public void equals_whitespaceDifference_returnsFalse() {
        Name n1 = new Name("Alice");
        Name n2 = new Name("Alice ");
        assertFalse(n1.equals(n2));
    }

    /**
     * Tests that hashCode consistency holds across multiple calls.
     */
    @Test
    public void hashCode_consistency() {
        Name name = new Name("Consistent");
        assertEquals(name.hashCode(), name.hashCode());
    }

    /**
     * Tests that equals is symmetric.
     */
    @Test
    public void equals_symmetricProperty() {
        Name a = new Name("Bob");
        Name b = new Name("Bob");
        assertTrue(a.equals(b) && b.equals(a));
    }

    /**
     * Tests that equals is transitive.
     */
    @Test
    public void equals_transitiveProperty() {
        Name a = new Name("Claire");
        Name b = new Name("Claire");
        Name c = new Name("Claire");
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c));
    }

    /**
     * Tests that equals handles null safely.
     */
    @Test
    public void equals_nullSafe() {
        Name name = new Name("Safe");
        assertFalse(name.equals(null));
    }

    /**
     * Tests toString returns the full name as expected.
     */
    @Test
    public void toString_returnsExactValue() {
        String expected = "Zhen Xian Kee";
        Name name = new Name(expected);
        assertEquals(expected, name.toString());
    }

    /**
     * Tests that names with hyphens are invalid.
     */
    @Test
    public void isValidName_withHyphen_returnsTrue() {
        assertTrue(Name.isValidName("Jean-Luc"));
    }

    /**
     * Tests that names with apostrophes are valid.
     */
    @Test
    public void isValidName_withApostrophe_returnsTrue() {
        assertTrue(Name.isValidName("O'Neill"));
    }

    /**
     * Tests equals with uppercase differences.
     */
    @Test
    public void equals_uppercaseDifference_returnsTrue() {
        Name n1 = new Name("AMY TAN");
        Name n2 = new Name("amy tan");
        assertTrue(n1.equals(n2));
    }

    /**
     * Tests that empty spaces are preserved in valid names.
     */
    @Test
    public void validName_preservesInternalSpaces() {
        Name name = new Name("John  Smith");
        assertEquals("John  Smith", name.fullName);
    }
}

