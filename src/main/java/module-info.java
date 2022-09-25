module com.nihar_raut.todolist {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens com.nihar_raut.todolist to javafx.fxml;
    exports com.nihar_raut.todolist;
}