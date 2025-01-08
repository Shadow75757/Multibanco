package src;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class TelaLogin extends JFrame {

    private JTextField txtNumeroConta;
    private JTextField txtNome;
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

        JPanel painelLogin = new JPanel(new GridLayout(2, 2, 5, 10));
        painelLogin.setBorder(BorderFactory.createTitledBorder("Dados de Acesso"));

        painelLogin.add(new JLabel("Número da Conta:"));
        txtNumeroConta = new JTextField(10);
        painelLogin.add(txtNumeroConta);
        painelLogin.add(new JLabel("Nome do Titular:"));
        txtNome = new JTextField(20);
        painelLogin.add(txtNome);

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

            if (numeroContaStr.isEmpty() || nome.isEmpty()) {
                mostrarMensagem("Por favor, preencha todos os campos!", false);
                return;
            }

            int numeroConta;
            try {
                numeroConta = Integer.parseInt(numeroContaStr);
            } catch (NumberFormatException e) {
                mostrarMensagem("Número da conta inválido.", false);
                return;
            }

            if (verificarCredenciais(numeroConta, nome)) {
                this.dispose();
                SwingUtilities.invokeLater(() -> new TelaPin(numeroConta, nome).setVisible(true));
            } else {
                mostrarMensagem("Dados de login incorretos! Tente novamente.", false);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarMensagem("Erro ao acessar a base de dados. Tente novamente mais tarde.", false);
        }
    }

    private boolean verificarCredenciais(int numeroConta, String nome) throws SQLException {
        String sql = "SELECT COUNT(*) FROM contas_bancarias WHERE numero_conta = ? AND titular = ?";
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

    private void mostrarMensagem(String mensagem, boolean sucesso) {
        lblMensagem.setText(mensagem);
        if (sucesso) {
            painelMensagem.setBackground(new Color(200, 255, 200));
        } else {
            painelMensagem.setBackground(new Color(255, 200, 200));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin());
    }
}
