package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import src.DatabaseConnection;

public class TelaLogin extends JFrame {

    private JTextField txtNumeroConta;
    private JTextField txtNome;
    private JPasswordField txtPin;
    private JLabel lblMensagem;
    private JPanel painelMensagem;
    private JButton btnLogin;

    public TelaLogin() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel painelLogin = new JPanel(new GridLayout(3, 2, 5, 10));
        painelLogin.setBorder(BorderFactory.createTitledBorder("Dados de Acesso"));

        painelLogin.add(new JLabel("Número da Conta:"));
        txtNumeroConta = new JTextField(10);
        painelLogin.add(txtNumeroConta);
        painelLogin.add(new JLabel("Nome do Titular:"));
        txtNome = new JTextField(20);
        painelLogin.add(txtNome);
        painelLogin.add(new JLabel("PIN (4 dígitos):"));
        txtPin = new JPasswordField(4);
        painelLogin.add(txtPin);

        btnLogin = new JButton("Entrar");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(120, 40));

        painelMensagem = new JPanel();
        painelMensagem.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        lblMensagem = new JLabel(" ");
        lblMensagem.setHorizontalAlignment(JLabel.CENTER);
        painelMensagem.add(lblMensagem);

        painelPrincipal.add(painelLogin, BorderLayout.CENTER);
        painelPrincipal.add(btnLogin, BorderLayout.SOUTH);
        add(painelPrincipal, BorderLayout.CENTER);
        add(painelMensagem, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> realizarLogin());

        setVisible(true);
    }

    private void realizarLogin() {
        try {
            String numeroContaStr = txtNumeroConta.getText().trim();
            String nome = txtNome.getText().trim();
            String pinDigitado = new String(txtPin.getPassword()).trim();

            if (numeroContaStr.isEmpty() || nome.isEmpty() || pinDigitado.isEmpty()) {
                mostrarMensagem("Por favor, preencha todos os campos!", false);
                return;
            }

            // Verifica se o número da conta é válido
            int numeroConta = Integer.parseInt(numeroContaStr);

            // Recupera os dados do banco de dados para verificar se a conta já existe
            if (verificarContaExistente(numeroConta, nome)) {
                // Conta existente: Verifica o PIN
                String pinEncriptado = recuperarPinEncriptadoDaBase(numeroConta, nome);

                if (BCrypt.checkpw(pinDigitado, pinEncriptado)) {
                    // PIN correto, login bem-sucedido
                    ContaBancaria conta = new ContaBancaria(numeroConta, nome, 10000.0);
                    this.dispose();
                    SwingUtilities.invokeLater(() -> new TelaCaixaEletronico(conta));
                } else {
                    mostrarMensagem("PIN incorreto! Tente novamente.", false);
                }
            } else {
                // Conta não existe: Cria nova conta com o PIN encriptado
                criarNovaConta(numeroConta, nome, pinDigitado);
                mostrarMensagem("Conta criada com sucesso! Agora faça o login.", true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarMensagem("Erro ao acessar a base de dados", false);
        } catch (NumberFormatException ex) {
            mostrarMensagem("Número da conta inválido.", false);
        }
    }

    private boolean verificarContaExistente(int numeroConta, String nome) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE numero_conta = ? AND nome = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, numeroConta);
            pst.setString(2, nome);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private String recuperarPinEncriptadoDaBase(int numeroConta, String nome) throws SQLException {
        String pinEncriptado = null;
        String sql = "SELECT password FROM users WHERE numero_conta = ? AND nome = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, numeroConta);
            pst.setString(2, nome);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                pinEncriptado = rs.getString("password");
            }
        }
        return pinEncriptado;
    }

    private void criarNovaConta(int numeroConta, String nome, String pin) throws SQLException {
        String pinEncriptado = BCrypt.hashpw(pin, BCrypt.gensalt());
        String sql = "INSERT INTO users (numero_conta, nome, password, saldo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, numeroConta);
            pst.setString(2, nome);
            pst.setString(3, pinEncriptado);
            pst.setDouble(4, 10000.0); // Saldo inicial de 10,000
            pst.executeUpdate();
        }
    }

    private void mostrarMensagem(String mensagem, boolean sucesso) {
        lblMensagem.setText(mensagem);
        if (sucesso) {
            painelMensagem.setBackground(new Color(200, 255, 200)); // Verde claro para sucesso
        } else {
            painelMensagem.setBackground(new Color(255, 200, 200)); // Vermelho claro para erro
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new TelaLogin());
    }
}
