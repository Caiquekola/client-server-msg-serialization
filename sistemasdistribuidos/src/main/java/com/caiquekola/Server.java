package com.caiquekola;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import com.caiquekola.csv.EscritaCsv;
import com.caiquekola.json.EscritaJson;
import com.caiquekola.toml.EscritaTOML;
import com.caiquekola.xml.EscritaXML;
import com.caiquekola.yaml.EscritaYAML;

public class Server {
    public final static int portaServidor = 2304;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket server = new ServerSocket(portaServidor);
        System.out.println("Servidor iniciado na porta: " + portaServidor);
        while (true) {
            Socket cliente = server.accept();
            System.out.println("Cliente conectado:" + cliente.getInetAddress().getHostAddress());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

            List<String[]> dadosCliente = (List<String[]>) entrada.readObject();
            criarArquivosSerializacao(dadosCliente);
            dadosCliente.stream().forEach(linha -> {
                for (String celula : linha) {
                    System.out.print(celula + ",");
                }
                System.out.println();
            });

            entrada.close();
            saida.close();
            cliente.close();

        }
    }

    public static void criarArquivosSerializacao(List<String[]> dadosCliente) {
        String caminho = "sistemasdistribuidos\\\\src\\\\main\\\\java\\\\com\\\\caiquekola\\\\dados\\\\dados";
        EscritaCsv.escreverCsv(caminho + ".csv", dadosCliente);
        EscritaJson.escreverJson(dadosCliente, caminho+".json");
        EscritaXML.escreverXML(dadosCliente,caminho+".xml");
        EscritaYAML.escreverYAML(dadosCliente,caminho+".yaml");
        EscritaTOML.escreverTOML(dadosCliente,caminho+".toml");
        

    }
}