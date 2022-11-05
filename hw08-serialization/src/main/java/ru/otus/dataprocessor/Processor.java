package ru.otus.dataprocessor;

import ru.otus.crm.model.Measurement;

import java.util.List;
import java.util.Map;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
