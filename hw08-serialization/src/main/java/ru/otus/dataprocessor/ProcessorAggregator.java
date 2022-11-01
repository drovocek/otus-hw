package ru.otus.dataprocessor;

import ru.otus.crm.model.Measurement;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        return data.stream()
                .sorted(Comparator.comparing(Measurement::getName))
                .collect(
                        Collectors.groupingBy(Measurement::getName,
                                LinkedHashMap::new,
                                Collectors.summingDouble(Measurement::getValue))
                );
    }
}
