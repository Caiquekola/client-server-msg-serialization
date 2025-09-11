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
            String caminhos[] = criarArquivosSerializacao(dadosCliente);
            saida.writeObject(caminhos);

            entrada.close();
            saida.close();
            cliente.close();
        }
    }

    public static String[] criarArquivosSerializacao(List<String[]> dadosCliente) {
        String caminho = "sistemasdistribuidos\\\\src\\\\main\\\\java\\\\com\\\\caiquekola\\\\dados\\\\dados";
        String tipos[] = {".csv",".json",".xml",".yaml",".toml"};
        String caminhos[] = {caminho+tipos[0],caminho+tipos[1],caminho+tipos[2],caminho+tipos[3],caminho+tipos[4]};
        EscritaCsv.escreverCsv(caminhos[0], dadosCliente);
        EscritaJson.escreverJson(dadosCliente, caminhos[1]);
        EscritaXML.escreverXML(dadosCliente,caminhos[2]);
        EscritaYAML.escreverYAML(dadosCliente,caminhos[3]);
        EscritaTOML.escreverTOML(dadosCliente,caminhos[4]);
        return caminhos;
    }
}