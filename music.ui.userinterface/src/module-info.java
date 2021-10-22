module music.ui.userinterface{
    requires javafx.controls;
    requires javafx.fxml;
    requires music.ui.db;

    opens music.ui.userinteface;

    exports music.ui.userinteface;
}