package com.caiquekola.csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LeituraCsv {   
	public static List<String[]> lerCsv(String caminhoArquivo){
        List<String[]> dados = new ArrayList<>();
        
        try {
            List<String> linhas = Files.readAllLines(Paths.get(caminhoArquivo));
            for(String linha:linhas){
                String[] valores = linha.split(",");
                dados.add(valores);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dados;
    }
}