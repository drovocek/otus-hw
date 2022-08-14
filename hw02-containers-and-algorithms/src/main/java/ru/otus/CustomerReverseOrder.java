package ru.otus;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    private final Deque<Customer> stack = new ArrayDeque<>();

    public void add(Customer customer) {
        this.stack.push(customer);
    }

    public Customer take() {
        return this.stack.pop();
    }
}