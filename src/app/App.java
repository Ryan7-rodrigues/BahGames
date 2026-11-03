package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Principal.fxml"));
            
            Scene scene = new Scene(root);
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("Bah Games");
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar a tela principal. Verifique o caminho do FXML.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
