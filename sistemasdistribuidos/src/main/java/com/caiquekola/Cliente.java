package com.caiquekola;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.Serializable;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.moandjiezana.toml.TomlWriter;
import com.opencsv.CSVWriter;
import org.yaml.snakeyaml.Yaml;

public class Cliente {

    public static final String HOST = "127.0.0.1";
    public static final int PORTA = 9009;

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final XmlMapper XML = new XmlMapper();
    private static final Yaml YAML = new Yaml();
    private static final TomlWriter TOML = new TomlWriter();

    public static void main(String[] args) throws Exception {
        Dados dados = new Dados(
                "Caique Augusto de Aquino Braga",
                "123.456.789-00",
                20,
                "Mensagem muito interressante aqui!"
        );

        String conteudoCSV  = dumpCSV(dados);
        String conteudoJSON = JSON.writeValueAsString(dados);
        String conteudoXML  = XML.writeValueAsString(dados);
        String conteudoYAML = YAML.dump(beanToMap(dados));
        String conteudoTOML = TOML.write(beanToMap(dados));

        try (Socket cliente = new Socket(HOST, PORTA)) {
            DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());
            DataInputStream  entrada = new DataInputStream(cliente.getInputStream());

            enviar(saida, "CSV",  conteudoCSV);
            enviar(saida, "JSON", conteudoJSON);
            enviar(saida, "XML",  conteudoXML);
            enviar(saida, "YAML", conteudoYAML);
            enviar(saida, "TOML", conteudoTOML);

            System.out.println("Servidor: " + entrada.readUTF());
        }
    }

    private static void enviar(DataOutputStream saida, String formato, String conteudo) throws Exception {
        saida.writeUTF(formato);
        saida.writeUTF(conteudo);
        saida.flush();
        System.out.println();
    }


    private static String dumpCSV(Dados d) throws Exception {
        StringWriter sw = new StringWriter();
        try (CSVWriter w = new CSVWriter(sw)) {
            w.writeNext(new String[]{"nome", "cpf", "idade", "mensagem"});
            w.writeNext(new String[]{d.nome, d.cpf, String.valueOf(d.idade), d.mensagem});
        }
        return sw.toString();
    }

    private static Map<String, Object> beanToMap(Dados d) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("nome", d.nome);
        m.put("cpf", d.cpf);
        m.put("idade", d.idade);
        m.put("mensagem", d.mensagem);
        return m;
    }
}



@JacksonXmlRootElement(localName = "dados")
class Dados implements Serializable {
    public String nome;
    public String cpf;
    public int idade;
    public String mensagem;

    public Dados() {}
    public Dados(String nome, String cpf, int idade, String mensagem) {
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.mensagem = mensagem;
    }
}