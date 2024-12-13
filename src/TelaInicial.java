package src;
// Importação das bibliotecas necessárias

import java.awt.*;
import javax.swing.*;

public class TelaInicial extends JFrame {

    public TelaInicial() {
        // Configurações básicas da janela
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Janela em tela cheia
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);

        // Painel principal com fundo cinza claro
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(Color.LIGHT_GRAY);
        painelPrincipal.setLayout(new GridBagLayout()); // Centraliza o conteúdo
        add(painelPrincipal);

        // Painel central (o retângulo)
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new GridLayout(1, 2)); // Dividido em 50% esquerda/direita
        painelCentral.setBackground(Color.WHITE);
        painelCentral.setPreferredSize(new Dimension(800, 400));
        painelCentral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20), // Espaço interno
                BorderFactory.createLineBorder(Color.GRAY, 1)
        ));

        // Adicionando sombra ao redor do retângulo
        painelCentral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Lado esquerdo com a imagem ocupando o lado inteiro
        JLabel imagem = new JLabel(new ImageIcon("icon.png"));
        imagem.setHorizontalAlignment(JLabel.CENTER);
        imagem.setVerticalAlignment(JLabel.CENTER);
        imagem.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Espaçamento em relação à borda
        painelCentral.add(imagem);

        // Lado direito com texto e botão ocupando o lado inteiro
        JPanel painelTexto = new JPanel();
        painelTexto.setLayout(new BorderLayout());
        painelTexto.setBackground(Color.WHITE);
        painelTexto.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Espaço interno

        // Texto centralizado e estilizado
        JLabel texto = new JLabel("<html><div style='text-align: center;'>Insira o seu cartão<br>ISTEC-Bank</div></html>");
        texto.setFont(new Font("Arial", Font.BOLD, 24));
        texto.setHorizontalAlignment(SwingConstants.CENTER);
        painelTexto.add(texto, BorderLayout.CENTER);

        // Botão estilizado na parte inferior
        JButton botao = new JButton("Esqueci-me do cartão em casa");
        botao.setFont(new Font("Arial", Font.PLAIN, 16));
        botao.setPreferredSize(new Dimension(200, 50));
        botao.setBackground(new Color(30, 144, 255)); // Azul claro
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adicionando efeito de hover no botão
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(new Color(0, 120, 215)); // Azul mais escuro
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(new Color(30, 144, 255)); // Azul claro
            }
        });

        painelTexto.add(botao, BorderLayout.SOUTH);

        // Ação do botão para abrir a tela de login
        botao.addActionListener(e -> {
            this.dispose(); // Fecha a tela inicial
            SwingUtilities.invokeLater(TelaLogin::new); // Abre a tela de login
        });

        painelCentral.add(painelTexto);

        // Adiciona o painel central ao painel principal
        painelPrincipal.add(painelCentral);

        // Torna a janela visível
        setVisible(true);
    }

        // Método main para inicializar a aplicação
    public static void main(String[] args) {
        // Usa a Event Dispatch Thread para garantir que a interface gráfica seja atualizada corretamente
        SwingUtilities.invokeLater(() -> new TelaInicial());
    }
}
