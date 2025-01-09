package src;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

public class TelaPin extends JFrame {

    private JPasswordField txtPin;
    private JTextField placeholderField;
    private StringBuilder pinBuilder;

    private ContaBancaria contaBancaria;

    private int tentativasRestantes = 3;
    private JLabel lblTentativas;

    public TelaPin(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;

        if (contaBancaria == null || contaBancaria.getNumeroConta() <= 0 || contaBancaria.getTitular() == null || contaBancaria.getTitular().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, faça login primeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            new TelaLogin().setVisible(true);
            dispose();
            return;
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        pinBuilder = new StringBuilder();

        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.setPreferredSize(new Dimension(400, 300));

        JPanel painelTeclado = new JPanel(new GridLayout(4, 3, 5, 5));
        painelTeclado.setPreferredSize(new Dimension(350, 200));

        txtPin = new JPasswordField(10);
        txtPin.setEditable(false);
        txtPin.setHorizontalAlignment(JTextField.CENTER);
        txtPin.setFont(new Font("Arial", Font.BOLD, 14));

        placeholderField = new JTextField("Insert your pin");
        placeholderField.setEditable(false);
        placeholderField.setHorizontalAlignment(JTextField.CENTER);
        placeholderField.setFont(new Font("Arial", Font.BOLD, 14));
        placeholderField.setForeground(Color.GRAY);
        placeholderField.setBorder(BorderFactory.createEmptyBorder());

        JPanel pinPanel = new JPanel(new BorderLayout());
        pinPanel.add(placeholderField, BorderLayout.CENTER);
        pinPanel.add(txtPin, BorderLayout.CENTER);

        for (String tecla : new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "00"}) {
            JButton btn = new JButton(tecla);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setPreferredSize(new Dimension(60, 60));

            if (tecla.equals(".")) {
                btn.addActionListener(e -> {});
            } else {
                btn.addActionListener(e -> adicionarTecla(tecla));
            }

            painelTeclado.add(btn);
        }

        JPanel painelBotoesColoridos = new JPanel(new GridLayout(4, 1, 5, 5));
        painelBotoesColoridos.setPreferredSize(new Dimension(120, 200));

        JButton btnCancelar = criarBotaoAcao("Cancelar", Color.RED, e -> voltarTelaInicial());
        JButton btnCorrigir = criarBotaoAcao("Corrigir", Color.YELLOW, e -> apagarUltimoCaracter());
        JButton btnEmBranco = new JButton();
        btnEmBranco.setEnabled(false);
        JButton btnConfirmar = criarBotaoAcao("Confirmar", Color.GREEN, e -> validarPin());

        painelBotoesColoridos.add(btnCancelar);
        painelBotoesColoridos.add(btnCorrigir);
        painelBotoesColoridos.add(btnEmBranco);
        painelBotoesColoridos.add(btnConfirmar);

        JPanel painelTecladoComBotoes = new JPanel(new BorderLayout(10, 10));
        painelTecladoComBotoes.add(painelTeclado, BorderLayout.CENTER);
        painelTecladoComBotoes.add(painelBotoesColoridos, BorderLayout.EAST);

        painelCentral.add(pinPanel, BorderLayout.NORTH);
        painelCentral.add(painelTecladoComBotoes, BorderLayout.CENTER);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        painelPrincipal.setPreferredSize(new Dimension(400, 300));

        JPanel painelContaInfo = new JPanel();
        painelContaInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JLabel lblNumeroConta = new JLabel("Conta: " + contaBancaria.getNumeroConta());
        JLabel lblNomeTitular = new JLabel("Titular: " + contaBancaria.getTitular());
        painelContaInfo.add(lblNumeroConta);
        painelContaInfo.add(lblNomeTitular);

        painelPrincipal.add(painelContaInfo, BorderLayout.NORTH);

        lblTentativas = new JLabel("Tentativas restantes: " + tentativasRestantes);
        lblTentativas.setFont(new Font("Arial", Font.BOLD, 14));
        lblTentativas.setForeground(Color.RED);
        lblTentativas.setVisible(false);
        painelPrincipal.add(lblTentativas, BorderLayout.SOUTH);

        add(painelPrincipal, BorderLayout.CENTER);

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        getContentPane().add(painelPrincipal, constraints);

        txtPin.requestFocusInWindow();
        placeholderField.setVisible(true);

        txtPin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (pinBuilder.length() > 0 && placeholderField.isVisible()) {
                    placeholderField.setVisible(false);
                }
            }
        });

        txtPin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (pinBuilder.length() == 0 && !placeholderField.isVisible()) {
                    placeholderField.setVisible(true);
                }
            }
        });

        setVisible(true);
    }

    private JButton criarBotaoAcao(String texto, Color cor, java.awt.event.ActionListener acao) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(cor);
        btn.setPreferredSize(new Dimension(90, 50));
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
        if (pinBuilder.length() != 4) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um PIN de 4 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int pin = Integer.parseInt(pinBuilder.toString());
        if (contaBancaria.getPin() == pin) {
            JOptionPane.showMessageDialog(this, "PIN válido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            new TelaCaixaEletronico(contaBancaria).setVisible(true);
            dispose();
        } else {
            tentativasRestantes--;

            JOptionPane.showMessageDialog(this, "PIN incorreto, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);

            pinBuilder.setLength(0);
            txtPin.setText("");

            if (tentativasRestantes <= 0) {
                JOptionPane.showMessageDialog(this, "Tentativas esgotadas. A sua conta foi bloqueada, contacte o seu banco 800 757 740.", "Erro", JOptionPane.ERROR_MESSAGE);
                bloquearConta();
                new TelaInicial().setVisible(true); // Redirect to TelaLogin after blocking account
                dispose();
            } else {
                lblTentativas.setText("Tentativas restantes: " + tentativasRestantes);
                lblTentativas.setVisible(true);
            }
        }
    }

    private void bloquearConta() {
        String query = "UPDATE contas_bancarias SET status = 'blocked' WHERE numero_conta = " + contaBancaria.getNumeroConta();

        try (Connection connection = DatabaseConnection.getConnection(); Statement statement = connection.createStatement()) {
            int rowsUpdated = statement.executeUpdate(query);
            if (rowsUpdated > 0) {
                System.out.println("Conta bloqueada com sucesso.");
            } else {
                System.err.println("Erro ao bloquear a conta.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o status da conta: " + e.getMessage());
        }
    }

    private void voltarTelaInicial() {
        new TelaLogin().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        new TelaPin(new ContaBancaria(41, 143456, "José", 10000.0, 1234, "OK"));
    }
}
