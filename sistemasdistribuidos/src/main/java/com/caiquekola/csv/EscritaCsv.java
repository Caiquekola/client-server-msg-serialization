package com.caiquekola.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

public class EscritaCsv {
    public static void escreverCsv(String caminhoArquivo, List<String[]> dados){
        try (CSVWriter writer = new CSVWriter(new FileWriter(caminhoArquivo))) {
            for(String[] linha: dados){
                writer.writeNext(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao escrever o arquivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String[] cabecalho = { "Nome", "Idade", "Cidade" };
        String[] linha1 = { "Mateus", "25", "Pará" };
        String[] linha2 = { "Lia", "30", "Paraná" };
        List<String[]> dados = new ArrayList<>();
        dados.add(cabecalho);
        dados.add(linha1);
        dados.add(linha2);
        escreverCsv("dados.csv",dados);

    }
}
