package ru.otus.calculator;

import java.util.ArrayList;
import java.util.List;

public class Summator {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;

    private final static int MAX_IDX = 6_600_000;
    private final List<Data> listValues = new ArrayList<>();

    //!!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        generateHeapTrash(data);

        int newValue = data.getValue();
        this.sum += newValue;

        this.sumLastThreeValues = newValue + this.prevValue + this.prevPrevValue;

        this.prevPrevValue = this.prevValue;
        this.prevValue = newValue;

        int idx = listValues.size();
        int x = this.sumLastThreeValues * this.sumLastThreeValues;
        int y = newValue + 1;
        int delta = x / y - this.sum;

        this.someValue += delta;
        this.someValue = this.someValue < 0 ? -this.someValue : this.someValue;
        this.someValue = this.someValue + idx;

        this.someValue += delta;
        this.someValue = this.someValue < 0 ? -this.someValue : this.someValue;
        this.someValue = this.someValue + idx;

        this.someValue += delta;
        this.someValue = this.someValue < 0 ? -this.someValue : this.someValue;
        this.someValue = this.someValue + idx;
    }

    private void generateHeapTrash(Data data) {
        listValues.add(data);
        if (listValues.size() % MAX_IDX == 0) {
            listValues.clear();
        }
    }

    public int getSum() {
        return this.sum;
    }

    public int getPrevValue() {
        return this.prevValue;
    }

    public int getPrevPrevValue() {
        return this.prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return this.sumLastThreeValues;
    }

    public int getSomeValue() {
        return this.someValue;
    }
}