package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author profesor
 */
public class StudentJavaFX extends Application {

    BorderPane root = new BorderPane();
    Label imeLbl = new Label("Ime:");
    TextField imeTxt = new TextField();
    Label prezimeLbl = new Label("Prezime:");
    TextField prezimeTxt = new TextField();
    Label smerLbl = new Label("Smer:");
    ComboBox<Student.Smer> smerCombo = new ComboBox<>(FXCollections.observableArrayList(Student.Smer.Informacione_tehnologije,
            Student.Smer.Softversko_inzenjerstvo, Student.Smer.Racunarske_igre));
    Label tipStudiranjaLbl = new Label("Tip studiranja:");
    RadioButton tradicionalnoRadio = new RadioButton("Tradicionalno");
    RadioButton internetRadio = new RadioButton("Internet");
    ToggleGroup groupRadio = new ToggleGroup();

    Button saveBtn = new Button("Save");
    TextField searchText = new TextField();
    TableView<Student> tableView = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        root.setPadding(new Insets(20));

        //****************** LEFT PANE *************//
        // Da bismo omogućili da samo jedno dugme u trenutku bude selektovano, RadioButton komponente moraju da pripadaju istoj ToogleGroup-i
        tradicionalnoRadio.setToggleGroup(groupRadio);
        internetRadio.setToggleGroup(groupRadio);
        // Pri otvaranju forme selektujemo smer Informacione tehnologije, jer će u suprotnom ComboBox komponenta biti prazna
        smerCombo.getSelectionModel().select(Student.Smer.Informacione_tehnologije);
        // Pri otvaranju forme selektujemo tip Tradicionalno
        tradicionalnoRadio.setSelected(true);

        //Formiramo levi panel koji formatiramo korišćenjem padding-a i vertikalnog prostora (spacing) između komponenata
        VBox leftPane = new VBox(imeLbl, imeTxt, prezimeLbl, prezimeTxt, smerLbl, smerCombo, tipStudiranjaLbl, tradicionalnoRadio, internetRadio, saveBtn);
        leftPane.setSpacing(10);
        root.setLeft(leftPane);

        //***************** CENTER PANE ******************//
        searchText.setPromptText("Unesite rec za pretragu");
        FlowPane topCenterPane = new FlowPane(searchText);
        topCenterPane.setPadding(new Insets(0, 0, 10, 0));
        topCenterPane.setAlignment(Pos.CENTER_RIGHT);

        VBox centerPane = new VBox(topCenterPane, tableView);
        centerPane.setPadding(new Insets(0, 0, 0, 10));
        root.setCenter(centerPane);

        //************************* TABLE VIEW FORMAT ******************************//
        TableColumn<Student, String> imeCol = new TableColumn<>("Ime");
        TableColumn<Student, String> prezimeCol = new TableColumn<>("Prezime");
        TableColumn<Student, String> smerCol = new TableColumn<>("Smer");
        TableColumn<Student, String> tipCol = new TableColumn<>("Tip studiranja");

        tableView.getColumns().addAll(imeCol, prezimeCol, smerCol, tipCol);

        imeCol.setCellValueFactory(new PropertyValueFactory<>("ime"));
        prezimeCol.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        smerCol.setCellValueFactory(new PropertyValueFactory<>("smer"));
        tipCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>((cellData.getValue().isTradicionalno()) ? "Tradicionalno" : "Internet"));
        ObservableList<Student> studentList = FileUtil.readData();
        FilteredList<Student> filteredData = new FilteredList<>(studentList, s -> true);
        tableView.getItems().addAll(filteredData);

        //*************************** SAVE STUDENT *********************************//
        saveBtn.setOnAction((ActionEvent e) -> {
            Student student = new Student(imeTxt.getText(), prezimeTxt.getText(), smerCombo.getSelectionModel().getSelectedItem(), tradicionalnoRadio.isSelected());
            FileUtil.saveData(student);
            studentList.add(student);
            tableView.getItems().setAll(filteredData);
            resetFields();
            Alert message = new Alert(Alert.AlertType.INFORMATION, "Uspešno ste sačuvali studenta", ButtonType.OK);
            message.show();
        });

        //**************************SEARCH STUDENT ***************************************//
        //Definisanje predikata pretrage po svakom parametru. Record treba da se prikazuje ukoliko se uneti tekst nalazi u nekom od atributa
        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getIme().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (student.getPrezime().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (student.getSmer().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if ((student.isTradicionalno() ? "tradicionalno" : "internet").toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            });
            tableView.getItems().setAll(filteredData);

        });

        Scene scene = new Scene(root, 600, 350);

        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void resetFields() {
        imeTxt.clear();
        prezimeTxt.clear();
        smerCombo.getSelectionModel().selectFirst();
        tradicionalnoRadio.setSelected(true);
    }

}
