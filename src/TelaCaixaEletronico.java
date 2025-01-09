package src;

import java.awt.*;
import javax.swing.*;

/**
 * Classe principal que implementa a interface gráfica do caixa eletrônico
 * Herda de JFrame para criar uma janela principal
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
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Painel com informações do cliente
        JPanel painelCliente = criarPainelCliente();
        JPanel painelOperacoes = criarPainelOperacoes();

        // Painel de mensagens
        painelMensagem = new JPanel();
        lblMensagem = new JLabel(" ");
        lblMensagem.setFont(new Font("Arial", Font.BOLD, 14));
        painelMensagem.add(lblMensagem);

        // Adiciona os painéis à janela principal
        add(painelCliente, BorderLayout.NORTH);
        add(painelOperacoes, BorderLayout.CENTER);
        add(painelMensagem, BorderLayout.SOUTH);

        atualizarSaldo();

        setVisible(true);
    }

    private JPanel criarPainelCliente() {
        JPanel painelCliente = new JPanel(new GridLayout(3, 2, 10, 10));
        painelCliente.setBorder(BorderFactory.createTitledBorder("Informações do Cliente"));
        painelCliente.setBackground(new Color(245, 245, 245));
        painelCliente.setOpaque(true);

        painelCliente.add(new JLabel("Número da Conta:"));
        painelCliente.add(new JLabel(String.valueOf(conta.getNumeroConta())));
        painelCliente.add(new JLabel("Titular:"));
        painelCliente.add(new JLabel(conta.getTitular()));
        painelCliente.add(new JLabel("Saldo Atual:"));

        txtSaldo = new JTextField();
        txtSaldo.setEditable(false);
        txtSaldo.setHorizontalAlignment(JTextField.RIGHT);
        painelCliente.add(txtSaldo);

        return painelCliente;
    }

    private JPanel criarPainelOperacoes() {
        JPanel painelOperacoes = new JPanel(new BorderLayout(10, 10));
        painelOperacoes.setBorder(BorderFactory.createTitledBorder("Operações"));
        painelOperacoes.setBackground(Color.WHITE);

        JPanel painelValor = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelValor.setBackground(Color.WHITE);
        painelValor.add(new JLabel("Valor:"));
        txtValor = new JTextField(10);
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
            painelMensagem.setBackground(new Color(200, 255, 200));
        } else {
            painelMensagem.setBackground(new Color(255, 200, 200));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCaixaEletronico(new ContaBancaria(41,143456, "José", 10000.0, 1234, "OK")));
    }
}
