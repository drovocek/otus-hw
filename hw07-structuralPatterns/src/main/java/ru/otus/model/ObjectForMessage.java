package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {

    public ObjectForMessage() {
    }

    public ObjectForMessage(ObjectForMessage objectForMessage) {
        this.data = new ArrayList<>(objectForMessage.getData());
    }

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
