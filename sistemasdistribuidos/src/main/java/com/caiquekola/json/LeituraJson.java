package com.caiquekola.json;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LeituraJson {
    public static Map<String, String> leituraJson(String caminho) {
        Map<String, String> retorno = new HashMap<>();
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(caminho)) {
            Object obj = jsonParser.parse(reader);
            JSONObject usuarioJson = (JSONObject) obj;
            for (Object key : usuarioJson.keySet()) {
                retorno.put(key.toString(), usuarioJson.get(key).toString());
            }
        } catch (IOException | ParseException e) {
            System.err.println("Ocorreu um erro ao ler ou parsear o arquivo: " + e.getMessage());
        }
        return retorno;
    }
}