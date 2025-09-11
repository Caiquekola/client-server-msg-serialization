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

public class Cliente {
    private final static int portaServidor = 2304;
    private static Socket cliente;

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        cliente = new Socket("localhost", portaServidor);
        List<String[]> dados = inserirDados();
        enviarDadosServer(dados);

        cliente.close();
    }

    public static void enviarDadosServer(List<String[]> dados) throws UnknownHostException, IOException, ClassNotFoundException {
        ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
        ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

        // Teste para comunicação
        Date data_atual = (Date) entrada.readObject();
        JOptionPane.showMessageDialog(null, "Data recebida do servidor" + data_atual, "Mensagem do servidor",
                JOptionPane.CANCEL_OPTION);
        saida.writeObject(dados);
        saida.flush();
        System.out.println("Dados enviados");
        saida.close();
        entrada.close();
        System.out.println("Conexão encerrada");
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
