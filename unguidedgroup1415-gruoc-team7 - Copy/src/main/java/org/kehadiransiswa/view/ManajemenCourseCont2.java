package org.kehadiransiswa.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kehadiransiswa.data.Course;

import java.net.URL;
import java.util.ResourceBundle;

public class ManajemenCourseCont2 implements Initializable {

    @FXML
    private TextField id;

    @FXML
    private TextField title;

    @FXML
    private TextArea deskripsi;

    @FXML
    private Button tambah;

    @FXML
    private Button hapus;

    @FXML
    private Button ubah;

    @FXML
    private Button search;

    @FXML
    private TableView<Course> table;

    @FXML
    private TableColumn<Course, Integer> tableId;

    @FXML
    private TableColumn<Course, String> tableTitle;

    @FXML
    private TableColumn<Course, String> tableDeskripsi;

    @FXML
    private TextField searchField;

    private final ObservableList<Course> courseList = FXCollections.observableArrayList();

    @FXML
    void handleClearSearchText(ActionEvent event) {
        searchField.setText("");
        event.consume();
    }

    @FXML
    void onTambah(ActionEvent event) {

        try {
            int courseId = Integer.parseInt(id.getText());
            String courseTitle = title.getText();
            String courseDesc = deskripsi.getText();

            Course newCourse = new Course(courseId, courseTitle, courseDesc);
            courseList.add(newCourse);
            clearInputFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "ID must be an integer.");
        }
    }

    @FXML
    void onUbah(ActionEvent event) {

        Course selectedCourse = table.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            try {
                selectedCourse.setId(Integer.parseInt(id.getText()));
                selectedCourse.setTitle(title.getText());
                selectedCourse.setDescription(deskripsi.getText());
                table.refresh();
                clearInputFields();
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "ID must be an integer.");
            }
        } else {
            showAlert("No Selection", "No course selected to update.");
        }
    }

    @FXML
    void onHapus(ActionEvent event) {

        Course selectedCourse = table.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            courseList.remove(selectedCourse);
            clearInputFields();
        } else {
            showAlert("No Selection", "No course selected to delete.");
        }
    }

    private void clearInputFields() {
        id.clear();
        title.clear();
        deskripsi.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableDeskripsi.setCellValueFactory(new PropertyValueFactory<>("description"));

        table.setItems(courseList);
    }
}