package com.caiquekola;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;

public class Servidor {

    public static final String HOST = "127.0.0.1";
    public static final int PORTA = 9009;

    private static final ObjectMapper JSON   = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final XmlMapper XML       = (XmlMapper) new XmlMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final YAMLMapper YAML     = (YAMLMapper) new YAMLMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final CsvMapper CSV       = new CsvMapper();
    private static final TomlMapper TOML     = new TomlMapper(); 

    public static void main(String[] args) throws Exception {
        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.printf("Servidor esperando conexão | %s:%d%n", HOST, PORTA);

            try (Socket conexao = servidor.accept()) {
                System.out.println("Conectado por: " + conexao.getRemoteSocketAddress());

                DataInputStream entrada = new DataInputStream(conexao.getInputStream());
                DataOutputStream saida  = new DataOutputStream(conexao.getOutputStream());

                // Recebe 5 mensagens: CSV, JSON, XML, YAML, TOML
                for (int i = 0; i < 5; i++) {
                    String formato  = entrada.readUTF();
                    String conteudo = entrada.readUTF();

                    System.out.println("\n=== " + formato + " ===");
                    switch (formato) {
                        case "JSON" -> System.out.println(loadJson(conteudo));
                        case "XML"  -> System.out.println(loadXml(conteudo));
                        case "YAML" -> System.out.println(loadYaml(conteudo));
                        case "TOML" -> System.out.println(loadToml(conteudo));
                        case "CSV"  -> System.out.println(loadCsv(conteudo));
                        default     -> System.out.println(conteudo);
                    }
                }

                saida.writeUTF("OK");
                saida.flush();
            }
        }
    }

    private static String loadJson(String raw) {
        try {
            JsonNode node = JSON.readTree(raw);
            return JSON.writeValueAsString(node);
        } catch (Exception e) {
            return raw;
        }
    }

    private static String loadXml(String raw) {
        try {
            JsonNode node = XML.readTree(raw.getBytes()); // load
            return XML.writerWithDefaultPrettyPrinter().writeValueAsString(node); // dump
        } catch (Exception e) {
            return raw;
        }
    }

    private static String loadYaml(String raw) {
        try {
            JsonNode node = YAML.readTree(raw); // load
            return YAML.writeValueAsString(node); // dump
        } catch (Exception e) {
            return raw;
        }
    }

    private static String loadToml(String raw) {
        try {
            JsonNode node = TOML.readTree(raw); // load
            return TOML.writeValueAsString(node); // dump
        } catch (Exception e) {
            return raw;
        }
    }

    private static String loadCsv(String raw) {
        try {
            CsvSchema readSchema = CsvSchema.emptySchema().withHeader();
            List<Map<String, String>> rows =
                    CSV.readerFor(Map.class).with(readSchema).readValue(raw);

            if (rows.isEmpty()) return "";
            CsvSchema.Builder b = CsvSchema.builder();
            for (String col : rows.get(0).keySet()) {
                b.addColumn(col);
            }
            CsvSchema writeSchema = b.setUseHeader(true).build();

            return CSV.writer(writeSchema).writeValueAsString(rows);
        } catch (Exception e) {
            return raw;
        }
    }
}
