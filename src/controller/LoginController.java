package controller;

import dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Usuario;

/**
 * Controller da tela de login do sistema Bah Games
 * 
 * @author Nicolas
 * @version 1.0
 */
public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblMensagem;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleEntrar() {
        String email = txtEmail.getText().trim();
        String senha = txtSenha.getText().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Preencha e-mail e senha!");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(email, senha);
            if (usuario != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
                Parent root = loader.load();

                MainController mainController = loader.getController();
                mainController.setUsuarioLogado(usuario);

                Stage stage = (Stage) txtEmail.getScene().getWindow();
                Scene scene = new Scene(root, 1120, 680);
                if (getClass().getResource("/css/style.css") != null) {
                    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                }
                stage.setTitle("Bah Games - Plataforma de Jogos");
                stage.setScene(scene);
                stage.setResizable(true);
                stage.centerOnScreen();
            } else {
                lblMensagem.setText("E-mail ou senha incorretos.");
            }
        } catch (Exception e) {
            lblMensagem.setText("Erro ao conectar no banco de dados.");
            e.printStackTrace();
        }
    }
}