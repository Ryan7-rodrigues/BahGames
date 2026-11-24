package controller;

import dao.CategoriaDAO;
import dao.CompraDAO;
import dao.ConexaoBD;
import dao.JogoDAO;
import dao.UsuarioDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controller principal que gerencia todas as funcionalidades da plataforma Bah Games.
 * 
 * @author Nicolas
 * @version 1.0
 */
public class MainController {

    // Painéis de navegação
    @FXML private VBox paneLoja;
    @FXML private VBox paneBiblioteca;
    @FXML private HBox paneCrudJogos;
    @FXML private VBox paneCrudUsuarios;
    @FXML private Label lblUsuarioLogado;
    @FXML private Button btnMenuJogos;
    @FXML private Button btnMenuUsuarios;

    // Loja (Consulta 1)
    @FXML private TextField txtFiltroTitulo;
    @FXML private ComboBox<Categoria> cbFiltroCategoria;
    @FXML private TextField txtFiltroPrecoMax;
    @FXML private TableView<Jogo> tblLoja;
    @FXML private TableColumn<Jogo, String> colLojaTitulo;
    @FXML private TableColumn<Jogo, String> colLojaCategoria;
    @FXML private TableColumn<Jogo, String> colLojaDescricao;
    @FXML private TableColumn<Jogo, Double> colLojaPreco;

    // Biblioteca (Consulta 2)
    @FXML private TableView<Jogo> tblBiblioteca;
    @FXML private TableColumn<Jogo, String> colBibTitulo;
    @FXML private TableColumn<Jogo, String> colBibCategoria;
    @FXML private TableColumn<Jogo, LocalDate> colBibLancamento;
    @FXML private TableColumn<Jogo, String> colBibDescricao;

    // CRUD Jogos
    @FXML private TextField txtJogoTitulo;
    @FXML private ComboBox<Categoria> cbJogoCategoria;
    @FXML private TextField txtJogoPreco;
    @FXML private DatePicker dpJogoLancamento;
    @FXML private TextField txtJogoDescricao;
    @FXML private TableView<Jogo> tblCrudJogos;
    @FXML private TableColumn<Jogo, Integer> colCrudJogoId;
    @FXML private TableColumn<Jogo, String> colCrudJogoTitulo;
    @FXML private TableColumn<Jogo, String> colCrudJogoCat;
    @FXML private TableColumn<Jogo, Double> colCrudJogoPreco;

    // CRUD Categorias
    @FXML private TextField txtCatNome;
    @FXML private TextField txtCatDescricao;
    @FXML private TableView<Categoria> tblCrudCategorias;
    @FXML private TableColumn<Categoria, Integer> colCrudCatId;
    @FXML private TableColumn<Categoria, String> colCrudCatNome;

    // CRUD Usuários
    @FXML private TextField txtUserNome;
    @FXML private TextField txtUserEmail;
    @FXML private PasswordField txtUserSenha;
    @FXML private ComboBox<TipoPerfil> cbUserPerfil;
    @FXML private TableView<Usuario> tblCrudUsuarios;
    @FXML private TableColumn<Usuario, Integer> colCrudUserId;
    @FXML private TableColumn<Usuario, String> colCrudUserNome;
    @FXML private TableColumn<Usuario, String> colCrudUserEmail;
    @FXML private TableColumn<Usuario, TipoPerfil> colCrudUserPerfil;

    private Usuario usuarioLogado;
    private final JogoDAO jogoDAO = new JogoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final CompraDAO compraDAO = new CompraDAO();

    @FXML
    public void initialize() {
        configurarTabelas();
        cbUserPerfil.setItems(FXCollections.observableArrayList(TipoPerfil.values()));
        carregarDadosIniciais();
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        lblUsuarioLogado.setText("Olá, " + usuario.getNome());
        if (usuario.getTipoPerfil() == TipoPerfil.CLIENTE) {
            btnMenuJogos.setVisible(false);
            btnMenuJogos.setManaged(false);
            btnMenuUsuarios.setVisible(false);
            btnMenuUsuarios.setManaged(false);
        }
        carregarBiblioteca();
    }

