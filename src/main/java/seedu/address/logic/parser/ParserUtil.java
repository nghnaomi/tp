package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Country;
import seedu.address.model.person.Email;
import seedu.address.model.person.Event;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Offset;
import seedu.address.model.person.Organisation;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PreferredLanguage;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser
 * classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading
     * and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero
     *                        unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String organisation} into an {@code Organisation}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code organisation} is invalid.
     */
    public static Organisation parseOrganisation(String org) throws ParseException {
        if (org == null) {
            throw new ParseException("Organisation cannot be null");
        }

        return new Organisation(org.trim());
    }

    /**
     * Parses a {@code String event} into an {@code Event}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code event} is invalid.
     */
    public static Event parseEvent(String event) throws ParseException {
        if (event == null) {
            throw new ParseException("Event cannot be null");
        }
        // Allow empty or whitespace-only input
        String trimmedEvent = event.trim();
        return new Event(trimmedEvent);
    }

    /**
     * Parses a {@code String note} into a {@code Note}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code note} is invalid.
     */
    public static Note parseNote(String note) throws ParseException {
        if (note == null) {
            throw new ParseException("Note cannot be null");
        }
        // Allow empty or whitespace-only input
        String trimmedNote = note.trim();
        return new Note(trimmedNote);
    }

    /**
     * Parses an optional {@code String country} into an {@code Country}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code country} is invalid.
     */
    public static Country parseCountry(String country) throws ParseException {
        requireNonNull(country);
        String trimmedCountry = country.trim();
        if (!Country.isValidCountry(trimmedCountry)) {
            throw new ParseException(Country.MESSAGE_CONSTRAINTS);
        }
        return new Country(trimmedCountry);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Offset parser. Validates +HH:MM / -HH:MM.
     * @param input raw user input (non-null)
     *
     */
    public static Offset parseOffset(String input) throws ParseException {
        requireNonNull(input);
        String trimmedInput = input.trim();

        if (!trimmedInput.matches("^[+-](?:0\\d|1[0-4]):[0-5]\\d$")) {
            throw new ParseException(String.format("Invalid Offset format: " + Offset.MESSAGE_CONSTRAINTS));
        }
        try {
            return new Offset(trimmedInput);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Parses a {@code String language} into a {@code PreferredLanguage}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code language} is invalid.
     */
    public static PreferredLanguage parsePreferredLanguage(String language) {
        return new PreferredLanguage(language.trim());
    }

}
