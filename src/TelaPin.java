package src;

import java.awt.*;
import javax.swing.*;

public class TelaPin extends JFrame {

    private JPasswordField txtPin;
    private StringBuilder pinBuilder;

    public TelaPin(int numeroConta, String nome) {
        // Make window fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove the title bar

        pinBuilder = new StringBuilder();

        // Painel central com o painel numérico e os botões de ação
        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.setPreferredSize(new Dimension(400, 300)); // Fixed size for the content

        // Teclado numérico com tamanho reduzido
        JPanel painelTeclado = new JPanel(new GridLayout(4, 3, 5, 5));
        painelTeclado.setPreferredSize(new Dimension(350, 200)); // Reduced size for numeric keypad

        // Campo para o PIN
        txtPin = new JPasswordField(10);
        txtPin.setEditable(false);
        txtPin.setHorizontalAlignment(JTextField.CENTER);
        txtPin.setFont(new Font("Arial", Font.BOLD, 14)); // Smaller font size

        // Adicionar teclas ao painel de teclado
        for (String tecla : new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "00"}) {
            JButton btn = new JButton(tecla);
            btn.setFont(new Font("Arial", Font.BOLD, 12)); // Smaller font size
            btn.setPreferredSize(new Dimension(60, 60)); // Smaller buttons
            btn.addActionListener(e -> adicionarTecla(tecla));
            painelTeclado.add(btn);
        }

        // Painel para os botões coloridos (ao lado dos números)
        JPanel painelBotoesColoridos = new JPanel(new GridLayout(4, 1, 5, 5));
        painelBotoesColoridos.setPreferredSize(new Dimension(120, 200)); // Increased width for color buttons

        // Botões coloridos (ao lado das linhas numéricas)
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

        painelCentral.add(txtPin, BorderLayout.NORTH);
        painelCentral.add(painelTecladoComBotoes, BorderLayout.CENTER);

        // Create main panel for centralization
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        painelPrincipal.setPreferredSize(new Dimension(400, 300)); // Keep content fixed size

        add(painelPrincipal, BorderLayout.CENTER);

        // Center the content in the middle of the screen
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        getContentPane().add(painelPrincipal, constraints);

        // Exibir a janela
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
            pinBuilder.append(tecla);
            txtPin.setText(pinBuilder.toString());
        }
    }

    private void apagarUltimoCaracter() {
        if (pinBuilder.length() > 0) {
            pinBuilder.deleteCharAt(pinBuilder.length() - 1);
            txtPin.setText(pinBuilder.toString());
        }
    }

    private void validarPin() {
        if (pinBuilder.toString().length() == 4) {
            JOptionPane.showMessageDialog(this, "PIN Validado com sucesso!");
            dispose();
            // Navigate to the next screen, e.g., TelaCaixaEletronico
        } else {
            JOptionPane.showMessageDialog(this, "O PIN deve conter 4 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltarTelaInicial() {
        this.dispose();
        SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true)); // Redirect to TelaInicial
    }
}
