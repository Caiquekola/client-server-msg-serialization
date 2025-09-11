package com.caiquekola.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class EscritaYAML {
    public static void escreverYAML(List<String[]> dados, String caminho) {
        try (FileWriter writer = new FileWriter(caminho)) {
            String[] cabecalho = dados.get(0);

            List<Map<String, String>> registros = new ArrayList<>();

            for (int i = 1; i < dados.size(); i++) {
                String[] linha = dados.get(i);
                Map<String, String> registro = new LinkedHashMap<>();
                for (int j = 0; j < linha.length; j++) {
                    registro.put(cabecalho[j], linha[j]);
                }
                registros.add(registro);
            }

            // Configurações de formatação YAML
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);

            Yaml yaml = new Yaml(options);
            yaml.dump(registros, writer);

            System.out.println("Arquivo YAML gerado em: " + caminho);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
