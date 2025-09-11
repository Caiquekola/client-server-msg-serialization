package com.caiquekola.ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class VisualizadorArquivos {

    public static void exibirArquivosEmJanela(String[] caminhos) {
        JFrame frame = new JFrame("Arquivos gerados pelo servidor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();

        for (String caminho : caminhos) {
            String ext = obterExtensao(caminho).toLowerCase();
            String nomeAba = ext.toUpperCase().replace(".", "");
            if (nomeAba.isEmpty()) nomeAba = "ARQUIVO";

            JComponent panel = criarPainelParaArquivo(caminho, ext);
            abas.addTab(nomeAba, panel);
        }

        frame.setContentPane(abas);
        frame.setVisible(true);
    }

    private static String obterExtensao(String caminho) {
        int i = caminho.lastIndexOf('.');
        return (i >= 0) ? caminho.substring(i) : "";
    }

    private static JComponent criarPainelParaArquivo(String caminho, String ext) {
        JPanel container = new JPanel(new BorderLayout(8, 8));
        container.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel topo = new JLabel("<html><b>Caminho:</b> " + caminho + "</html>");
        container.add(topo, BorderLayout.NORTH);

        try {
            String conteudo = lerArquivoComoString(caminho);

            // Exibe tudo em texto monoespaçado, inclusive CSV
            JTextArea ta = new JTextArea();
            ta.setEditable(false);
            ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
            ta.setText(conteudo);
            ta.setCaretPosition(0);
            ta.setLineWrap(false);       // sem quebra automática, útil p/ CSV
            ta.setWrapStyleWord(false);

            JScrollPane scroll = new JScrollPane(ta);
            container.add(scroll, BorderLayout.CENTER);

        } catch (IOException e) {
            JTextArea erro = new JTextArea("Erro ao ler arquivo:\n" + e.getMessage());
            erro.setEditable(false);
            erro.setForeground(new Color(180, 0, 0));
            container.add(new JScrollPane(erro), BorderLayout.CENTER);
        }

        // Barra de ações (copiar caminho / abrir pasta)
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton copiar = new JButton("Copiar caminho");
        copiar.addActionListener(ae -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    new java.awt.datatransfer.StringSelection(caminho), null);
            JOptionPane.showMessageDialog(container, "Caminho copiado para a área de transferência.");
        });

        JButton abrirPasta = new JButton("Abrir pasta");
        abrirPasta.addActionListener(ae -> abrirNoExplorador(caminho));

        botoes.add(copiar);
        botoes.add(abrirPasta);
        container.add(botoes, BorderLayout.SOUTH);

        return container;
    }

    private static String lerArquivoComoString(String caminho) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(caminho));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static void abrirNoExplorador(String caminho) {
        try {
            Path p = Paths.get(caminho).toAbsolutePath().getParent();
            if (p == null) {
                JOptionPane.showMessageDialog(null, "Não foi possível determinar a pasta do arquivo.");
                return;
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(p.toFile());
            } else {
                JOptionPane.showMessageDialog(null, "Desktop não suportado neste ambiente.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao abrir pasta: " + e.getMessage());
        }
    }
}
