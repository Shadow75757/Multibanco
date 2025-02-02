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

        JPanel painelPrincipal = new JPanel(new BorderLayout(50, 50));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JPanel painelLogin = new JPanel(new GridLayout(2, 2, 5, 10));
        painelLogin.setBorder(BorderFactory.createTitledBorder("Dados de Acesso"));

        painelLogin.add(new JLabel("  Número da Conta:"));
        txtNumeroConta = new JTextField(10);
        painelLogin.add(txtNumeroConta);
        painelLogin.add(new JLabel("  Nome do Titular:"));
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

            // Verifica se as credenciais estão corretas
            ContaBancaria contaBancaria = verificarCredenciais(numeroConta, nome);
            if (contaBancaria != null) {
                // Verifica se a conta está bloqueada
                if ("blocked".equals(contaBancaria.getStatus())) {
                    mostrarMensagem("A sua conta está bloqueada. Contacte o seu banco 800 757 740.", false);
                } else {
                    this.dispose(); // Fecha a tela de login
                    SwingUtilities.invokeLater(() -> new TelaPin(contaBancaria).setVisible(true));
                }
            } else {
                mostrarMensagem("Dados de login incorretos! Tente novamente.", false);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarMensagem("Erro ao acessar a base de dados. Tente novamente mais tarde.", false);
        }
    }

    private ContaBancaria verificarCredenciais(int numeroConta, String nome) throws SQLException {
        String sql = "SELECT * FROM contas_bancarias WHERE numero_conta = ? AND titular = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, numeroConta);
            pst.setString(2, nome);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // Cria o objeto ContaBancaria com os dados recuperados do banco
                int id = rs.getInt("id");
                double saldo = rs.getDouble("saldo");
                int pin = rs.getInt("pin");
                String status = rs.getString("status"); // Obtém o status da conta
                return new ContaBancaria(id, numeroConta, nome, saldo, pin, status);
            }
        }
        return null; // Retorna null se as credenciais não forem encontradas
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
