module org.kehadiransiswa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens org.kehadiransiswa.view to javafx.fxml;
    exports org.kehadiransiswa.view;
    opens org.kehadiransiswa to javafx.fxml;
    exports org.kehadiransiswa;
    opens org.kehadiransiswa.data to javafx.fxml;
    exports org.kehadiransiswa.data;

}