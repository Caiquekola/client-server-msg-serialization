package com.caiquekola.csv;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class LeituraCsv {   
	public static List<String[]> lerCsv(String caminhoArquivo){
        List<String[]> dados = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new FileReader(caminhoArquivo))){
            List<String[]> linhas = reader.readAll();
            for(String[] linha: linhas){
                for(int i =0; i < linha.length; i++){
                    System.out.print(linha[i]);
                    if(i<linha.length-1){
                        System.out.print(",");
                    }
                }
                System.out.println();
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return dados;
    }

    public static void main(String[] args) {
        String caminho = "dados.csv";
        lerCsv(caminho);
    }
}