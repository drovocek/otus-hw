package ru.otus;

import java.util.LinkedList;

public class CustomerReverseOrder {

    private final LinkedList<Customer> customers = new LinkedList<>();

    public void add(Customer customer) {
        this.customers.add(customer);
    }

    public Customer take() {
        return this.customers.pollLast();
    }
}