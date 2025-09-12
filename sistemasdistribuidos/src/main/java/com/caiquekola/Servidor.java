package com.caiquekola;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class Servidor {

    public static final String HOST = "127.0.0.1";
    public static final int PORTA = 9009;

    private static final ObjectMapper JSON = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final XmlMapper XML = new XmlMapper();

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
            var node = JSON.readTree(raw);               // load
            return JSON.writeValueAsString(node);        // dump 
        } catch (Exception e) {
            return raw; 
        }
    }

    private static String loadXml(String raw) {
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new org.xml.sax.InputSource(new StringReader(raw)));
            doc.getDocumentElement().normalize();

            var tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StringWriter sw = new StringWriter();
            tf.transform(new DOMSource(doc), new StreamResult(sw));     // dump 
            return sw.toString();
        } catch (Exception e) {
            try {
                Object bean = XML.readValue(raw, Object.class);
                return XML.writerWithDefaultPrettyPrinter().writeValueAsString(bean);
            } catch (Exception ignore) {
                return raw;
            }
        }
    }

    private static String loadYaml(String raw) {
        try {
            Yaml loader = new Yaml();                     // load
            Object obj = loader.load(raw);

            DumperOptions op = new DumperOptions();       // dump 
            op.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            op.setPrettyFlow(true);
            Yaml dumper = new Yaml(op);
            return dumper.dump(obj);
        } catch (Exception e) {
            return raw;
        }
    }

    private static String loadToml(String raw) {
        try {
            Map<String, Object> map = new Toml().read(raw).toMap(); // load
            TomlWriter writer = new TomlWriter();                   // dump
            return writer.write(map);
        } catch (Exception e) {
            return raw;
        }
    }

    private static String loadCsv(String raw) {
        try (CSVReader reader = new CSVReader(new StringReader(raw))) {
            List<String[]> rows = reader.readAll();  // load
            StringWriter sw = new StringWriter();
            try (CSVWriter writer = new CSVWriter(sw)) {
                writer.writeAll(rows);               // dump
            }
            return sw.toString();
        } catch (Exception e) {
            return raw;
        }
    }
}
