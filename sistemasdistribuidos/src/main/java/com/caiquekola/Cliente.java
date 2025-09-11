package com.caiquekola;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Cliente {
    private final static int portaServidor = 2304;
    private static Socket cliente;

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        cliente = new Socket("localhost", portaServidor);
        ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
        ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

        List<String[]> dados = inserirDados();
        saida.writeObject(dados);
        saida.flush();
        System.out.println("Dados enviados");
        String[] caminhos = (String[])entrada.readObject();


        saida.close();
        entrada.close();
        System.out.println("Conexão encerrada");
        cliente.close();

        SwingUtilities.invokeLater(() -> {
            com.caiquekola.ui.VisualizadorArquivos.exibirArquivosEmJanela(caminhos);
        });
    }

    public static List<String[]> inserirDados() {
        String nome = JOptionPane.showInputDialog("Insira seu nome: ");
        String cpf = JOptionPane.showInputDialog("Insira seu CPF: ");
        byte idade = Byte.valueOf(JOptionPane.showInputDialog("Insira sua idade: "));
        String mensagem = JOptionPane.showInputDialog("Insira a mensagem: ");

        String cabecalho[] = { "Nome", "CPF", "Idade", "Mensagem" };
        String respostas[] = { nome, cpf, String.valueOf(idade), mensagem };
        List<String[]> dados = new ArrayList<>();
        dados.add(cabecalho);
        dados.add(respostas);
        return dados;
    }
}
