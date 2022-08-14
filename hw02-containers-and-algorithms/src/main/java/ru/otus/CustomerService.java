package ru.otus;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final Comparator<Customer> byScoreAscComparator = Comparator.comparingLong(Customer::getScores);
    private final TreeMap<Customer, String> scoreByCustomer = new TreeMap<>(byScoreAscComparator);

    public Map.Entry<Customer, String> getSmallest() {
        return deepCopy(this.scoreByCustomer.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return deepCopy(this.scoreByCustomer.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        this.scoreByCustomer.put(customer, data);
    }

    private Map.Entry<Customer, String> deepCopy(Map.Entry<Customer, String> entry) {
        return Map.entry(new Customer(entry.getKey()), entry.getValue());
    }
}