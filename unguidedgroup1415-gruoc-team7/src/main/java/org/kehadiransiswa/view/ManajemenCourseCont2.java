package org.kehadiransiswa.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kehadiransiswa.data.Course;

import java.sql.*;

public class ManajemenCourseCont2 {

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

    private ObservableList<Course> courseList;
    private Connection connection;

    public void initialize() {
        // Establish database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Failed to connect to the database.");
        }

        // Initialize columns in the table
        tableId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableDeskripsi.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Load data into the table
        loadDataIntoTable();

        // Set action for buttons
        tambah.setOnAction(event -> onTambah());
        hapus.setOnAction(event -> onHapus());
        ubah.setOnAction(event -> onUbah());
        search.setOnAction(event -> onSearch());
    }

    private void loadDataIntoTable() {
        courseList = FXCollections.observableArrayList();
        String query = "SELECT * FROM courses";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                Course course = new Course(id, title, description);
                courseList.add(course);
            }
            table.setItems(courseList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve courses from the database.");
        }
    }

    @FXML
    private void onTambah() {
        String courseTitle = title.getText();
        String courseDesc = deskripsi.getText();
        if (!courseTitle.isEmpty() && !courseDesc.isEmpty()) {
            String query = "INSERT INTO courses (title, description) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, courseTitle);
                preparedStatement.setString(2, courseDesc);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    loadDataIntoTable();
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add course.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add course.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Title and description cannot be empty.");
        }
    }

    @FXML
    private void onHapus() {
        Course selectedCourse = table.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            int courseId = selectedCourse.getId();
            String query = "DELETE FROM courses WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, courseId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    loadDataIntoTable();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete course.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete course.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Select a course to delete.");
        }
    }

    @FXML
    private void onUbah() {
        Course selectedCourse = table.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            int courseId = selectedCourse.getId();
            String newTitle = title.getText();
            String newDesc = deskripsi.getText();
            if (!newTitle.isEmpty() && !newDesc.isEmpty()) {
                String query = "UPDATE courses SET title = ?, description = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, newTitle);
                    preparedStatement.setString(2, newDesc);
                    preparedStatement.setInt(3, courseId);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        loadDataIntoTable();
                        clearFields();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update course.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update course.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Title and description cannot be empty.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Select a course to update.");
        }
    }

    @FXML
    private void onSearch() {
        String keyword = searchField.getText();
        if (!keyword.isEmpty()) {
            courseList.clear(); // Clear current table data
            String query = "SELECT * FROM courses WHERE title LIKE ? OR description LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + keyword + "%");
                preparedStatement.setString(2, "%" + keyword + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    Course course = new Course(id, title, description);
                    courseList.add(course);
                }
                table.setItems(courseList);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to search for courses.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Enter a keyword to search.");
        }
    }

    private void clearFields() {
        title.clear();
        deskripsi.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
