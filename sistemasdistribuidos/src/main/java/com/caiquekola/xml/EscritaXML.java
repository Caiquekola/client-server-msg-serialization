package com.caiquekola.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EscritaXML {
    public static void escreverXML(List<String[]> dados, String caminho) {
        try {
            XmlMapper xmlMapper = new XmlMapper();

            String[] cabecalho = dados.get(0);

            List<Map<String, String>> registros = new java.util.ArrayList<>();
            for (int i = 1; i < dados.size(); i++) {
                String[] linha = dados.get(i);
                Map<String, String> registro = new HashMap<>();
                for (int j = 0; j < linha.length; j++) {
                    registro.put(cabecalho[j], linha[j]);
                }
                registros.add(registro);
            }

            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(caminho), registros);
            System.out.println("Arquivo XML gerado em: " + caminho);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
