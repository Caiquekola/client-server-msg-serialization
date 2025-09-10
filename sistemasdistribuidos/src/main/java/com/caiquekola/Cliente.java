package com.caiquekola;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.JOptionPane;
public class Cliente {
    public final static int portaServidor = 2304;

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        
        Socket cliente = new Socket("localhost",portaServidor);
        ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
        Date data_atual = (Date)entrada.readObject();
        JOptionPane.showMessageDialog(null,"Data recebida do servidor"+data_atual, "Mensagem do servidor",JOptionPane.CANCEL_OPTION);
        String nome = JOptionPane.showInputDialog("Insira seu nome: ");
        String cpf = JOptionPane.showInputDialog("Insira seu CPF: ");
        byte idade = Byte.valueOf(JOptionPane.showInputDialog("Insira sua idade: "));
        String mensagem = JOptionPane.showInputDialog("Insira a mensagem: ");
        String cabecalho[] = {"Nome","CPF","Idade","Mensagem"};
        String dados[] = {nome,cpf,String.valueOf(idade),mensagem};

        
        entrada.close();
        System.out.println("Conexão encerrada");
        cliente.close();
    }
}
