package com.caiquekola;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;

public class Server {
    public final static int portaServidor = 2304;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket server = new ServerSocket(portaServidor);
        System.out.println("Servidor iniciado na porta: " + portaServidor);
        while (true) {
            Socket cliente = server.accept();
            System.out.println("Cliente conectado:" + cliente.getInetAddress().getHostAddress());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(new Date());
            saida.flush();

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            List<String[]> dadosCliente = (List<String[]>) entrada.readObject();
            dadosCliente.stream().forEach(linha -> linha.forEach);

            // entrada.close();
            // saida.close();
            // cliente.close();

        }
    }
}