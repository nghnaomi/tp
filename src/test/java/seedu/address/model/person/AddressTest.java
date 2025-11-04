package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }

    @Test
    public void isValidAddress() {
        // null address
        assertThrows(NullPointerException.class, () -> Address.isValidAddress(null));

        // invalid addresses
        assertFalse(Address.isValidAddress("")); // empty string
        assertFalse(Address.isValidAddress(" ")); // spaces only

        // valid addresses
        assertTrue(Address.isValidAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Address.isValidAddress("-")); // one character
        assertTrue(Address.isValidAddress("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long address
    }

    @Test
    public void isValidAddress_exceedsMaxLength_returnsFalse() {
        String longAddress = "a".repeat(256);
        assertFalse(Address.isValidAddress(longAddress));
    }

    @Test
    public void isValidAddress_maxLengthBoundary_returnsTrue() {
        String validAddress = "a".repeat(255);
        assertTrue(Address.isValidAddress(validAddress));
    }

    @Test
    public void isValidAddress_withSpecialCharacters() {
        assertTrue(Address.isValidAddress("123 Main St., #04-567, Apt. 9!"));
        assertTrue(Address.isValidAddress("Baker Street; London; UK"));
        assertTrue(Address.isValidAddress("Rue de l'Université, Paris, France"));
        assertTrue(Address.isValidAddress("北京市朝阳区建国路88号")); // non-Latin characters
    }

    @Test
    public void isValidAddress_onlySymbols() {
        assertTrue(Address.isValidAddress("@#$%^&*()_+")); // symbols only allowed
        assertTrue(Address.isValidAddress("-.,;:/")); // punctuation
    }

    @Test
    public void equals() {
        Address address = new Address("Valid Address");

        // same values -> returns true
        assertTrue(address.equals(new Address("Valid Address")));

        // same object -> returns true
        assertTrue(address.equals(address));

        // null -> returns false
        assertFalse(address.equals(null));

        // different types -> returns false
        assertFalse(address.equals(5.0f));

        // different values -> returns false
        assertFalse(address.equals(new Address("Other Valid Address")));
    }

    @Test
    public void equals_edgeCases() {
        Address address1 = new Address("123 Main St.");
        Address address2 = new Address("123 main st.");
        Address address3 = new Address("123 Main St.");

        // Addresses should be case-sensitive
        assertFalse(address1.equals(address2));
        // Same exact address
        assertTrue(address1.equals(address3));
    }

    @Test
    public void immutability_test() {
        Address address = new Address("Original Address");
        String copy = address.value;
        copy = "Changed Address";
        assertTrue(address.value.equals("Original Address"));
    }

    @Test
    public void isValidAddress_variousValidFormats() {
        String[] validAddresses = {
            "1000 Broadway Ave, New York, NY",
            "Apartment #42, 12 Elm Street",
            "PO Box 12345",
            "Bldg. 9, Industrial Park, Tokyo",
            "Via Roma 50, 00100 Rome, Italy",
            "Calle 123, Ciudad de México, México"
        };

        for (String addr : validAddresses) {
            assertTrue(Address.isValidAddress(addr), "Failed for valid address: " + addr);
        }
    }

    @Test
    public void isValidAddress_variousInvalidFormats() {
        String[] invalidAddresses = {
            "", // empty
            " ", // spaces only
            "\t", // tab character
            "\n", // newline
            "   ", // multiple spaces
        };

        for (String addr : invalidAddresses) {
            assertFalse(Address.isValidAddress(addr), "Failed for invalid address: " + addr);
        }
    }
}
