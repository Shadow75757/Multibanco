package src;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.*;

public class TelaPin extends JFrame {

    private JPasswordField txtPin;
    private JTextField placeholderField;
    private StringBuilder pinBuilder;

    private int numeroConta;
    private String nome;

    public TelaPin(int numeroConta, String nome) {
        this.numeroConta = numeroConta;
        this.nome = nome;

        // Verificar se o login foi feito corretamente (conta e nome)
        if (numeroConta <= 0 || nome == null || nome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, faça login primeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            // Redireciona para a TelaLogin
            new TelaLogin().setVisible(true);
            dispose(); // Fecha a tela atual
            return;
        }

        // Make window fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove the title bar

        pinBuilder = new StringBuilder();

        // Central panel with numeric keypad and action buttons
        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.setPreferredSize(new Dimension(400, 300)); // Fixed size for the content

        // Numeric keypad with reduced size
        JPanel painelTeclado = new JPanel(new GridLayout(4, 3, 5, 5));
        painelTeclado.setPreferredSize(new Dimension(350, 200)); // Reduced size for numeric keypad

        // Field for PIN (to show the pin the user enters)
        txtPin = new JPasswordField(10);
        txtPin.setEditable(false);
        txtPin.setHorizontalAlignment(JTextField.CENTER);
        txtPin.setFont(new Font("Arial", Font.BOLD, 14)); // Smaller font size

        // Placeholder JTextField (acts as the placeholder text)
        placeholderField = new JTextField("Insert your pin");
        placeholderField.setEditable(false);
        placeholderField.setHorizontalAlignment(JTextField.CENTER);
        placeholderField.setFont(new Font("Arial", Font.BOLD, 14));
        placeholderField.setForeground(Color.GRAY); // Placeholder color
        placeholderField.setBorder(BorderFactory.createEmptyBorder()); // No border for the placeholder

        // Add the PIN and placeholder to a panel to overlay them
        JPanel pinPanel = new JPanel(new BorderLayout());
        pinPanel.add(placeholderField, BorderLayout.CENTER);
        pinPanel.add(txtPin, BorderLayout.CENTER);

        // Add keys to numeric keypad
        for (String tecla : new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "00"}) {
            JButton btn = new JButton(tecla);
            btn.setFont(new Font("Arial", Font.BOLD, 12)); // Smaller font size
            btn.setPreferredSize(new Dimension(60, 60)); // Smaller buttons
            
            // Check if the button is the period button
            if (tecla.equals(".")) {
                btn.addActionListener(e -> {
                    // Do nothing when period button is clicked
                });
            } else {
                btn.addActionListener(e -> adicionarTecla(tecla));
            }
            
            painelTeclado.add(btn);
        }

        // Panel for colored action buttons (next to the number lines)
        JPanel painelBotoesColoridos = new JPanel(new GridLayout(4, 1, 5, 5));
        painelBotoesColoridos.setPreferredSize(new Dimension(120, 200)); // Increased width for color buttons

        // Colored action buttons
        JButton btnCancelar = criarBotaoAcao("Cancelar", Color.RED, e -> voltarTelaInicial());
        JButton btnCorrigir = criarBotaoAcao("Corrigir", Color.YELLOW, e -> apagarUltimoCaracter());
        JButton btnEmBranco = new JButton(); // Empty button
        btnEmBranco.setEnabled(false); // Non-clickable button
        JButton btnConfirmar = criarBotaoAcao("Confirmar", Color.GREEN, e -> validarPin());

        // Add colored buttons to the panel
        painelBotoesColoridos.add(btnCancelar);  // Row 1 2 3
        painelBotoesColoridos.add(btnCorrigir);  // Row 4 5 6
        painelBotoesColoridos.add(btnEmBranco);  // Row 7 8 9
        painelBotoesColoridos.add(btnConfirmar); // Row . 0 00

        // Organize the central panel to include the keypad and colored buttons
        JPanel painelTecladoComBotoes = new JPanel(new BorderLayout(10, 10));
        painelTecladoComBotoes.add(painelTeclado, BorderLayout.CENTER);
        painelTecladoComBotoes.add(painelBotoesColoridos, BorderLayout.EAST); // Place colored buttons on the right

