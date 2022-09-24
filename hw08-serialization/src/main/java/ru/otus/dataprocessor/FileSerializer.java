package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    /**
     * 'Gson instances are Thread-safe'
     * <a href="https://www.javadoc.io/doc/com.google.code.gson/gson/2.8.0/com/google/gson/Gson.html">Doc</a>
     * */
    private final Gson gson;
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.gson = new Gson();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        String json = this.gson.toJson(data);
        try (var writer = new BufferedWriter(new FileWriter(this.fileName))) {
            writer.write(json);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
