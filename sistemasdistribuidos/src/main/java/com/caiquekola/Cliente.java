package com.caiquekola;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.Serializable;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class Cliente {

    public static final String HOST = "127.0.0.1";
    public static final int PORTA = 9009;

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final XmlMapper XML = new XmlMapper();
    private static final YAMLMapper YAML = new YAMLMapper();
    private static final CsvMapper CSV = new CsvMapper();
    private static final TomlMapper TOML = new TomlMapper();
    public static void main(String[] args) throws Exception {
        Dados dados = new Dados(
                "Caique Augusto de Aquino Braga",
                "123.456.789-00",
                20,
                "Mensagem muito interessante aqui!"
        );

        CsvSchema schema = CSV.schemaFor(Dados.class).withHeader();
        String conteudoCSV = CSV.writer(schema).writeValueAsString(dados);
        String conteudoJSON = JSON.writeValueAsString(dados);
        String conteudoXML = XML.writeValueAsString(dados);
        String conteudoYAML = YAML.writeValueAsString(dados);
        String conteudoTOML = TOML.writeValueAsString(dados);
        try (Socket cliente = new Socket(HOST, PORTA)) {
            DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());
            DataInputStream entrada = new DataInputStream(cliente.getInputStream());

            enviar(saida, "CSV", conteudoCSV);
            enviar(saida, "JSON", conteudoJSON);
            enviar(saida, "XML", conteudoXML);
            enviar(saida, "YAML", conteudoYAML);
            enviar(saida,"TOML",conteudoTOML);
            System.out.println("Servidor: " + entrada.readUTF());
        }
    }

    private static void enviar(DataOutputStream saida, String formato, String conteudo) throws Exception {
        saida.writeUTF(formato);
        saida.writeUTF(conteudo);
        saida.flush();
        System.out.println("Enviado: " + formato);
    }
}

@JacksonXmlRootElement(localName = "dados")
@JsonPropertyOrder({"nome", "cpf", "idade", "mensagem"}) 
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
