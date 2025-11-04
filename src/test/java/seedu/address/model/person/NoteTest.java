package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NoteTest {

    @Test
    public void isValidNote_exceedsMaxLength_returnsFalse() {
        String longNote = "a".repeat(501);
        assertFalse(Note.isValidNote(longNote));
    }

    @Test
    public void isValidNote_maxLengthBoundary_returnsTrue() {
        String validNote = "a".repeat(500);
        assertTrue(Note.isValidNote(validNote));
    }

    @Test
    public void equals() {
        Note note = new Note("Hello");

        // same object -> returns true
        assertTrue(note.equals(note));

        // same values -> returns true
        Note noteCopy = new Note("Hello");
        assertTrue(note.equals(noteCopy));

        // different types -> returns false
        assertFalse(note.equals(1));

        // null -> returns false
        assertFalse(note.equals(null));

        // different note -> returns false
        Note differentNote = new Note("Bye");
        assertFalse(note.equals(differentNote));

        // empty note vs non-empty -> returns false
        Note emptyNote = new Note("");
        assertFalse(note.equals(emptyNote));

        // two empty notes -> returns true
        Note anotherEmpty = new Note("");
        assertTrue(emptyNote.equals(anotherEmpty));
    }

    @Test
    public void toString_returnsValue() {
        Note note = new Note("Prefers WhatsApp");
        assertEquals("Prefers WhatsApp", note.toString());

        Note emptyNote = new Note("");
        assertEquals("", emptyNote.toString());
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        Note note1 = new Note("Test note");
        Note note2 = new Note("Test note");
        assertEquals(note1.hashCode(), note2.hashCode());
    }

    /**
     * Tests that a note with only spaces is considered valid.
     */
    @Test
    public void isValidNote_onlySpaces_returnsTrue() {
        assertTrue(Note.isValidNote("   "));
    }

    /**
     * Tests that a note containing newlines is valid.
     */
    @Test
    public void isValidNote_containsNewline_returnsTrue() {
        assertTrue(Note.isValidNote("Line1\nLine2"));
    }

    /**
     * Tests that a note containing tabs is valid.
     */
    @Test
    public void isValidNote_containsTab_returnsTrue() {
        assertTrue(Note.isValidNote("Hello\tWorld"));
    }

    /**
     * Tests that a note with special characters is valid.
     */
    @Test
    public void isValidNote_specialCharacters_returnsTrue() {
        assertTrue(Note.isValidNote("@user loves #coding!"));
    }

    /**
     * Tests equality with same content but different casing.
     */
    @Test
    public void equals_differentCase_returnsFalse() {
        Note noteLower = new Note("hello");
        Note noteUpper = new Note("HELLO");
        assertFalse(noteLower.equals(noteUpper));
    }

    /**
     * Tests that equals is symmetric.
     */
    @Test
    public void equals_symmetricProperty() {
        Note noteA = new Note("Symmetric test");
        Note noteB = new Note("Symmetric test");
        assertTrue(noteA.equals(noteB) && noteB.equals(noteA));
    }

    /**
     * Tests that equals is transitive.
     */
    @Test
    public void equals_transitiveProperty() {
        Note a = new Note("abc");
        Note b = new Note("abc");
        Note c = new Note("abc");
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c));
    }

    /**
     * Tests that equals handles null safely.
     */
    @Test
    public void equals_nullSafe() {
        Note note = new Note("safe");
        assertFalse(note.equals(null));
    }

    /**
     * Tests that equals handles non-Note objects safely.
     */
    @Test
    public void equals_differentType() {
        Note note = new Note("text");
        assertFalse(note.equals(1234));
    }

    /**
     * Tests that hashCode consistency holds after multiple calls.
     */
    @Test
    public void hashCode_consistency() {
        Note note = new Note("Consistency check");
        int hash1 = note.hashCode();
        int hash2 = note.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests that two notes with different values have different hash codes.
     */
    @Test
    public void hashCode_differentValues_notEqual() {
        Note note1 = new Note("One");
        Note note2 = new Note("Two");
        assertFalse(note1.hashCode() == note2.hashCode());
    }

    /**
     * Tests toString on multiline input.
     */
    @Test
    public void toString_multiline_returnsSame() {
        String text = "Line 1\nLine 2";
        Note note = new Note(text);
        assertEquals(text, note.toString());
    }

    /**
     * Tests creating a note with an empty string.
     */
    @Test
    public void constructor_emptyString_createsNote() {
        Note note = new Note("");
        assertEquals("", note.toString());
    }

    /**
     * Tests creating a note with a single character.
     */
    @Test
    public void constructor_singleChar_createsNote() {
        Note note = new Note("a");
        assertEquals("a", note.toString());
    }

    /**
     * Tests that leading and trailing spaces are preserved.
     */
    @Test
    public void note_preservesWhitespace() {
        Note note = new Note("   spaced text   ");
        assertEquals("   spaced text   ", note.toString());
    }

    /**
     * Tests that very long valid notes (just under 500 chars) are valid.
     */
    @Test
    public void isValidNote_longValid_returnsTrue() {
        String longNote = "b".repeat(499);
        assertTrue(Note.isValidNote(longNote));
    }

    /**
     * Tests boundary between valid and invalid note lengths.
     */
    @Test
    public void isValidNote_boundary500And501() {
        assertTrue(Note.isValidNote("a".repeat(500)));
        assertFalse(Note.isValidNote("a".repeat(501)));
    }

    /**
     * Tests that an extremely short note (1 char) is valid.
     */
    @Test
    public void isValidNote_singleCharacter_returnsTrue() {
        assertTrue(Note.isValidNote("A"));
    }

    /**
     * Tests that an empty string is valid (assuming empty notes allowed).
     */
    @Test
    public void isValidNote_empty_returnsTrue() {
        assertTrue(Note.isValidNote(""));
    }

    /**
     * Tests equality with multiple whitespace differences.
     */
    @Test
    public void equals_whitespaceDifference_returnsFalse() {
        Note n1 = new Note("abc");
        Note n2 = new Note("abc ");
        assertFalse(n1.equals(n2));
    }

    /**
     * Tests that equals handles emoji characters.
     */
    @Test
    public void equals_withEmoji_returnsTrue() {
        Note n1 = new Note("Good 😊");
        Note n2 = new Note("Good 😊");
        assertTrue(n1.equals(n2));
    }

    /**
     * Tests that equals detects different emoji content.
     */
    @Test
    public void equals_differentEmoji_returnsFalse() {
        Note n1 = new Note("Good 😊");
        Note n2 = new Note("Good 😢");
        assertFalse(n1.equals(n2));
    }

    /**
     * Tests hashCode stability for notes with Unicode.
     */
    @Test
    public void hashCode_unicodeConsistency() {
        Note note = new Note("你好世界");
        int hash = note.hashCode();
        assertEquals(hash, note.hashCode());
    }

    /**
     * Tests that equals treats same Unicode content equally.
     */
    @Test
    public void equals_unicodeEquality() {
        Note n1 = new Note("こんにちは");
        Note n2 = new Note("こんにちは");
        assertTrue(n1.equals(n2));
    }

}
