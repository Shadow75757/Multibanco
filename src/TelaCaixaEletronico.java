package src;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Classe principal que implementa a interface gráfica do caixa eletrônico Herda
 * de JFrame para criar uma janela principal
 */
public class TelaCaixaEletronico extends JFrame {

    private ContaBancaria conta;
    private JTextField txtSaldo;
    private JTextField txtValor;
    private JLabel lblMensagem;
    private JPanel painelMensagem;

    public TelaCaixaEletronico(ContaBancaria conta) {
        this.conta = conta;

        // Configurações básicas da janela
        setTitle("Multibanco - ISTEC");
        setUndecorated(true); // Remove a barra de título
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Tela cheia
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20)); // Maior espaçamento entre os painéis
        setBackground(new Color(245, 245, 245)); // Cor de fundo suave

        // Painel com informações do cliente
        JPanel painelCliente = criarPainelCliente();
        JPanel painelOperacoes = criarPainelOperacoes();

        // Painel de mensagens
        painelMensagem = new JPanel();
        lblMensagem = new JLabel(" ");
        lblMensagem.setFont(new Font("Arial", Font.BOLD, 16));
        painelMensagem.setBackground(new Color(245, 245, 245)); // Cor de fundo leve
        painelMensagem.add(lblMensagem);

        // Adiciona os painéis à janela principal
        add(painelCliente, BorderLayout.NORTH);
        add(painelOperacoes, BorderLayout.CENTER);
        add(painelMensagem, BorderLayout.SOUTH);

        atualizarSaldo();

        setVisible(true);
    }

    private JPanel criarPainelCliente() {
        JPanel painelCliente = new JPanel(new GridLayout(3, 2, 20, 20)); // Maior espaçamento entre os campos
        painelCliente.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2), "Informações do Cliente", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 16), new Color(70, 130, 180)));
        painelCliente.setBackground(new Color(255, 255, 255)); // Fundo branco para contraste

        painelCliente.add(new JLabel("Número da Conta:"));
        painelCliente.add(new JLabel(String.valueOf(conta.getNumeroConta())));
        painelCliente.add(new JLabel("Titular:"));
        painelCliente.add(new JLabel(conta.getTitular()));
        painelCliente.add(new JLabel("Saldo Atual:"));

        txtSaldo = new JTextField();
        txtSaldo.setEditable(false);
        txtSaldo.setHorizontalAlignment(JTextField.RIGHT);
        txtSaldo.setFont(new Font("Arial", Font.PLAIN, 18));
        painelCliente.add(txtSaldo);

        return painelCliente;
    }

    private JPanel criarPainelOperacoes() {
        JPanel painelOperacoes = new JPanel(new BorderLayout(20, 20));
        painelOperacoes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2), "Operações", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 16), new Color(70, 130, 180)));
        painelOperacoes.setBackground(new Color(255, 255, 255)); // Fundo branco para contraste

        JPanel painelValor = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelValor.setBackground(Color.WHITE);
        painelValor.add(new JLabel("Valor:"));
        txtValor = new JTextField(10);
        txtValor.setFont(new Font("Arial", Font.PLAIN, 16));
        painelValor.add(txtValor);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.setBackground(Color.WHITE);
        JButton btnSacar = new JButton("Sacar");
        JButton btnDepositar = new JButton("Depositar");
        JButton btnSaldo = new JButton("Consultar Saldo");

        estilizarBotao(btnSacar);
        estilizarBotao(btnDepositar);
        estilizarBotao(btnSaldo);

        painelBotoes.add(btnSacar);
        painelBotoes.add(btnDepositar);
        painelBotoes.add(btnSaldo);

        painelOperacoes.add(painelValor, BorderLayout.NORTH);
        painelOperacoes.add(painelBotoes, BorderLayout.CENTER);

        btnSaldo.addActionListener(e -> consultarSaldo());
        btnSacar.addActionListener(e -> sacar());
        btnDepositar.addActionListener(e -> depositar());

        return painelOperacoes;
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBackground(new Color(70, 130, 180)); // Azul elegante
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botao.setPreferredSize(new Dimension(150, 40)); // Tamanho adequado para os botões

        // Efeito de hover para os botões
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(new Color(100, 149, 237)); // Azul mais claro ao passar o mouse
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(new Color(70, 130, 180)); // Volta ao azul original
            }
        });
    }

    private void atualizarSaldo() {
        txtSaldo.setText(String.format("€ %.2f", conta.getSaldo()));
    }

    private void consultarSaldo() {
        mostrarMensagem("Saldo atual: € " + conta.getSaldo(), true);
    }

    private void sacar() {
        try {
            double valor = Double.parseDouble(txtValor.getText());
            if (valor <= 0) {
                mostrarMensagem("Digite um valor válido para sacar.", false);
            } else if (valor <= conta.getSaldo()) {
                conta.sacar(valor);
                atualizarSaldo();
                mostrarMensagem("Saque realizado com sucesso! € " + valor, true);
            } else {
                mostrarMensagem("Saldo insuficiente para o saque.", false);
            }
        } catch (NumberFormatException e) {
            mostrarMensagem("Digite um valor válido para o saque.", false);
        }
    }

    private void depositar() {
        try {
            double valor = Double.parseDouble(txtValor.getText());
            if (valor <= 0) {
                mostrarMensagem("Digite um valor válido para depositar.", false);
            } else {
                conta.depositar(valor);
                atualizarSaldo();
                mostrarMensagem("Depósito realizado com sucesso! € " + valor, true);
            }
        } catch (NumberFormatException e) {
            mostrarMensagem("Digite um valor válido para o depósito.", false);
        }
    }

    private void mostrarMensagem(String mensagem, boolean sucesso) {
        lblMensagem.setText(mensagem);
        if (sucesso) {
            painelMensagem.setBackground(new Color(200, 255, 200)); // Cor verde suave para sucesso
        } else {
            painelMensagem.setBackground(new Color(255, 200, 200)); // Cor vermelha suave para erro
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCaixaEletronico(new ContaBancaria(41, 143456, "José", 10000.0, 1234, "OK")));
    }
}
