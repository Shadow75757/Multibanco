package src;
// Importação das bibliotecas necessárias do Swing para interface gráfica

import java.awt.*;
import javax.swing.*;

/**
 * Classe principal que implementa a interface gráfica do caixa eletrônico Herda
 * de JFrame para criar uma janela principal
 */
public class TelaCaixaEletronico extends JFrame {

    // Atributos privados para gerenciar a conta e componentes da interface
    private ContaBancaria conta;  // Referência à conta bancária do usuário
    private JTextField txtSaldo;  // Campo para exibir o saldo
    private JTextField txtValor;  // Campo para inserir valores de transação
    private JLabel lblMensagem;   // Label para mensagens de feedback
    private JPanel painelMensagem;  // Painel para exibir mensagens

    // Construtor que recebe uma conta bancária e configura a interface
    public TelaCaixaEletronico(ContaBancaria conta) {
        // Armazena a conta bancária recebida
        this.conta = conta;

        // Configurações básicas da janela
        setTitle("Multibanco - ISTEC");  // Título da janela
        setSize(600, 600);  // Tamanho da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Comportamento ao fechar
        setLayout(new BorderLayout(5, 5));  // Layout principal
        setLocationRelativeTo(null);  // Centraliza a janela

        // Painel com informações do cliente em grid layout
        JPanel painelCliente = new JPanel(new GridLayout(3, 2, 5, 5));
        painelCliente.setBorder(BorderFactory.createTitledBorder("Informações do Cliente"));
        // Adiciona labels com informações da conta
        painelCliente.add(new JLabel("Número da Conta:"));
        painelCliente.add(new JLabel(String.valueOf(conta.getNumeroConta())));
        painelCliente.add(new JLabel("Titular:"));
        painelCliente.add(new JLabel(conta.getTitular()));
        painelCliente.add(new JLabel("Saldo Atual:"));

        // Configuração do campo de saldo (somente leitura)
        txtSaldo = new JTextField();
        txtSaldo.setEditable(false);
        txtSaldo.setHorizontalAlignment(JTextField.RIGHT);
        painelCliente.add(txtSaldo);

        // Painel de operações
        JPanel painelOperacoes = new JPanel(new BorderLayout(5, 5));
        painelOperacoes.setBorder(BorderFactory.createTitledBorder("Operações"));

        // Subpainel para entrada de valor
        JPanel painelValor = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelValor.add(new JLabel("Valor:"));
        txtValor = new JTextField(10);
        painelValor.add(txtValor);
        painelOperacoes.add(painelValor, BorderLayout.NORTH);

        // Botões para operações (Sacar, Depositar, Saldo)
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSacar = new JButton("Sacar");
        JButton btnDepositar = new JButton("Depositar");
        JButton btnSaldo = new JButton("Consultar Saldo");
        painelBotoes.add(btnSacar);
        painelBotoes.add(btnDepositar);
        painelBotoes.add(btnSaldo);
        painelOperacoes.add(painelBotoes, BorderLayout.CENTER);

        // Configura o painel de mensagens de feedback
        painelMensagem = new JPanel();
        lblMensagem = new JLabel(" ");
        painelMensagem.add(lblMensagem);

        // Adiciona os painéis à janela principal
        add(painelCliente, BorderLayout.NORTH);
        add(painelOperacoes, BorderLayout.CENTER);
        add(painelMensagem, BorderLayout.SOUTH);

        // Exibe o saldo atual da conta
        atualizarSaldo();

        // Configurações de ação para os botões
        btnSaldo.addActionListener(e -> consultarSaldo());
        btnSacar.addActionListener(e -> sacar());
        btnDepositar.addActionListener(e -> depositar());
    }

    // Método para atualizar o campo de saldo com o valor atual
    private void atualizarSaldo() {
        txtSaldo.setText(String.format("€ %.2f", conta.getSaldo()));
    }

    // Método para consultar o saldo (sem efeito financeiro)
    private void consultarSaldo() {
        mostrarMensagem("Saldo atual: € " + conta.getSaldo(), true);
    }

    // Método para realizar o saque
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

    // Método para realizar o depósito
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

    // Método para exibir mensagens no painel de mensagens
    private void mostrarMensagem(String mensagem, boolean sucesso) {
        lblMensagem.setText(mensagem);
        if (sucesso) {
            painelMensagem.setBackground(new Color(200, 255, 200));  // Verde claro
        } else {
            painelMensagem.setBackground(new Color(255, 200, 200));  // Vermelho claro
        }
    }

    // Método principal para iniciar a aplicação
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCaixaEletronico(new ContaBancaria(143456,"José", 1000.0)));
    }
}
