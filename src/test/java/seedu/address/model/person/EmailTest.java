package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // blank email
        assertFalse(Email.isValidEmail("")); // empty string
        assertFalse(Email.isValidEmail(" ")); // spaces only

        // missing parts
        assertFalse(Email.isValidEmail("@example.com")); // missing local part
        assertFalse(Email.isValidEmail("peterjackexample.com")); // missing '@' symbol
        assertFalse(Email.isValidEmail("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Email.isValidEmail("peterjack@-")); // invalid domain name
        assertFalse(Email.isValidEmail("peterjack@exam_ple.com")); // underscore in domain name
        assertFalse(Email.isValidEmail("peter jack@example.com")); // spaces in local part
        assertFalse(Email.isValidEmail("peterjack@exam ple.com")); // spaces in domain name
        assertFalse(Email.isValidEmail(" peterjack@example.com")); // leading space
        assertFalse(Email.isValidEmail("peterjack@example.com ")); // trailing space
        assertFalse(Email.isValidEmail("peterjack@@example.com")); // double '@' symbol
        assertFalse(Email.isValidEmail("peter@jack@example.com")); // '@' symbol in local part
        assertFalse(Email.isValidEmail("-peterjack@example.com")); // local part starts with a hyphen
        assertFalse(Email.isValidEmail("peterjack-@example.com")); // local part ends with a hyphen
        assertFalse(Email.isValidEmail("peterjack@example@com")); // '@' symbol in domain name
        assertFalse(Email.isValidEmail("peterjack@.example.com")); // domain name starts with a period
        assertFalse(Email.isValidEmail("peterjack@example.com.")); // domain name ends with a period
        assertFalse(Email.isValidEmail("peterjack@-example.com")); // domain name starts with a hyphen
        assertFalse(Email.isValidEmail("peterjack@example.com-")); // domain name ends with a hyphen
        assertFalse(Email.isValidEmail("peterjack@example.c")); // top level domain has less than two chars

        // valid email
        assertTrue(Email.isValidEmail("PeterJack_1190@example.com")); // underscore in local part
        assertTrue(Email.isValidEmail("PeterJack.1190@example.com")); // period in local part
        assertTrue(Email.isValidEmail("PeterJack+1190@example.com")); // '+' symbol in local part
        assertTrue(Email.isValidEmail("PeterJack-1190@example.com")); // hyphen in local part
        assertTrue(Email.isValidEmail("a@bc")); // minimal
        assertTrue(Email.isValidEmail("test@localhost")); // alphabets only
        assertTrue(Email.isValidEmail("123@145")); // numeric local part and domain name
        assertTrue(Email.isValidEmail("a1+be.d@example1.com")); // mixture of alphanumeric and special characters
        assertTrue(Email.isValidEmail("peter_jack@very-very-very-long-example.com")); // long domain name
        assertTrue(Email.isValidEmail("if.you.dream.it_you.can.do.it@example.com")); // long local part
        assertTrue(Email.isValidEmail("e1234567@u.nus.edu")); // more than one period in domain
    }

    @Test
    public void equals() {
        Email email = new Email("valid@email");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@email")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other.valid@email")));
    }

    @Test
    public void isValidEmail_exceedsMaxLength_returnsFalse() {
        String longEmail = "a".repeat(245) + "@gmail.com";
        assertFalse(Email.isValidEmail(longEmail));
    }

    @Test
    public void isValidEmail_maxLengthBoundary_returnsTrue() {
        String validEmail = "a".repeat(244) + "@gmail.com";
        assertTrue(Email.isValidEmail(validEmail));
    }

    @Test
    public void isValidEmail_localPartEdgeCases() {
        // Local part starting/ending with period
        assertFalse(Email.isValidEmail(".alice@example.com"));
        assertFalse(Email.isValidEmail("alice.@example.com"));
        // Multiple consecutive periods
        assertFalse(Email.isValidEmail("alice..bob@example.com"));
        // Valid hyphens in local part
        assertTrue(Email.isValidEmail("alice-bob@example.com"));
        // Valid underscores in local part
        assertTrue(Email.isValidEmail("alice_bob@example.com"));
        // Valid plus sign
        assertTrue(Email.isValidEmail("alice+bob@example.com"));
    }

    @Test
    public void isValidEmail_domainEdgeCases() {
        // Domain starting/ending with hyphen
        assertFalse(Email.isValidEmail("alice@-example.com"));
        assertFalse(Email.isValidEmail("alice@example-.com"));
        // Domain starting/ending with period
        assertFalse(Email.isValidEmail("alice@.example.com"));
        assertFalse(Email.isValidEmail("alice@example.com."));
        // Domain with consecutive periods
        assertFalse(Email.isValidEmail("alice@ex..ample.com"));
        // Valid long domain
        assertTrue(Email.isValidEmail("alice@example-subdomain.example.com"));
        // Numeric domain allowed
        assertTrue(Email.isValidEmail("alice@123.com"));
    }

    @Test
    public void isValidEmail_whitespaceAndSpecialCases() {
        assertFalse(Email.isValidEmail(" alice@example.com"));
        assertFalse(Email.isValidEmail("alice@example.com "));
        assertFalse(Email.isValidEmail("ali ce@example.com"));
        assertFalse(Email.isValidEmail("alice@exam ple.com"));
        assertFalse(Email.isValidEmail("alice@exam\tple.com")); // tab character
        assertFalse(Email.isValidEmail("alice@\nexample.com")); // newline in domain
    }

    @Test
    public void equals_differentEmails() {
        Email email1 = new Email("alice@example.com");
        Email email2 = new Email("bob@example.com");
        Email email3 = new Email("alice@example.com");
        assertTrue(email1.equals(email3));
        assertFalse(email1.equals(email2));
    }

    @Test
    public void isValidEmail_internationalEmails() {
        // International domain names (punycode) are not valid
        assertFalse(Email.isValidEmail("user@xn--example-9ta.com"));
        // Unicode characters in local part are not allowed in most cases
        assertTrue(Email.isValidEmail("üser@example.com"));
    }

    @Test
    public void isValidEmail_multipleAtSymbols() {
        assertFalse(Email.isValidEmail("alice@@example.com"));
        assertFalse(Email.isValidEmail("alice@exam@ple.com"));
    }

    @Test
    public void isValidEmail_maxLocalAndDomainLengths() {
        // Local part max 64, domain part max 255
        String local = "a".repeat(64);
        String domain = "b".repeat(63) + ".com"; // total < 255
        assertTrue(Email.isValidEmail(local + "@" + domain));
        // Exceeding domain length
        String tooLongDomain = "b".repeat(256) + ".com";
        assertFalse(Email.isValidEmail(local + "@" + tooLongDomain));
    }

    @Test
    public void isValidEmail_variousValidEmails() {
        String[] validEmails = {
            "simple@example.com",
            "very.common@example.com",
            "disposable.style.email.with+symbol@example.com",
            "other.email-with-hyphen@example.com",
            "fully-qualified-domain@example.com",
            "user.name+tag+sorting@example.com",
            "x@example.com",
            "example-indeed@strange-example.com",
            "admin@mailserver1",
            "example@s.solutions"
        };

        for (String email : validEmails) {
            assertTrue(Email.isValidEmail(email), "Failed for valid email: " + email);
        }
    }

    @Test
    public void isValidEmail_variousInvalidEmails() {
        String[] invalidEmails = {
            "Abc.example.com", // missing @
            "A@b@c@example.com", // multiple @
            "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com", // invalid characters
            "just\"not\"right@example.com", // quoted improperly
            "this is\"not\\allowed@example.com", // spaces
            "i_like_underscore@but_its_not_allowed_in_this_part.example.com" // underscore in domain
        };

        for (String email : invalidEmails) {
            assertFalse(Email.isValidEmail(email), "Failed for invalid email: " + email);
        }
    }
}

