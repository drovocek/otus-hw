package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import ru.otus.crm.model.Measurement;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {

    /**
    * 'Gson instances are Thread-safe'
    * <a href="https://www.javadoc.io/doc/com.google.code.gson/gson/2.8.0/com/google/gson/Gson.html">Doc</a>
    * */
    private final Gson gson;
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.gson = new Gson();
    }

    @Override
    public List<Measurement> load() {
        String fullFilePath = Objects.requireNonNull(getClass().getClassLoader().getResource(this.fileName)).getPath();

        try (var reader = new JsonReader(new FileReader(fullFilePath))) {
            Type listType = new TypeToken<ArrayList<Measurement>>() {
            }.getType();
            return this.gson.fromJson(reader, listType);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
