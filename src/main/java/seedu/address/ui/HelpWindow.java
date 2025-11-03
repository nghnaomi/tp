package seedu.address.ui;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.LanguageLoader;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-f14b-4.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<CommandEntry> commandTableView;

    @FXML
    private TableColumn<CommandEntry, String> actionColumn;

    @FXML
    private TableColumn<CommandEntry, String> formatColumn;

    @FXML
    private TableView<CountryEntry> countryTableView;

    @FXML
    private TableColumn<CountryEntry, String> countryNameColumn;

    @FXML
    private TableColumn<CountryEntry, String> countryCodeColumn;

    @FXML
    private TableView<TimezoneEntry> timezoneTable;

    @FXML
    private TextField timezoneSearchField;

    @FXML
    private TableColumn<TimezoneEntry, String> regionColumn;

    @FXML
    private TableColumn<TimezoneEntry, String> offsetColumn;

    @FXML
    private TextField countrySearchField;

    @FXML
    private TableView<LanguageEntry> languageTableView;

    @FXML
    private TableColumn<LanguageEntry, String> languageNameColumn;

    @FXML
    private TableColumn<LanguageEntry, String> languageGreetingsColumn;

    @FXML
    private TextField languageSearchField;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    private void initializeTimezones() {
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));
        offsetColumn.setCellValueFactory(new PropertyValueFactory<>("offset"));

        List<TimezoneEntry> zones = getAllZones();

        FilteredList<TimezoneEntry> filteredZones = new FilteredList<>(FXCollections.observableArrayList(zones),
                p -> true);
        timezoneTable.setItems(filteredZones);

        timezoneSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String lower = newValue == null ? "" : newValue.toLowerCase();
            filteredZones.setPredicate(zone -> zone.getRegion().toLowerCase().contains(lower)
                    || zone.getOffset().toLowerCase().contains(lower));
        });
    }

    /**
     * Generates a list of all available time zones with their corresponding city/region
     * and UTC offset, sorted by offset (from lowest to highest) and then by city name.
     * <p>
     * The UTC offset is displayed in the format "UTC±HH:MM". The special "Z" offset
     * (representing UTC) is normalized to "+00:00" for display purposes.
     * </p>
     *
     * @return a {@link List} of {@link TimezoneEntry} objects, each representing
     *         a time zone with its city/region and formatted UTC offset.
     */
    private List<TimezoneEntry> getAllZones() {
        List<TimezoneEntry> list = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now();

        for (String zoneId : ZoneId.getAvailableZoneIds()) {
            ZoneId zone = ZoneId.of(zoneId);
            ZoneOffset offset = now.withZoneSameInstant(zone).getOffset();

            String offsetString = offset.getId().equals("Z") ? "+00:00" : offset.getId();

            String [] parts = zoneId.split("/");
            String city = parts.length > 1 ? parts[parts.length - 1].replace("_", " ") : zoneId;
            list.add(new TimezoneEntry(city, "UTC" + offsetString));
        }

        list.sort(Comparator
                .comparingInt((TimezoneEntry entry) -> {
                    String offsetStr = entry.getOffset().substring(3);
                    if (offsetStr.equals("Z")) {
                        offsetStr = "00:00";
                    }
                    String [] hms = offsetStr.split(":");
                    int hours = Integer.parseInt(hms[0]);
                    int minutes = Integer.parseInt(hms[1]);
                    return hours * 60 + (hours < 0 ? -minutes : minutes);
                }).thenComparing(TimezoneEntry::getRegion));
        return list;
    }

    /**
     * Initializes the HelpWindow after its FXML is loaded.
     * Sets up the table columns and populates the command reference table.
     */
    @FXML
    public void initialize() {
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("format"));

        formatColumn.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<CommandEntry, String>() {
                private final javafx.scene.text.Text text = new javafx.scene.text.Text();

                {
                    text.wrappingWidthProperty().bind(formatColumn.widthProperty().subtract(10));
                    setGraphic(text);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        text.setText(null);
                    } else {
                        text.setText(item);
                    }
                }
            };
        });

        commandTableView.setFixedCellSize(-1);

        commandTableView.getItems().addAll(
                new CommandEntry("Help", "help"),
                new CommandEntry("Add", "add name:NAME phone:PHONE email:EMAIL address:ADDRESS offset:OFFSET "
                        + "[country:COUNTRY] [organisation:ORGANISATION] [event:EVENT] [channel:CHANNEL] "
                        + "[language:LANGUAGE] [note:NOTE] [tag:TAG]...\n"
                        + "e.g. add name:James Ho phone:22224444 email:jamesho@example.com "
                        + "address:123, Clementi Rd, 1234665  offset:+08:00 country:Singapore language:English "
                        + "tag:friend tag:colleague"),
                new CommandEntry("List", "list"),
                new CommandEntry("Edit", "edit INDEX [name:NAME] [phone:PHONE] [email:EMAIL] "
                        + "[address:ADDRESS] [offset: OFFSET] [country:COUNTRY] [organisation:ORGANISATION] "
                        + "[event:EVENT] [channel:CHANNEL] [language:LANGUAGE] [tag:TAG]...\n"
                        + "e.g. edit 2 name:James Lee email:jameslee@example.com"),
                new CommandEntry("Delete", "delete INDEX\n e.g. delete 3"),
                new CommandEntry("Find", "find KEYWORD [MORE_KEYWORDS]\n e.g. find James Jake"),
                new CommandEntry("Find tag", "findtag TAG [MORE_TAGS]\n e.g. findtag friends"),
                new CommandEntry("Find country", "findcountry COUNTRY\n e.g. findcountry Singapore"),
                new CommandEntry("Find organisation", "findorganisation ORGANISAION\n e.g. findorganisation NUS"),
                new CommandEntry("Archive", "archive INDEX"),
                new CommandEntry("Unarchive", "unarchive INDEX"),
                new CommandEntry("Archive list", "archivelist"),
                new CommandEntry("Sort by country", "sortcountry"),
                new CommandEntry("Sort by name", "sortname"),
                new CommandEntry("Sort by date added", "sortdate"),
                new CommandEntry("Clear", "clear"),
                new CommandEntry("Exit", "exit"));

        countryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        countryCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));

        List<CountryEntry> countries = new ArrayList<>();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        for (String regionCode : phoneUtil.getSupportedRegions()) {
            String countryName = new Locale("", regionCode).getDisplayCountry(Locale.ENGLISH);
            if (!countryName.isEmpty()) {
                String phoneCode = "+" + phoneUtil.getCountryCodeForRegion(regionCode);
                countries.add(new CountryEntry(countryName, phoneCode));
            }
        }

        countries.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
        FilteredList<CountryEntry> filteredCountries = new FilteredList<>(FXCollections.observableArrayList(countries),
                p -> true);
        countryTableView.setItems(filteredCountries);

        countrySearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String lower = newValue.toLowerCase();
            filteredCountries.setPredicate(country -> {
                if (lower == null || lower.isBlank()) {
                    return true;
                }
                return country.getName().toLowerCase().contains(lower)
                        || country.getCode().toLowerCase().contains(lower);
            });
        });

        initializeTimezones();

        languageNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        languageGreetingsColumn.setCellValueFactory(new PropertyValueFactory<>("greeting"));

        // Wrap long text
        languageGreetingsColumn.setCellFactory(col -> new TableCell<LanguageEntry, String>() {
            private final javafx.scene.text.Text text = new javafx.scene.text.Text();
            {
                text.wrappingWidthProperty().bind(languageGreetingsColumn.widthProperty().subtract(15));
                setGraphic(text);
                setPrefHeight(USE_COMPUTED_SIZE);
            }

            @Override
            protected void updateItem(String hello, boolean empty) {
                super.updateItem(hello, empty);
                text.setText((empty || hello == null) ? null : hello);
            }
        });

        ObservableList<LanguageEntry> languages = LanguageLoader.loadLanguages();

        // Sort alphabetically
        languages.sort(Comparator.comparing(LanguageEntry::getName));

        // FilteredList for search
        FilteredList<LanguageEntry> filteredLanguages = new FilteredList<>(FXCollections.observableArrayList(languages),
            p -> true);
        languageTableView.setItems(filteredLanguages);

        languageSearchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal == null ? "" : newVal.toLowerCase();
            filteredLanguages.setPredicate(lang ->
                    lang.getName().toLowerCase().contains(lower)
                        || lang.getGreeting().toLowerCase().contains(lower));
        });

        languageTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tabPane.getTabs().forEach(tab -> tab.setClosable(false));
    }

    /**
     * Shows the help window.
     *
     * @throws IllegalStateException
     *                               <ul>
     *                               <li>
     *                               if this method is called on a thread other than
     *                               the JavaFX Application Thread.
     *                               </li>
     *                               <li>
     *                               if this method is called during animation or
     *                               layout processing.
     *                               </li>
     *                               <li>
     *                               if this method is called on the primary stage.
     *                               </li>
     *                               <li>
     *                               if {@code dialogStage} is already showing.
     *                               </li>
     *                               </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
