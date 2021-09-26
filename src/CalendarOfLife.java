import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

//TODO: Add calender snapshot function
//TODO: Add skill mastery or achievement duration for comparison
public class CalendarOfLife extends Application {
    
    CheckMenuItem enableProfile;
    MenuItem manageProfile, saveProfile, exitProgram, aboutProgram;
    
    ObservableList<Profile> profile;
    ComboBox<String> profilePicker;
    
    DatePicker birthDatePicker;
    TextField expectedAgeField;
    Button submitAndClear;
    Label calendarTitle;
    Text totalWeeksText, usedWeeksText, remainingWeeksText;
    FlowPane display;
    ArrayList<Box> boxes;
    VBox outputColumn;
    HBox infoRow;

    @Override
    public void init() throws Exception {
        super.init();
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        Menu profileMenu = new Menu("Profile");
        Menu helpMenu = new Menu("Help");
        
        //Toggle profile
        enableProfile = new CheckMenuItem("Enable Profile");
        enableProfile.setSelected(true);
        enableProfile.setOnAction(e -> {
            if (!profilePicker.isDisabled()) {
                profilePicker.setDisable(true);
                birthDatePicker.setValue(LocalDate.of(2000, Month.JANUARY, 1));
                expectedAgeField.clear();
                
            } else if (profilePicker.isDisabled()) {
                profilePicker.setDisable(false);
                profilePicker.getSelectionModel().clearSelection();
            }});
        manageProfile = new MenuItem("Manage Profile");
        manageProfile.setOnAction(e -> openProfileManager());
        //Save menu item
        saveProfile = new MenuItem("Save Current Configuration as Profile");
        //Exit menu item
        exitProgram = new MenuItem("Exit");
        exitProgram.setOnAction(e -> primaryStage.close());
        //About Program menu item
        aboutProgram = new MenuItem("About");
        aboutProgram.setOnAction(e -> openAboutProgramWindow());
        
        profileMenu.getItems().addAll(enableProfile, new SeparatorMenuItem(),
                manageProfile, saveProfile, new SeparatorMenuItem(), exitProgram);
        helpMenu.getItems().add(aboutProgram);
        
        profile = FXCollections.observableArrayList();
        profile.add(new Profile("I Gusti Bagus Ananda", 
                LocalDate.of(1998, Month.JULY, 18), 50));
        profile.add(new Profile("Karina Paramitha", 
                LocalDate.of(2001, Month.APRIL, 5), 80));
        
        profilePicker = new ComboBox<>();
        profilePicker.setPrefWidth(100);
        profilePicker.getItems().add(profile.get(0).getName());
        profilePicker.getSelectionModel().selectedItemProperty().addListener(
                e -> {
                    try {
                        Profile selectedProfile = profile.get(profilePicker.getSelectionModel().getSelectedIndex());
                        birthDatePicker.setValue(selectedProfile.getBirthDate());
                        expectedAgeField.setText(String.valueOf(selectedProfile.getExpectedAge()));
                    } catch (ArrayIndexOutOfBoundsException exception) {}
                });
        
        birthDatePicker = new DatePicker(LocalDate.of(2000, Month.JANUARY, 1));
        expectedAgeField = new TextField();
        expectedAgeField.setMaxWidth(100);
        submitAndClear = new Button("Submit");
        submitAndClear.setOnAction(e -> submitAndClearBoxes());
        
        calendarTitle = new Label("The Weeks of Your Life");
        calendarTitle.setFont(new Font("College Block", 20));
        
        totalWeeksText = new Text();
        totalWeeksText.setFont(new Font(20));
        usedWeeksText = new Text();
        usedWeeksText.setFont(new Font(20));
        remainingWeeksText = new Text();
        remainingWeeksText.setFont(new Font(20));
        
        infoRow = new HBox(20, new VBox(new Label("Total Weeks"), totalWeeksText),
                new VBox(new Label("Used Weeks"), usedWeeksText),
                new VBox(new Label("Remaining Weeks"), remainingWeeksText));
        infoRow.setAlignment(Pos.BOTTOM_RIGHT);
        infoRow.setVisible(false);
                
        HBox inputRow = new HBox(10, new VBox(2, new Label("Profile"), profilePicker),
                new VBox(2, new Label("Birthdate"), birthDatePicker),
                new VBox(2, new Label("Expected Age"), expectedAgeField), submitAndClear);
        inputRow.setAlignment(Pos.BOTTOM_LEFT);
        
        AnchorPane bottomRow = new AnchorPane(inputRow, infoRow);
        AnchorPane.setLeftAnchor(inputRow, Double.valueOf(0));
        AnchorPane.setRightAnchor(infoRow, Double.valueOf(25));
        
        display = new FlowPane(2, 2);
        display.prefWidthProperty().bind(primaryStage.widthProperty());
        display.prefHeightProperty().bind(primaryStage.heightProperty());
        display.setBackground(
                new Background(new BackgroundFill(Color.GREY, null, null)));
        display.setPadding(new Insets(5));        
        ScrollPane scrollPane = new ScrollPane(display);
        scrollPane.setFitToWidth(true);
        
        boxes = new ArrayList<>();
        
        outputColumn = new VBox(2, calendarTitle, scrollPane);
        outputColumn.setAlignment(Pos.TOP_CENTER);
        outputColumn.setVisible(false);
                
        VBox mainPane = new VBox(10, outputColumn, bottomRow);
        mainPane.setPadding(new Insets(5));
        
        Scene scene = new Scene(new VBox(new MenuBar(profileMenu, helpMenu), mainPane), 800, 600);
        
        primaryStage.setTitle("Tim Urban's Calendar of Life");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void submitAndClearBoxes() {
        
        switch (submitAndClear.getText()) {
            
            case "Submit":
                LocalDate birthDate = birthDatePicker.getValue();
                LocalDate deathDate = birthDate.plusYears(Long.parseLong(expectedAgeField.getText()));
                LocalDate todayDate = LocalDate.now();

                long totalWeeks = ChronoUnit.WEEKS.between(birthDate, deathDate);
                long usedWeeks = ChronoUnit.WEEKS.between(birthDate, todayDate);
                long remainingWeeks = totalWeeks - usedWeeks;
                
                for (int i = 1; i <= usedWeeks; i++) {
                    boxes.add(new Box(true));
                }

                for (int i =  1; i <= remainingWeeks; i++) {
                    boxes.add(new Box(false));
                } 
                
                Iterator<Box> boxIterator = boxes.iterator();
                KeyFrame key = new KeyFrame(Duration.millis(2), 
                        e -> display.getChildren().add(boxIterator.next()));
                Timeline animationPlayer = new Timeline(key);
                animationPlayer.setCycleCount(boxes.size());
                animationPlayer.play();
                submitAndClear.setDisable(true); 
                
                animationPlayer.setOnFinished(e -> {
                    totalWeeksText.setText(String.valueOf(totalWeeks));
                    usedWeeksText.setText(String.valueOf(usedWeeks));
                    remainingWeeksText.setText(String.valueOf(remainingWeeks));
                    infoRow.setVisible(true);
                    submitAndClear.setDisable(false);
                    
                    for (int i = 0; i < display.getChildren().size(); i++) {
                        LocalDate firstDateInAWeek = birthDate.plusWeeks(i);
                        LocalDate lastDateInAWeek = firstDateInAWeek.plusDays(7);
                        Tooltip tooltip = new Tooltip("Week #" + (i + 1) + "\n" + 
                                firstDateInAWeek + "\nuntil\n" +
                                lastDateInAWeek);
                        Tooltip.install(display.getChildren().get(i), tooltip);
                    }
                });

                outputColumn.setVisible(true);

                birthDatePicker.setDisable(true);
                expectedAgeField.setDisable(true);

                submitAndClear.setText("Clear");
                break;
            case "Clear":
                boxes.clear();
                display.getChildren().clear();
                outputColumn.setVisible(false);
                
                totalWeeksText.setText("");
                usedWeeksText.setText("");
                remainingWeeksText.setText("");
                infoRow.setVisible(false);

                birthDatePicker.setDisable(false);
                expectedAgeField.setDisable(false);
                
                birthDatePicker.setValue(LocalDate.of(2000, Month.JANUARY, 1));
                expectedAgeField.clear();

                submitAndClear.setText("Submit");
                break;
        }
    }
    
    public void openProfileManager() {
        TableView profileTable = new TableView(profile);
        profileTable.setPrefWidth(400);
        TableColumn<Profile, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setPrefWidth(220);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Profile, LocalDate> birthdateColumn = new TableColumn<>("Birthdate");
        birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        TableColumn<Profile, Integer> expectedAgeColumn = new TableColumn<>("Expected Age");
        expectedAgeColumn.setCellValueFactory(new PropertyValueFactory<>("expectedAge"));
        profileTable.getColumns().addAll(nameColumn, birthdateColumn, expectedAgeColumn);
        
        TextField nameEditor = new TextField();
        DatePicker birthDateEditor = new DatePicker();
        TextField expectedAgeEditor = new TextField();
        Button editButton = new Button("Change");
        Button addButton = new Button("Add New Profile");
        Button deleteButton = new Button("Delete Profile");
                
        VBox tableColumn = new VBox(5, profileTable, new HBox(5, addButton, deleteButton));
        tableColumn.setPadding(new Insets(10, 0, 10, 10));
        
        VBox editorColumn = new VBox(5, new VBox(5, new Label("Name"), nameEditor),
                new VBox(2, new Label("Birthdate"), birthDateEditor), 
                new VBox(2, new Label("Expected Age"), expectedAgeEditor),
                editButton);
        editorColumn.setPadding(new Insets(20));
        editorColumn.setAlignment(Pos.CENTER);
                
        Stage profileManager = new Stage();
        profileManager.setTitle("Profile Manager");
        profileManager.initModality(Modality.APPLICATION_MODAL);
        profileManager.setScene(new Scene(new HBox(tableColumn, editorColumn)));
        profileManager.show();
    }
    
    public void openAboutProgramWindow() {
        ImageView imageView = new ImageView("file:/test.jpg");
        
        VBox root = new VBox(imageView);
        root.setPadding(new Insets(10));
        
        Stage aboutWindow = new Stage();
        aboutWindow.setTitle("About");
        aboutWindow.setScene(new Scene(root));
        aboutWindow.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}