package ru.otus.multi;

import lombok.SneakyThrows;

/**
 * Два потока печатают числа от 1 до 10, потом от 10 до 1.
 * Надо сделать так, чтобы числа чередовались, т.е. получился такой вывод:
 * Поток 1:1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3 4....
 * Поток 2: 1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3....
 * Всегда должен начинать Поток 1.
 */
public class MultiDemo {

    private static final int[] numbers = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static String last = "Second";

    public static void main(String[] args) {
        MultiDemo demo = new MultiDemo();
        Thread t1 = new Thread(demo::print);
        t1.setName("First");
        Thread t2 = new Thread(demo::print);
        t2.setName(last);
        t1.start();
        t2.start();
    }

    private synchronized void print() {
        for (int i = 0; i < numbers.length; i++) {
            print(i);
        }

        for (int i = numbers.length - 1; i >= 0; i--) {
            print(i);
        }
    }

    @SneakyThrows
    private void print(int i) {
        String currThreadName = Thread.currentThread().getName();
        while (last.equals(currThreadName)) {
            wait();
        }
        System.out.println(Thread.currentThread().getName() + ": " + numbers[i]);
        last = currThreadName;
        Thread.sleep(200);
        notifyAll();
    }
}
