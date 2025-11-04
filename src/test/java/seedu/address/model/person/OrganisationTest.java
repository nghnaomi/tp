package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class OrganisationTest {

    @Test
    public void equals() {
        Organisation org = new Organisation("NUS");

        // same object -> returns true
        assertTrue(org.equals(org));

        // same values -> returns true
        Organisation orgCopy = new Organisation("NUS");
        assertTrue(org.equals(orgCopy));

        // different types -> returns false
        assertFalse(org.equals(1));

        // null -> returns false
        assertFalse(org.equals(null));

        // different organisation -> returns false
        Organisation differentOrg = new Organisation("NTU");
        assertFalse(org.equals(differentOrg));

        // empty organisation vs non-empty -> returns false
        Organisation emptyOrg = new Organisation("");
        assertFalse(org.equals(emptyOrg));

        // two empty organisations -> returns true
        Organisation anotherEmpty = new Organisation("");
        assertTrue(emptyOrg.equals(anotherEmpty));
    }

    @Test
    public void toString_returnsValue() {
        Organisation org = new Organisation("National University of Singapore");
        assertEquals("National University of Singapore", org.toString());

        Organisation emptyOrg = new Organisation("");
        assertEquals("", emptyOrg.toString());
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        Organisation org1 = new Organisation("Google");
        Organisation org2 = new Organisation("Google");
        assertEquals(org1.hashCode(), org2.hashCode());
    }

    @Test
    public void isValidOrganisation_exceedsMaxLength_returnsFalse() {
        String longOrganisation = "a".repeat(61);
        assertFalse(Organisation.isValidOrganisation(longOrganisation));
    }

    @Test
    public void isValidOrganisation_maxLengthBoundary_returnsTrue() {
        String validOrganisation = "a".repeat(60);
        assertTrue(Organisation.isValidOrganisation(validOrganisation));
    }

    /**
     * Tests that equals returns false for different capitalisations (case-sensitive comparison).
     */
    @Test
    public void equals_differentCasing_returnsFalse() {
        Organisation org1 = new Organisation("NUS");
        Organisation org2 = new Organisation("nus");
        assertFalse(org1.equals(org2));
    }

    /**
     * Tests that organisations with leading or trailing whitespace are treated as distinct.
     */
    @Test
    public void equals_whitespaceDifferences_returnsFalse() {
        Organisation org1 = new Organisation("NUS");
        Organisation org2 = new Organisation(" NUS ");
        assertFalse(org1.equals(org2));
    }

    /**
     * Tests that toString() returns the exact input string without modification.
     */
    @Test
    public void toString_exactPreservation_returnsTrue() {
        String name = "  OpenAI Research  ";
        Organisation org = new Organisation(name);
        assertEquals(name, org.toString());
    }

    /**
     * Tests that hashCode remains consistent across multiple calls.
     */
    @Test
    public void hashCode_idempotent_returnsSameValue() {
        Organisation org = new Organisation("Temasek Holdings");
        int hash1 = org.hashCode();
        int hash2 = org.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests equality contract: reflexive, symmetric, and transitive.
     */
    @Test
    public void equals_contractHolds_true() {
        Organisation a = new Organisation("Shopee");
        Organisation b = new Organisation("Shopee");
        Organisation c = new Organisation("Shopee");

        assertTrue(a.equals(a)); // reflexive
        assertTrue(a.equals(b) && b.equals(a)); // symmetric
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c)); // transitive
    }

    /**
     * Tests that different organisations produce different hash codes most of the time.
     */
    @Test
    public void hashCode_differentValues_likelyDifferentHashCodes() {
        Organisation a = new Organisation("Apple");
        Organisation b = new Organisation("Microsoft");
        assertFalse(a.hashCode() == b.hashCode());
    }

    /**
     * Tests that isValidOrganisation returns true for common company names.
     */
    @Test
    public void isValidOrganisation_commonNames_returnsTrue() {
        assertTrue(Organisation.isValidOrganisation("Google"));
        assertTrue(Organisation.isValidOrganisation("National University of Singapore"));
        assertTrue(Organisation.isValidOrganisation("Temasek Holdings Pte Ltd"));
    }

    /**
     * Tests that isValidOrganisation returns true for strings with numbers and special symbols like & or -.
     */
    @Test
    public void isValidOrganisation_symbolsAndNumbers_returnsTrue() {
        assertTrue(Organisation.isValidOrganisation("R&D Labs"));
        assertTrue(Organisation.isValidOrganisation("Tech-42 Innovations"));
        assertTrue(Organisation.isValidOrganisation("Finance & Co."));
    }

    /**
     * Tests boundary conditions: exactly 60 characters should be valid.
     */
    @Test
    public void isValidOrganisation_exactly60Characters_returnsTrue() {
        String name = "a".repeat(60);
        assertTrue(Organisation.isValidOrganisation(name));
    }

    /**
     * Tests boundary conditions: 61 characters should be invalid.
     */
    @Test
    public void isValidOrganisation_sixtyOneCharacters_returnsFalse() {
        String name = "a".repeat(61);
        assertFalse(Organisation.isValidOrganisation(name));
    }

    /**
     * Tests equals() returns false when compared with unrelated type.
     */
    @Test
    public void equals_unrelatedType_returnsFalse() {
        Organisation org = new Organisation("OpenAI");
        assertFalse(org.equals("OpenAI"));
    }

    /**
     * Tests that two different Organisation objects with empty strings are equal.
     */
    @Test
    public void equals_twoEmptyOrganisations_returnsTrue() {
        Organisation a = new Organisation("");
        Organisation b = new Organisation("");
        assertTrue(a.equals(b));
    }

    /**
     * Tests that Organisation name is case-sensitive and not normalised automatically.
     */
    @Test
    public void toString_caseSensitivity_preserved() {
        Organisation org = new Organisation("nUs");
        assertEquals("nUs", org.toString());
    }

    /**
     * Tests that toString() for empty organisation returns an empty string.
     */
    @Test
    public void toString_emptyOrganisation_returnsEmptyString() {
        Organisation org = new Organisation("");
        assertEquals("", org.toString());
    }

    /**
     * Tests that equality and hashCode remain consistent across multiple randomly generated valid names.
     */
    @Test
    public void equalsAndHashCode_consistencyOverMultipleInstances_true() {
        for (int i = 0; i < 10; i++) {
            String name = "Org" + i;
            Organisation org1 = new Organisation(name);
            Organisation org2 = new Organisation(name);
            assertTrue(org1.equals(org2));
            assertEquals(org1.hashCode(), org2.hashCode());
        }
    }
}
