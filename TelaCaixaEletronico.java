// Importação das bibliotecas necessárias do Swing para interface gráfica
import javax.swing.*;
// Importação das classes do AWT para layouts e componentes básicos
import java.awt.*;

/**
 * Classe principal que implementa a interface gráfica do caixa eletrônico
 * Herda de JFrame para criar uma janela principal
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
        txtValor = new JTextField(12);
        txtValor.setEditable(false);
        txtValor.setHorizontalAlignment(JTextField.RIGHT);
        painelValor.add(txtValor);

        // Criação do teclado numérico
        JPanel tecladoNumerico = new JPanel(new GridLayout(4, 3, 3, 3));
        String[] teclas = {"7", "8", "9", "4", "5", "6", "1", "2", "3", ".", "0", "←"};
        for (String tecla : teclas) {
            JButton btn = new JButton(tecla);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.setMargin(new Insets(8, 8, 8, 8));
            // Lógica para manipulação das teclas do teclado numérico
            btn.addActionListener(e -> {
                if (tecla.equals("←")) {
                    // Remoção do último caractere
                    String texto = txtValor.getText();
                    if (!texto.isEmpty()) {
                        txtValor.setText(texto.substring(0, texto.length() - 1));
                    }
                } else {
                    // Validações para entrada de valor (decimal)
                    if (tecla.equals(".") && txtValor.getText().contains(".")) return;
                    if (txtValor.getText().contains(".")) {
                        String[] partes = txtValor.getText().split("\\.");
                        if (partes.length > 1 && partes[1].length() >= 2) return;
                    }
                    txtValor.setText(txtValor.getText() + tecla);
                }
            });
            tecladoNumerico.add(btn);
        }

        // Criação dos botões de operação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnLevantar = new JButton("Levantar");
        JButton btnDepositar = new JButton("Depositar");
        JButton btnLimpar = new JButton("Limpar");
        
        // Configurações de estilo dos botões
        Dimension buttonSize = new Dimension(150, 50);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        for (JButton btn : new JButton[]{btnLevantar, btnDepositar, btnLimpar}) {
            btn.setPreferredSize(buttonSize);
            btn.setFont(buttonFont);
            btn.setMargin(new Insets(10, 10, 10, 10));
            btn.setFocusPainted(false);
        }
        painelBotoes.add(btnLevantar);
        painelBotoes.add(btnDepositar);
        painelBotoes.add(btnLimpar);

        // Montagem dos painéis de operações
        painelOperacoes.add(painelValor, BorderLayout.NORTH);
        painelOperacoes.add(tecladoNumerico, BorderLayout.CENTER);
        painelOperacoes.add(painelBotoes, BorderLayout.SOUTH);

        // Painel de mensagens
        painelMensagem = new JPanel();
        painelMensagem.setBorder(BorderFactory.createTitledBorder("Mensagens"));
        lblMensagem = new JLabel(" ");
        lblMensagem.setHorizontalAlignment(JLabel.CENTER);
        painelMensagem.add(lblMensagem);

        // Montagem do painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout(5, 5));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        painelPrincipal.add(painelCliente, BorderLayout.NORTH);
        painelPrincipal.add(painelOperacoes, BorderLayout.CENTER);
        painelPrincipal.add(painelMensagem, BorderLayout.SOUTH);
        add(painelPrincipal);

        // Evento de levantamento
        btnLevantar.addActionListener(e -> {
            try {
                double valor = Double.parseDouble(txtValor.getText());
                if (valor > 0 && valor <= conta.getSaldo()) {
                    conta.levantar(valor);
                    atualizarMensagem("Levantamento realizado com sucesso!", true);
                } else if (valor <= 0) {
                    atualizarMensagem("Valor inválido para levantamento!", false);
                } else {
                    atualizarMensagem("Saldo insuficiente!", false);
                }
                atualizarSaldo();
            } catch (NumberFormatException ex) {
                atualizarMensagem("Por favor, insira um valor válido!", false);
            }
        });

        // Evento de depósito
        btnDepositar.addActionListener(e -> {
            try {
                double valor = Double.parseDouble(txtValor.getText());
                if (valor > 0) {
                    conta.depositar(valor);
                    atualizarMensagem("Depósito realizado com sucesso!", true);
                } else {
                    atualizarMensagem("Valor inválido para depósito!", false);
                }
                atualizarSaldo();
            } catch (NumberFormatException ex) {
                atualizarMensagem("Por favor, insira um valor válido!", false);
            }
        });

        // Evento de limpeza
        btnLimpar.addActionListener(e -> {
            txtValor.setText("");
            lblMensagem.setText(" ");
            painelMensagem.setBackground(null);
        });

        // Atualiza o saldo inicial e torna a janela visível
        atualizarSaldo();
        setVisible(true);
    }

    // Método para atualizar o campo de saldo
    private void atualizarSaldo() {
        txtSaldo.setText(String.format("€ %.2f", conta.getSaldo()));
        txtValor.setText("");
    }

    // Método para atualizar mensagens de feedback
    private void atualizarMensagem(String mensagem, boolean sucesso) {
        lblMensagem.setText(mensagem);
        if (sucesso) {
            painelMensagem.setBackground(new Color(200, 255, 200));  // Verde claro
        } else {
            painelMensagem.setBackground(new Color(255, 200, 200));  // Vermelho claro
        }
    }

    // Método principal para iniciar a aplicação
    public static void main(String[] args) {
        try {
            // Define o look and feel do sistema operacional
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Inicializa a interface do caixa eletrônico
        SwingUtilities.invokeLater(() -> {
            ContaBancaria conta = new ContaBancaria(12345, "Celso Batista", 1000);
            new TelaCaixaEletronico(conta);
        });
    }
}