    private void configurarTabelas() {
        // Tabela Loja
        colLojaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colLojaCategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategoria().getNome()));
        colLojaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colLojaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        // Tabela Biblioteca
        colBibTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colBibCategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategoria().getNome()));
        colBibLancamento.setCellValueFactory(new PropertyValueFactory<>("dataLancamento"));
        colBibDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        // Tabela CRUD Jogos
        colCrudJogoId.setCellValueFactory(new PropertyValueFactory<>("idJogo"));
        colCrudJogoTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colCrudJogoCat.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategoria().getNome()));
        colCrudJogoPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tblCrudJogos.getSelectionModel().selectedItemProperty().addListener((obs, oldV, j) -> {
            if (j != null) {
                txtJogoTitulo.setText(j.getTitulo());
                txtJogoDescricao.setText(j.getDescricao());
                txtJogoPreco.setText(String.valueOf(j.getPreco()));
                dpJogoLancamento.setValue(j.getDataLancamento());
                cbJogoCategoria.setValue(j.getCategoria());
            }
        });

        // Tabela CRUD Categorias
        colCrudCatId.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colCrudCatNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tblCrudCategorias.getSelectionModel().selectedItemProperty().addListener((obs, oldV, c) -> {
            if (c != null) {
                txtCatNome.setText(c.getNome());
                txtCatDescricao.setText(c.getDescricao());
            }
        });

        // Tabela CRUD Usuários
        colCrudUserId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colCrudUserNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCrudUserEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCrudUserPerfil.setCellValueFactory(new PropertyValueFactory<>("tipoPerfil"));
        tblCrudUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldV, u) -> {
            if (u != null) {
                txtUserNome.setText(u.getNome());
                txtUserEmail.setText(u.getEmail());
                txtUserSenha.setText(u.getSenha());
                cbUserPerfil.setValue(u.getTipoPerfil());
            }
        });
    }

    private void carregarDadosIniciais() {
        try {
            var categorias = FXCollections.observableArrayList(categoriaDAO.listarTodas());
            cbFiltroCategoria.setItems(categorias);
            cbJogoCategoria.setItems(categorias);
            tblCrudCategorias.setItems(categorias);

            var jogos = FXCollections.observableArrayList(jogoDAO.listarTodos());
            tblLoja.setItems(jogos);
            tblCrudJogos.setItems(jogos);

            tblCrudUsuarios.setItems(FXCollections.observableArrayList(usuarioDAO.listarTodos()));
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Falha ao carregar dados: " + e.getMessage());
        }
    }

    // --- NAVEGAÇÃO ---
    private void ocultarTodosPaineis() {
        paneLoja.setVisible(false);
        paneBiblioteca.setVisible(false);
        paneCrudJogos.setVisible(false);
        paneCrudUsuarios.setVisible(false);
    }

    @FXML private void mostrarLoja() { ocultarTodosPaineis(); paneLoja.setVisible(true); carregarDadosIniciais(); }
    @FXML private void mostrarBiblioteca() { ocultarTodosPaineis(); paneBiblioteca.setVisible(true); carregarBiblioteca(); }
    @FXML private void mostrarCrudJogos() { ocultarTodosPaineis(); paneCrudJogos.setVisible(true); carregarDadosIniciais(); }
    @FXML private void mostrarCrudUsuarios() { ocultarTodosPaineis(); paneCrudUsuarios.setVisible(true); carregarDadosIniciais(); }

    // --- LOJA E COMPRA ---
    @FXML
    private void filtrarLoja() {
        try {
            String titulo = txtFiltroTitulo.getText();
            Categoria cat = cbFiltroCategoria.getValue();
            Integer idCat = (cat != null) ? cat.getIdCategoria() : null;
            Double precoMax = txtFiltroPrecoMax.getText().isEmpty() ? null : Double.parseDouble(txtFiltroPrecoMax.getText().replace(",", "."));
            tblLoja.setItems(FXCollections.observableArrayList(jogoDAO.buscarPorFiltro(titulo, idCat, precoMax)));
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.WARNING, "Atenção", "Informe um valor numérico válido para o preço máximo.");
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void limparFiltrosLoja() {
        txtFiltroTitulo.clear();
        cbFiltroCategoria.setValue(null);
        txtFiltroPrecoMax.clear();
        carregarDadosIniciais();
    }

    @FXML
    private void comprarJogoSelecionado() {
        Jogo jogo = tblLoja.getSelectionModel().getSelectedItem();
        if (jogo == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um jogo na tabela para comprar.");
            return;
        }
        try {
            if (compraDAO.usuarioJaPossuiJogo(usuarioLogado.getIdUsuario(), jogo.getIdJogo())) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Aviso", "Você já possui este jogo na sua biblioteca!");
                return;
            }
            compraDAO.registrarCompra(new Compra(usuarioLogado, jogo));
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogo '" + jogo.getTitulo() + "' adquirido com sucesso!");
            carregarBiblioteca();
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao registrar compra: " + e.getMessage());
        }
    }

    private void carregarBiblioteca() {
        if (usuarioLogado == null) return;
        try {
            tblBiblioteca.setItems(FXCollections.observableArrayList(compraDAO.buscarBibliotecaDoUsuario(usuarioLogado.getIdUsuario())));
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    // --- CRUD JOGOS ---
    @FXML
    private void salvarJogo() {
        try {
            if (txtJogoTitulo.getText().isEmpty() || cbJogoCategoria.getValue() == null || txtJogoPreco.getText().isEmpty()) {
                exibirAlerta(Alert.AlertType.WARNING, "Atenção", "Preencha título, categoria e preço.");
                return;
            }
            Jogo j = new Jogo(txtJogoTitulo.getText(), txtJogoDescricao.getText(),
                    Double.parseDouble(txtJogoPreco.getText().replace(",", ".")),
                    dpJogoLancamento.getValue() != null ? dpJogoLancamento.getValue() : LocalDate.now(),
                    cbJogoCategoria.getValue());
            jogoDAO.cadastrar(j);
            limparFormJogo();
            carregarDadosIniciais();
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogo cadastrado!");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar jogo: " + e.getMessage());
        }
    }

    @FXML
    private void atualizarJogo() {
        Jogo selecionado = tblCrudJogos.getSelectionModel().getSelectedItem();
        if (selecionado == null) return;
        try {
            selecionado.setTitulo(txtJogoTitulo.getText());
            selecionado.setDescricao(txtJogoDescricao.getText());
            selecionado.setPreco(Double.parseDouble(txtJogoPreco.getText().replace(",", ".")));
            selecionado.setDataLancamento(dpJogoLancamento.getValue());
            selecionado.setCategoria(cbJogoCategoria.getValue());
            jogoDAO.atualizar(selecionado);
            limparFormJogo();
            carregarDadosIniciais();
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogo atualizado!");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void excluirJogo() {
        Jogo selecionado = tblCrudJogos.getSelectionModel().getSelectedItem();
        if (selecionado == null) return;
        try {
            jogoDAO.excluir(selecionado.getIdJogo());
            limparFormJogo();
            carregarDadosIniciais();
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Jogo excluído!");
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível excluir: " + e.getMessage());
        }
    }

    @FXML
    private void limparFormJogo() {
        txtJogoTitulo.clear();
        txtJogoDescricao.clear();
        txtJogoPreco.clear();
        dpJogoLancamento.setValue(null);
        cbJogoCategoria.setValue(null);
    }

    // --- CRUD CATEGORIAS ---
    @FXML
    private void salvarCategoria() {
        if (txtCatNome.getText().isEmpty()) return;
        try {
            categoriaDAO.cadastrar(new Categoria(txtCatNome.getText(), txtCatDescricao.getText()));
            txtCatNome.clear(); txtCatDescricao.clear();
            carregarDadosIniciais();
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Categoria cadastrada!");
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void atualizarCategoria() {
        Categoria sel = tblCrudCategorias.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            sel.setNome(txtCatNome.getText());
            sel.setDescricao(txtCatDescricao.getText());
            categoriaDAO.atualizar(sel);
            txtCatNome.clear(); txtCatDescricao.clear();
            carregarDadosIniciais();
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Categoria atualizada!");
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void excluirCategoria() {
        Categoria sel = tblCrudCategorias.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            categoriaDAO.excluir(sel.getIdCategoria());
            txtCatNome.clear(); txtCatDescricao.clear();
            carregarDadosIniciais();
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Categoria excluída!");
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Categoria vinculada a jogos não pode ser excluída.");
        }
    }

    // --- CRUD USUÁRIOS ---
    @FXML
    private void salvarUsuario() {
        if (txtUserNome.getText().isEmpty() || txtUserEmail.getText().isEmpty() || txtUserSenha.getText().isEmpty() || cbUserPerfil.getValue() == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Atenção", "Preencha todos os campos do usuário.");
            return;
        }
        try {
            usuarioDAO.cadastrar(new Usuario(txtUserNome.getText(), txtUserEmail.getText(), txtUserSenha.getText(), cbUserPerfil.getValue()));
            limparFormUsuario();
            carregarDadosIniciais();
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Usuário cadastrado!");
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void atualizarUsuario() {
        Usuario sel = tblCrudUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            sel.setNome(txtUserNome.getText());
            sel.setEmail(txtUserEmail.getText());
            sel.setSenha(txtUserSenha.getText());
            sel.setTipoPerfil(cbUserPerfil.getValue());
            usuarioDAO.atualizar(sel);
            limparFormUsuario();
            carregarDadosIniciais();
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Usuário atualizado!");
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void excluirUsuario() {
        Usuario sel = tblCrudUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            usuarioDAO.excluir(sel.getIdUsuario());
            limparFormUsuario();
            carregarDadosIniciais();
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Usuário excluído!");
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void limparFormUsuario() {
        txtUserNome.clear(); txtUserEmail.clear(); txtUserSenha.clear(); cbUserPerfil.setValue(null);
    }

    // --- RELATÓRIOS COM JASPER ---
    @FXML

    private void gerarRelatorioJogos() {
        abrirRelatorio("relatorio_jogos.jrxml");
    }

    @FXML
    private void gerarRelatorioVendas() {
        abrirRelatorio("relatorio_vendas.jrxml");
    }

    private void abrirRelatorio(String nomeArquivo) {
        try (Connection conn = ConexaoBD.getConexao()) {
            String limpo = nomeArquivo.replace("/relatorios/", "");
            InputStream stream = getClass().getResourceAsStream("/relatorios/" + limpo);
            if (stream == null) {
                java.io.File arq = new java.io.File("relatorios/" + limpo);
                if (arq.exists()) {
                    stream = new java.io.FileInputStream(arq);
                }
            }
            if (stream == null) {
                exibirAlerta(Alert.AlertType.WARNING, "Aviso", "Relatório não encontrado: " + limpo);
                return;
            }
            JasperReport report = JasperCompileManager.compileReport(stream);
            JasperPrint print = JasperFillManager.fillReport(report, null, conn);
            JasperViewer.viewReport(print, false);
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro no Relatório", e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleSair() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            Stage stage = (Stage) lblUsuarioLogado.getScene().getWindow();
            stage.setScene(new Scene(root, 420, 460));
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}