@echo off
start javaw -Djava.library.path="javafx-lib" --enable-native-access=javafx.graphics --module-path "javafx-lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics -jar BahGames.jar