package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CHANNEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COUNTRY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LANGUAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OFFSET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_COUNTRY_AMY = "Singapore";
    public static final String VALID_COUNTRY_BOB = "Singapore";
    public static final String VALID_NOTE_AMY = "Like German history.";
    public static final String VALID_NOTE_BOB = "Favourite pastime: Eating pasta";
    public static final String VALID_ORGANISATION_AMY = "Google";
    public static final String VALID_ORGANISATION_BOB = "Microsoft";
    public static final String VALID_EVENT_AMY = "TechFair 2025";
    public static final String VALID_EVENT_BOB = "React Meetup 2025";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_OFFSET_AMY = "+00:00";
    public static final String VALID_OFFSET_BOB = "+00:00";
    public static final String VALID_CHANNEL_BOB = "TELEGRAM";
    public static final String VALID_LANGUAGE_BOB = "chinese";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String COUNTRY_DESC_AMY = " " + PREFIX_COUNTRY + VALID_COUNTRY_AMY;
    public static final String COUNTRY_DESC_BOB = " " + PREFIX_COUNTRY + VALID_COUNTRY_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_FRIEND1 = " " + PREFIX_TAG + VALID_TAG_FRIEND + "1";
    public static final String TAG_DESC_FRIEND2 = " " + PREFIX_TAG + VALID_TAG_FRIEND + "2";
    public static final String TAG_DESC_FRIEND3 = " " + PREFIX_TAG + VALID_TAG_FRIEND + "3";
    public static final String TAG_DESC_FRIEND4 = " " + PREFIX_TAG + VALID_TAG_FRIEND + "4";
    public static final String TAG_DESC_FRIEND5 = " " + PREFIX_TAG + VALID_TAG_FRIEND + "5";
    public static final String TAG_DESC_FRIEND6 = " " + PREFIX_TAG + VALID_TAG_FRIEND + "6";
    public static final String TAG_DESC_FRIEND7 = " " + PREFIX_TAG + VALID_TAG_FRIEND + "7";
    public static final String TAG_DESC_FRIEND8 = " " + PREFIX_TAG + VALID_TAG_FRIEND + "8";
    public static final String TAG_DESC_FRIEND9 = " " + PREFIX_TAG + VALID_TAG_FRIEND + "9";
    public static final String TAG_DESC_FRIEND0 = " " + PREFIX_TAG + VALID_TAG_FRIEND + "0";
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;
    public static final String OFFSET_DESC_AMY = " " + PREFIX_OFFSET + VALID_OFFSET_AMY;
    public static final String OFFSET_DESC_BOB = " " + PREFIX_OFFSET + VALID_OFFSET_BOB;
    public static final String CHANNEL_DESC_BOB = " " + PREFIX_CHANNEL + VALID_CHANNEL_BOB;
    public static final String LANGUAGE_DESC_BOB = " " + PREFIX_LANGUAGE + VALID_LANGUAGE_BOB;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_OFFSET_DESC = " " + PREFIX_OFFSET + "-17:00";

    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder()
            .withName(VALID_NAME_AMY)
            .withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY)
            .withAddress(VALID_ADDRESS_AMY)
            .withCountry(VALID_COUNTRY_AMY)
            .withOrganisation(VALID_ORGANISATION_AMY)
            .withEvent(VALID_EVENT_AMY)
            .withNote(VALID_NOTE_AMY)
            .withTags(VALID_TAG_FRIEND)
            .build();

        DESC_BOB = new EditPersonDescriptorBuilder()
            .withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withCountry(VALID_COUNTRY_BOB)
            .withOrganisation(VALID_ORGANISATION_BOB)
            .withEvent(VALID_EVENT_BOB)
            .withNote(VALID_NOTE_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();
    }


    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult}
     * <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to
     * {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in
     * {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given
     * {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

}