        painelCentral.add(pinPanel, BorderLayout.NORTH); // Pin panel with placeholder and password field
        painelCentral.add(painelTecladoComBotoes, BorderLayout.CENTER);

        // Create main panel for centralization
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        painelPrincipal.setPreferredSize(new Dimension(400, 300)); // Keep content fixed size

        // New panel for showing account number and name
        JPanel painelContaInfo = new JPanel();
        painelContaInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Center aligned
        JLabel lblNumeroConta = new JLabel("Conta: " + numeroConta);
        JLabel lblNomeTitular = new JLabel("Titular: " + nome);
        painelContaInfo.add(lblNumeroConta);
        painelContaInfo.add(lblNomeTitular);

        // Add the info panel above the PIN panel
        painelPrincipal.add(painelContaInfo, BorderLayout.NORTH);

        add(painelPrincipal, BorderLayout.CENTER);

        // Center the content in the middle of the screen
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        getContentPane().add(painelPrincipal, constraints);

        // Focus on the PIN field when the window loads
        txtPin.requestFocusInWindow();
        placeholderField.setVisible(true);

        // Add a listener to detect changes in the PIN field
        txtPin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Hide placeholder text when typing starts
                if (pinBuilder.length() > 0 && placeholderField.isVisible()) {
                    placeholderField.setVisible(false);
                }
            }
        });

        // Add a listener to detect when the field is cleared
        txtPin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Show placeholder text when the field is cleared
                if (pinBuilder.length() == 0 && !placeholderField.isVisible()) {
                    placeholderField.setVisible(true);
                }
            }
        });

        // Display the window
        setVisible(true);
    }

    private JButton criarBotaoAcao(String texto, Color cor, java.awt.event.ActionListener acao) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 12)); // Smaller font size
        btn.setBackground(cor);
        btn.setPreferredSize(new Dimension(90, 50)); // Slightly larger than numeric buttons
        btn.addActionListener(acao);
        return btn;
    }

    private void adicionarTecla(String tecla) {
        if (pinBuilder.length() < 4) {
            if (tecla.equals("00")) {
                if (pinBuilder.length() <= 2) {
                    pinBuilder.append(tecla);
                    txtPin.setText(pinBuilder.toString());
                }
            } else {
                pinBuilder.append(tecla);
                txtPin.setText(pinBuilder.toString());
            }
        }
    }

    private void apagarUltimoCaracter() {
        if (pinBuilder.length() > 0) {
            pinBuilder.deleteCharAt(pinBuilder.length() - 1);
            txtPin.setText(pinBuilder.toString());
        }
    }

    private void validarPin() {
        String pin = pinBuilder.toString();
        
        // Check if the PIN is 4 digits long
        if (pin.length() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor, insira o PIN.", "Erro", JOptionPane.ERROR_MESSAGE);
        } else if (pin.length() < 4) {
            JOptionPane.showMessageDialog(this, "O PIN deve conter 4 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            // Check if the PIN matches the account in the database
            if (verificarPin(numeroConta, nome, pin)) {
                JOptionPane.showMessageDialog(this, "PIN Validado com sucesso!");
                dispose();
                // Close the current window
                System.exit(0);  // Exit the program
            } else {
                JOptionPane.showMessageDialog(this, "PIN incorreto, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                new TelaLogin().setVisible(true);
                dispose();
            }
        }
    }

    private boolean verificarPin(int numeroConta, String nome, String pin) {
        // Connect to the database and verify the PIN
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT pin FROM contas_bancarias WHERE numero_conta = ? AND titular = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, numeroConta);
                stmt.setString(2, nome);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return pin.equals(rs.getString("pin"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void voltarTelaInicial() {
        // Redireciona para a TelaInicial
        JOptionPane.showMessageDialog(this, "Retornando à tela inicial...");
        dispose(); // Fecha a tela atual
        new TelaLogin().setVisible(true);
    }

    public static void main(String[] args) {
        new TelaPin(12345, "João da Silva"); // Test example with dummy account number and name
    }
}
