package com.caiquekola.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;

public class EscritaJson {

    public static void escreverJson(List<String[]> dados, String caminho){
        String[] chaves = dados.get(0);
        String[] valores = dados.get(1);

        JSONObject jsonObject = new JSONObject();
        int tamanho = Math.min(chaves.length,valores.length);
        for(int i = 0; i < tamanho; i++){
            jsonObject.put(chaves[i], valores[i]);
        }

        String jsonString = jsonObject.toJSONString();

        try (FileWriter file = new FileWriter(caminho)){
            file.write(jsonString);
            file.flush();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
}