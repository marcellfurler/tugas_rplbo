//package org.kehadiransiswa.view;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.control.cell.PropertyValueFactory;
//import org.kehadiransiswa.managers.DBConnectionManager;
////import org.kehadiransiswa.model.ManajemenCourse;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class ManajemenCourseCont {
//
//    @FXML
//    private TextField id;
//
//    @FXML
//    private TextField title;
//
//    @FXML
//    private TextArea deskripsi;
//
//    @FXML
//    private Button tambah;
//
//    @FXML
//    private Button hapus;
//
//    @FXML
//    private Button ubah;
//
//    @FXML
//    private Button search;
//
//    @FXML
//    private TableView<ManajemenCourse> table;
//
//    @FXML
//    private TableColumn<ManajemenCourse, Integer> tableId;
//
//    @FXML
//    private TableColumn<ManajemenCourse, String> tableTitle;
//
//    @FXML
//    private TableColumn<ManajemenCourse, String> tableDeskripsi;
//
//    @FXML
//    private TextField searchField;
//
//    private ObservableList<ManajemenCourse> manajemenCourse;
//
//    @FXML
//    private void initialize() {
//        tableId.setCellValueFactory(new PropertyValueFactory<>("id"));
//        tableTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
//        tableDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
//
//        manajemenCourse = FXCollections.observableArrayList();
//        table.setItems(manajemenCourse);
//
//        loadCoursesFromDatabase();
//    }
//
//    @FXML
//    private void onTambah() {
//        try {
//            Integer courseId = Integer.parseInt(id.getText());
//            String courseTitle = title.getText();
//            String courseDeskripsi = deskripsi.getText();
//
//            if (courseTitle.isEmpty() || courseDeskripsi.isEmpty()) {
//                showAlert("Error", "Semua field harus diisi.");
//                return;
//            }
//
//            for (ManajemenCourse course : manajemenCourse) {
//                if (course.getId().equals(courseId)) {
//                    showAlert("Error", "ID Course sudah ada.");
//                    return;
//                }
//            }
//
//            ManajemenCourse newCourse = new ManajemenCourse(courseId, courseTitle, courseDeskripsi);
//            insertCourseToDatabase(newCourse);
//            manajemenCourse.add(newCourse);
//            clearFields();
//        } catch (NumberFormatException e) {
//            showAlert("Error", "ID harus berupa bilangan bulat.");
//        }
//    }
//
//    @FXML
//    private void onHapus() {
//        try {
//            Integer courseId = Integer.parseInt(id.getText());
//
//            if (courseId == null) {
//                showAlert("Error", "ID Course harus diisi.");
//                return;
//            }
//
//            ManajemenCourse courseToRemove = null;
//            for (ManajemenCourse course : manajemenCourse) {
//                if (course.getId().equals(courseId)) {
//                    courseToRemove = course;
//                    break;
//                }
//            }
//
//            if (courseToRemove != null) {
//                deleteCourseFromDatabase(courseToRemove);
//                manajemenCourse.remove(courseToRemove);
//                clearFields();
//            } else {
//                showAlert("Error", "Course tidak ditemukan.");
//            }
//        } catch (NumberFormatException e) {
//            showAlert("Error", "ID harus berupa bilangan bulat.");
//        }
//    }
//
//    @FXML
//    private void onUbah() {
//        try {
//            Integer courseId = Integer.parseInt(id.getText());
//            String courseTitle = title.getText();
//            String courseDeskripsi = deskripsi.getText();
//
//            if (courseTitle.isEmpty() || courseDeskripsi.isEmpty()) {
//                showAlert("Error", "Semua field harus diisi.");
//                return;
//            }
//
//            for (ManajemenCourse course : manajemenCourse) {
//                if (course.getId().equals(courseId)) {
//                    course.setTitle(courseTitle);
//                    course.setDeskripsi(courseDeskripsi);
//                    updateCourseInDatabase(course);
//                    table.refresh();
//                    clearFields();
//                    return;
//                }
//            }
//            showAlert("Error", "Course tidak ditemukan.");
//        } catch (NumberFormatException e) {
//            showAlert("Error", "ID harus berupa bilangan bulat.");
//        }
//    }
//
//    @FXML
//    private void onSearch() {
//        String searchText = searchField.getText().toLowerCase();
//
//        ObservableList<ManajemenCourse> filteredList = FXCollections.observableArrayList();
//
//        for (ManajemenCourse course : manajemenCourse) {
//            if (course.getId().toString().toLowerCase().contains(searchText) ||
//                    course.getTitle().toLowerCase().contains(searchText) ||
//                    course.getDeskripsi().toLowerCase().contains(searchText)) {
//                filteredList.add(course);
//            }
//        }
//
//        table.setItems(filteredList);
//    }
//
//    private void loadCoursesFromDatabase() {
//        try (Connection connection = DBConnectionManager.getConnection()) {
//            String query = "SELECT * FROM courses";
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                Integer courseId = resultSet.getInt("id");
//                String courseTitle = resultSet.getString("title");
//                String courseDeskripsi = resultSet.getString("deskripsi");
//
//                ManajemenCourse course = new ManajemenCourse(courseId, courseTitle, courseDeskripsi);
//                manajemenCourse.add(course);
//            }
//        } catch (SQLException e) {
//            showAlert("Error", "Failed to load courses from database.");
//            e.printStackTrace();
//        }
//    }
//
//    private void insertCourseToDatabase(ManajemenCourse course) {
//        try (Connection connection = DBConnectionManager.getConnection()) {
//            String query = "INSERT INTO courses (id, title, deskripsi) VALUES (?, ?, ?)";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, course.getId());
//            statement.setString(2, course.getTitle());
//            statement.setString(3, course.getDeskripsi());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            showAlert("Error", "Failed to insert course into database.");
//            e.printStackTrace();
//        }
//    }
//
//    private void updateCourseInDatabase(ManajemenCourse course) {
//        try (Connection connection = DBConnectionManager.getConnection()) {
//            String query = "UPDATE courses SET title = ?, deskripsi = ? WHERE id = ?";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setString(1, course.getTitle());
//            statement.setString(2, course.getDeskripsi());
//            statement.setInt(3, course.getId());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            showAlert("Error", "Failed to update course in database.");
//            e.printStackTrace();
//        }
//    }
//
//    private void deleteCourseFromDatabase(ManajemenCourse course) {
//        try (Connection connection = DBConnectionManager.getConnection()) {
//            String query = "DELETE FROM courses WHERE id = ?";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, course.getId());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            showAlert("Error", "Failed to delete course from database.");
//            e.printStackTrace();
//        }
//    }
//
//    private void clearFields() {
//        id.clear();
//        title.clear();
//        deskripsi.clear();
//    }
//
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}
