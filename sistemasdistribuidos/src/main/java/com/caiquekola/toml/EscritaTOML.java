package com.caiquekola.toml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class EscritaTOML {
    public static void escreverTOML(List<String[]> dados, String caminho) {
        try (FileWriter writer = new FileWriter(caminho)) {
            // Cabeçalho
            String[] cabecalho = dados.get(0);

            // Criar registros como seções TOML
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < dados.size(); i++) {
                String[] linha = dados.get(i);
                sb.append("[registro").append(i).append("]\n");
                for (int j = 0; j < linha.length; j++) {
                    sb.append(cabecalho[j]).append(" = \"").append(linha[j]).append("\"\n");
                }
                sb.append("\n");
            }

            // Escreve no arquivo
            writer.write(sb.toString());
            System.out.println("Arquivo TOML gerado em: " + caminho);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}