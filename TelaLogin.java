// Importação das bibliotecas necessárias para criar interfaces gráficas com Swing
import javax.swing.*;
import java.awt.*;

// Definição da classe TelaLogin que herda de JFrame, criando a janela principal de login
public class TelaLogin extends JFrame {
    // Declaração dos componentes de interface gráfica
    private JTextField txtNumeroConta;  // Campo para inserir o número da conta
    private JTextField txtNome;         // Campo para inserir o nome do titular
    private JLabel lblMensagem;         // Label para exibir mensagens de status
    private JPanel painelMensagem;      // Painel que contém a mensagem

    // Construtor da classe que configura toda a interface gráfica
    public TelaLogin() {
        // Configurações básicas da janela
        setTitle("Login - Multibanco ISTEC");  // Define o título da janela
        setSize(400, 300);  // Define o tamanho da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Define o comportamento ao fechar
        setLocationRelativeTo(null);  // Centraliza a janela na tela
        setLayout(new BorderLayout(5, 5));  // Define o layout da janela

        // Cria um painel principal com padding para melhor organização visual
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Cria um painel de login com layout de grade para organizar os campos
        JPanel painelLogin = new JPanel(new GridLayout(2, 2, 5, 10));
        painelLogin.setBorder(BorderFactory.createTitledBorder("Dados de Acesso"));

        // Adiciona os campos de entrada para número da conta e nome
        painelLogin.add(new JLabel("Número da Conta:"));
        txtNumeroConta = new JTextField(10);
        painelLogin.add(txtNumeroConta);
        painelLogin.add(new JLabel("Nome do Titular:"));
        txtNome = new JTextField(20);
        painelLogin.add(txtNome);

        // Cria o botão de login com estilização personalizada
        JButton btnLogin = new JButton("Entrar");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(120, 40));

        // Configura o painel de mensagens para exibir feedback ao usuário
        painelMensagem = new JPanel();
        painelMensagem.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        lblMensagem = new JLabel(" ");
        lblMensagem.setHorizontalAlignment(JLabel.CENTER);
        painelMensagem.add(lblMensagem);

        // Adiciona os componentes ao painel principal
        painelPrincipal.add(painelLogin, BorderLayout.CENTER);
        painelPrincipal.add(btnLogin, BorderLayout.SOUTH);

        // Adiciona os painéis à janela
        add(painelPrincipal, BorderLayout.CENTER);
        add(painelMensagem, BorderLayout.SOUTH);

        // Configura o evento de clique do botão de login
        btnLogin.addActionListener(e -> realizarLogin());

        // Torna a janela visível
        setVisible(true);
    }

    // Método para realizar o processo de login
    private void realizarLogin() {
        try {
            // Obtém os dados dos campos de texto
            int numeroConta = Integer.parseInt(txtNumeroConta.getText().trim());
            String nome = txtNome.getText().trim();

            // Validação básica do nome
            if (nome.isEmpty()) {
                mostrarMensagem("Por favor, insira o nome do titular!", false);
                return;
            }

            // Cria uma nova conta bancária com os dados fornecidos
            ContaBancaria conta = new ContaBancaria(numeroConta, nome, 1000.0); // Saldo inicial fixo
           
            // Fecha a tela de login
            this.dispose();
           
            // Abre a tela do caixa eletrônico
            SwingUtilities.invokeLater(() -> new TelaCaixaEletronico(conta));
        } catch (NumberFormatException ex) {
            mostrarMensagem("Número de conta inválido!", false);
        }
    }

    // Método para exibir mensagens de feedback com cores diferentes
    private void mostrarMensagem(String mensagem, boolean sucesso) {
        lblMensagem.setText(mensagem);
        if (sucesso) {
            painelMensagem.setBackground(new Color(200, 255, 200));  // Verde claro para sucesso
        } else {
            painelMensagem.setBackground(new Color(255, 200, 200));  // Vermelho claro para erro
        }
    }

    // Método principal que inicializa a aplicação
    public static void main(String[] args) {
        try {
            // Define o estilo de interface de acordo com o sistema operacional
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Inicializa a tela de login de forma segura na Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new TelaLogin());
    }
}