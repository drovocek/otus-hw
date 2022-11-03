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

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(MultiDemo::print);
        t1.setName("First");
        Thread t2 = new Thread(MultiDemo::print);
        t2.setName("Second");
        t1.start();
        Thread.sleep(200);
        t2.start();
    }

    @SneakyThrows
    private synchronized static void print() {
        for (int i = 0; i < numbers.length; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + numbers[i]);
            MultiDemo.class.notifyAll();
            MultiDemo.class.wait();
            Thread.sleep(200);
        }

        for (int i = numbers.length - 1; i >= 0; i--) {
            System.out.println(Thread.currentThread().getName() + ": " + numbers[i]);
            MultiDemo.class.notifyAll();
            MultiDemo.class.wait();
            Thread.sleep(200);
        }
        MultiDemo.class.notifyAll();
    }
}
