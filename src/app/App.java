package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principal de inicialização do sistema Bah Games.
 * 
 * @author Ryan, Nicolas e Matheus Ryan
 * @version 1.0
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    	dao.ConexaoBD.inicializarBanco();
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
        Scene scene = new Scene(root, 420, 460);
        if (getClass().getResource("/css/style.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        }
        primaryStage.setTitle("Bah Games - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}