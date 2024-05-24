package org.kehadiransiswa.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManajemenCourseApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ManajemenCourseApp.class.getResource("manajemen-courses.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Manajemen Course Kehadiran");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}